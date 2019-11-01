package com.example.firstspringboot.service.impls;

import com.example.firstspringboot.commons.Constants;
import com.example.firstspringboot.dto.DocEntity;
import com.example.firstspringboot.service.interfaces.IInformationSV;
import com.example.firstspringboot.service.interfaces.IRedisSV;
import com.example.firstspringboot.utils.DateUtil;
import com.example.firstspringboot.utils.PoiUtil;
import com.example.firstspringboot.utils.RedisUtil;
import com.example.firstspringboot.vo.BaseResponse;
import com.example.firstspringboot.vo.InformationVo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.Transaction;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class InformationSVImp implements IInformationSV {

    private static final Log log = LogFactory.getLog(IInformationSV.class);

    @Autowired
    private IRedisSV iRedisSV;

    @Override
    public BaseResponse saveNewInformation(MultipartFile file, Integer type) throws Exception {

        BaseResponse response = new BaseResponse();
        String fileName = file.getOriginalFilename();

        List<DocEntity> docEntities = null;
        try {
            /*1. 本次添加是否有效 */
            //文本名 日期分离 (默认格式: 文本名_yyyyMMdd)
            String[] str = fileName.split("_");
            String date = str[1].substring(0,str[1].lastIndexOf("."));
            log.debug("原字符串为: "+str[1]+", 截取后的日期为: "+date);

            SimpleDateFormat dfI = new SimpleDateFormat("yyyyMMdd");//输入格式
            SimpleDateFormat dfO = new SimpleDateFormat("yyyy-MM-dd");//目标格式
            String releaseTime = dfO.format(dfI.parse(date));
            String redisScore = date+(((new Date()).getTime())+"").substring(9,13);//redis有序集合分数
            String title = null;//redis有序集合资讯名
            //是否已经存在相同标题 资讯
//            String already = RedisUtil.getRedis().get(title);
//            if (!(already == null || "".equals(already))) {
//                response.setCode(400);
//                response.setData(already);
//                response.setMessage("该标题资讯已经存在,请先删除原资讯[" + already + "]后上传!");
//                log.debug("该标题资讯已经存在,请先删除原资讯后上传!");
//                return response;
//            }
            /* 2.读取word文档内容 */
            if (fileName.endsWith(".doc")) {
                docEntities = PoiUtil.getTextByDoc(file.getInputStream());
            } else if (fileName.endsWith(".docx")) {
//                docEntities = PoiUtil.getTextByDocx(file.getInputStream());
                docEntities = PoiUtil.getWordByDocx(file.getInputStream());
            } else {
                response.setCode(400);
                response.setMessage("上传文件格式不对(仅限.doc .docx)!");
                return response;
            }

            /* 3.编辑资讯mxl */
            if (docEntities == null || docEntities.size() < 0) {
                response.setCode(400);
                response.setMessage("上传文件中的文本内容为空!");
                return response;
            } else {
                String mxlHeadStr = "";
                String mxlBodyStr = "<div>";
                for (DocEntity docEntity : docEntities) {
                    if (DocEntity.DOC_HEAD.equals(docEntity.getTextType())) {
                        mxlHeadStr += "<div><span style='text-align: center;font-size: 15px;'><h2><b>"
                                + docEntity.getTextValue() + "</b></h2></span></div>";
                        title = docEntity.getTextValue();
                    } else if (DocEntity.DOC_TITLE.equals(docEntity.getTextType())) {
                        mxlBodyStr += "<span style='font-size: 15px;'><h2><b>"
                                + docEntity.getTextValue() + "</b></h2></span>";
                    } else if (DocEntity.DOC_TEXT.equals(docEntity.getTextType())) {
                        mxlBodyStr += "<p><span style='font-size: 14px;line-height:2.0;'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
                                + docEntity.getTextValue() + "</span></p>";
                    } else if (DocEntity.DOC_WRAP.equals(docEntity.getTextType())) {
                        mxlBodyStr += "<br>";
                    } else if (DocEntity.DOC_IMG_SMALL.equals(docEntity.getTextType())) {
                        mxlBodyStr += "<div style='width: 40%;margin-left: 30%;'><img width='100%' src='../upload/"
                                + docEntity.getTextValue() + "'></div>";
                    } else if (DocEntity.DOC_IMG_MEDIUM.equals(docEntity.getTextType())) {
                        mxlBodyStr += "<div style='width: 60%;margin-left: 20%;'><img width='100%' src='../upload/"
                                + docEntity.getTextValue() + "'></div>";
                    } else if (DocEntity.DOC_IMG_BIG.equals(docEntity.getTextType())) {
                        mxlBodyStr += "<div style='width: 80%;margin-left: 10%;'><img width='100%' src='../upload/"
                                + docEntity.getTextValue() + "'></div>";
                    } else if (DocEntity.DOC_IMG_DESCRIPTION.equals(docEntity.getTextType())) {
                        mxlBodyStr += "<span style='text-align: center;font-size: 14px;'><h2><b>"
                                + docEntity.getTextValue() + "</b></h2></span>";
                    }
                }
                mxlBodyStr += "</div>";

                /* 3.添加缓存 */
                //查询缓存中是否存在相同 文章标题
                Double score = RedisUtil.getRedis().zscore(Constants.InformationType.ALL_INFORMATION,title);
                if (!score.isNaN()){
                    log.debug("存在相同的文章标题:"+title);
                    response.setCode(400);
                    response.setData(title);
                    response.setMessage("该标题资讯已经存在,请先删除原资讯[" + title + "]后上传!");
                    log.debug("该标题资讯已经存在,请先删除原资讯后上传!");
                    return response;
                }
                //时间 做为redis key值
                InformationVo informationVo = new InformationVo();
                informationVo.setNewsId(redisScore);
                informationVo.setTitle(title);
                informationVo.setDate(releaseTime);
                informationVo.setDivInfo(mxlHeadStr + mxlBodyStr);
                //添加到对应类型缓存列表
                log.debug("添加到资讯类型列表缓存>>>>>>>");
                if (type == Constants.InformationType.ADD_NEWS_TYPE) {
                    RedisUtil.getRedis().zadd(Constants.InformationType.NEWS_TYPE,Double.valueOf(informationVo.getNewsId()),informationVo.getTitle());
                } else if (type == Constants.InformationType.ADD_ANNOUNCEMENT_TYPE) {
                    RedisUtil.getRedis().zadd(Constants.InformationType.ANNOUNCEMENT_TYPE,Double.valueOf(informationVo.getNewsId()),informationVo.getTitle());
                }
                Map<String, String> map = new HashMap<>();
                map.put(InformationVo.TITLE, informationVo.getTitle());
                map.put(InformationVo.DATE, informationVo.getDate());
                map.put(InformationVo.DIVINFO, informationVo.getDivInfo());
                log.debug("新的资讯信息:" + informationVo.getNewsId());
                RedisUtil.getRedis().hmset(informationVo.getNewsId(), map);
                //添加 有序集合, 对应类型资讯添加 全部类型资讯添加
                RedisUtil.getRedis().set(informationVo.getTitle(), informationVo.getNewsId());

                response.setData(informationVo);
            }
        } catch (ParseException pe) {
            log.debug(pe.fillInStackTrace());
            response.setCode(400);
            response.setMessage("上传文件名时间格式不对,请参照格式: 资讯标题名_yyyyMMdd !");
        } catch (ArrayIndexOutOfBoundsException indexE) {
            log.debug(indexE.fillInStackTrace());
            response.setCode(400);
            response.setMessage("上传文件名格式不对,请参照格式: 资讯标题名_yyyyMMdd !");
        } catch (Exception e) {
            log.debug(e.getMessage());
            response.setCode(400);
            response.setMessage(e.getMessage());
        } finally {
            RedisUtil.getRedis().close();
        }
        return response;
    }

    @Override
    public BaseResponse getIndexShow() throws Exception {
        BaseResponse response = new BaseResponse();
        List<InformationVo> all = iRedisSV.getInformationAll(6);
        if (!(all.size() > 0)){
            response.setMessage("没有查询到相关数据!");
            response.setCode(400);
            return response;
        }
        response.setData(all);
        response.setMessage("查询成功!");
        response.setCode(200);
        return response;
    }

    @Override
    public BaseResponse delInformationById(String id) throws Exception {
        BaseResponse response = new BaseResponse();
        InformationVo informationVo = iRedisSV.getHashMapById(id);
        /* 获取redis事务 */
        Transaction multi = RedisUtil.getRedis().multi();
        try {
            //清除 列表缓存
            multi.lrem(Constants.InformationType.ANNOUNCEMENT_TYPE, 1, id);
            multi.lrem(Constants.InformationType.NEWS_TYPE, 1, id);
            //清除 资讯信息缓存
            multi.del(id);
            //清除 资讯标题-id 键值对
            multi.del(informationVo.getTitle());
            /* 结束事务 提交 */
            multi.exec();
            response.setData("delete: " + informationVo.getTitle() + "[" + informationVo.getNewsId() + "]");
        } catch (Exception e) {
            e.fillInStackTrace();
            log.debug(e.fillInStackTrace());
            //事务回滚
            multi.discard();
            response.setCode(400);
            response.setMessage("删除失败,原因: " + e.getMessage());
        } finally {
            RedisUtil.getRedis().close();
        }

        return response;
    }

    @Override
    public void initialization() throws Exception {
        log.debug("开始初始化资讯>>>>>>>>>");
        /* 1.遍历指定文件夹下的word文档 */
        /* 2.读取文档内容 */
        /* 3.覆盖原来缓存 原来没有则新建缓存 */
        String path = "C:\\MyFile\\需求说明等\\公告";
        System.out.println("遍历文件夹["+path+"]下文件");
        File file = new File(path);
        File[] files = file.listFiles();
        for (File file1 : files) {
            if (!file1.isDirectory() && file1.getName().endsWith(".docx")){
                String[] str = file1.getName().split("_");
                if (checkFileName(file1.getName()) != null){
                    log.debug(checkFileName(file1.getName()));
                    continue;
                }
                String date = str[1].substring(0,str[1].lastIndexOf("."));
                SimpleDateFormat dfI = new SimpleDateFormat("yyyyMMdd");//输入格式
                SimpleDateFormat dfO = new SimpleDateFormat("yyyy-MM-dd");//目标格式
                String redisScore = date+((new Date()).getTime()+"").substring(9,13);
                String releaseTime = null;
                try {
                    releaseTime = dfO.format(dfI.parse(date));
                } catch (ParseException e) {
                    e.printStackTrace();
                    log.debug("文件["+file1.getName()+"]日期格式转换失败!");
                    continue;
                }
                FileInputStream inp = null;
                try {
                    inp = new FileInputStream(file1);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    log.debug("文件["+file1.getName()+"]未找到!");
                    continue;
                }
            }
        }
            List<DocEntity> docEntities = null;
            try {
                /*1. 本次添加是否有效 */
                //文本名 日期分离 (默认格式: 文本名_yyyyMMdd)
                String[] str = {};
                String title = str[0];
                String date = str[1].substring(0,str[1].lastIndexOf("."));
                log.debug("原字符串为: "+str[1]+", 截取后的日期为: "+date);

                SimpleDateFormat dfI = new SimpleDateFormat("yyyyMMdd");//输入格式
                SimpleDateFormat dfO = new SimpleDateFormat("yyyy-MM-dd");//目标格式
                String releaseTime = dfO.format(dfI.parse(date));
                //是否已经存在相同标题 资讯
                String already = RedisUtil.getRedis().get(title);
                if (!(already == null || "".equals(already))) {

                }

                /* 3.编辑资讯mxl */
                if (docEntities == null || docEntities.size() < 0) {

                } else {
                    String mxlHeadStr = "";
                    String mxlBodyStr = "<div>";
                    for (DocEntity docEntity : docEntities) {
                        if (DocEntity.DOC_HEAD.equals(docEntity.getTextType())) {
                            mxlHeadStr += "<div><span style='text-align: center;font-size: 15px;'><h2><b>"
                                    + docEntity.getTextValue() + "</b></h2></span></div>";
                        } else if (DocEntity.DOC_TITLE.equals(docEntity.getTextType())) {
                            mxlBodyStr += "<span style='font-size: 15px;'><h2><b>"
                                    + docEntity.getTextValue() + "</b></h2></span>";
                        } else if (DocEntity.DOC_TEXT.equals(docEntity.getTextType())) {
                            mxlBodyStr += "<p><span style='font-size: 14px;line-height:2.0;'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
                                    + docEntity.getTextValue() + "</span></p>";
                        } else if (DocEntity.DOC_WRAP.equals(docEntity.getTextType())) {
                            mxlBodyStr += "<br>";
                        } else if (DocEntity.DOC_IMG_SMALL.equals(docEntity.getTextType())) {
                            mxlBodyStr += "<div style='width: 40%;margin-left: 30%;'><img width='100%' src='../upload/"
                                    + docEntity.getTextValue() + "'></div>";
                        } else if (DocEntity.DOC_IMG_MEDIUM.equals(docEntity.getTextType())) {
                            mxlBodyStr += "<div style='width: 60%;margin-left: 20%;'><img width='100%' src='../upload/"
                                    + docEntity.getTextValue() + "'></div>";
                        } else if (DocEntity.DOC_IMG_BIG.equals(docEntity.getTextType())) {
                            mxlBodyStr += "<div style='width: 80%;margin-left: 10%;'><img width='100%' src='../upload/"
                                    + docEntity.getTextValue() + "'></div>";
                        } else if (DocEntity.DOC_IMG_DESCRIPTION.equals(docEntity.getTextType())) {
                            mxlBodyStr += "<span style='text-align: center;font-size: 14px;'><h2><b>"
                                    + docEntity.getTextValue() + "</b></h2></span>";
                        }
                    }
                    mxlBodyStr += "</div>";

                    /* 3.添加缓存 */
                    //时间 做为redis key值
                    InformationVo informationVo = new InformationVo();
                    informationVo.setNewsId(DateUtil.getNowDate());
                    informationVo.setTitle(title);
                    informationVo.setDate(releaseTime);
                    informationVo.setDivInfo(mxlHeadStr + mxlBodyStr);
                    //添加到对应类型缓存列表
                    log.debug("添加到资讯类型列表缓存>>>>>>>");

                    Map<String, String> map = new HashMap<>();
                    map.put(InformationVo.TITLE, informationVo.getTitle());
                    map.put(InformationVo.DATE, informationVo.getDate());
                    map.put(InformationVo.DIVINFO, informationVo.getDivInfo());
                    log.debug("新的资讯信息:" + informationVo.getNewsId());
                    RedisUtil.getRedis().hmset(informationVo.getNewsId(), map);
                    //添加 key value : 标题名 id
                    RedisUtil.getRedis().set(informationVo.getTitle(), informationVo.getNewsId());

                }
            } catch (ParseException pe) {
                log.debug(pe.fillInStackTrace());
            } catch (ArrayIndexOutOfBoundsException indexE) {
                log.debug(indexE.fillInStackTrace());
            } catch (Exception e) {
                log.debug(e.getMessage());
            } finally {
                RedisUtil.getRedis().close();
            }
            log.info("初始化完成>>>>>>>>>>>>>>>");

    }

    /**
     * 文档命名格式校验
     * @param fileName
     * @return
     */
    private String checkFileName(String fileName){
        String massage = null;
        String[] str = fileName.split("_");
        if (!fileName.endsWith(".docx")&& !(fileName.endsWith(".doc"))){
            massage = "文件["+fileName+"]类型不正确!";
            return massage;
        }
        if(str.length != 2){
            massage = "文件["+fileName+"]命名格式不符合规范(XXXXX_yyyyMMdd)!";
            return massage;
        }
        String date = str[1].substring(0,str[1].lastIndexOf("."));
        if (date.length() != 8){
            massage = "文件["+fileName+"]日期格式不规范,例如:20190527 而不是2019527";
            return massage;
        }
        return massage;
    }
}
