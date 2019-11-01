package com.example.firstspringboot.controller;

import com.example.firstspringboot.commons.Constants;
import com.example.firstspringboot.service.interfaces.IInformationSV;
import com.example.firstspringboot.service.interfaces.IRedisSV;
import com.example.firstspringboot.vo.BaseResponse;
import com.example.firstspringboot.vo.InformationVo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/information")
public class InformationController {

    private static final Log log = LogFactory.getLog(InformationController.class);

    @Autowired
    private IRedisSV iRedisSV;
    @Autowired
    private IInformationSV iInformationSV;

    /**
     * 目录列表查看
     * @param key
     * @return
     */
    @GetMapping("/list/{key}")
    public BaseResponse getNews(@PathVariable Integer key){

        log.info("==============>新闻目录查询: "+key);
        BaseResponse response = new BaseResponse();
        String listKey = null;
        if (key == Constants.InformationType.ADD_NEWS_TYPE){
            listKey = Constants.InformationType.NEWS_TYPE;
        }else if (key == Constants.InformationType.ADD_ANNOUNCEMENT_TYPE){
            listKey = Constants.InformationType.ANNOUNCEMENT_TYPE;
        }else{
            response.setData(400);
            response.setMessage("查询类型["+key+"]不存在!");
            return  response;
        }
        List<InformationVo> voList = null;
        try {
            voList = iRedisSV.getInformation(listKey);
            response.setData(voList);
        }catch (Exception e){
            e.fillInStackTrace();
            response.setData(400);
            response.setMessage("查询过程出错,请稍后重试");
            response.setData(e.getMessage());
            throw new RuntimeException("查询过程出错,请稍后重试");
        }
        return response;
    }
    @GetMapping(value = {"/indexShow"})
    public BaseResponse getIndexShow(){
        log.debug("获取首页展示的最新资讯>>>>>>>");
        BaseResponse response = null;
        try {
            response = iInformationSV.getIndexShow();
        } catch (Exception e) {
            response.setCode(400);
            response.setMessage("查询失败,原因: "+e.fillInStackTrace());
            e.printStackTrace();
        }
        return response;
    }

    @GetMapping(value = {"/delInformation/{id}"})
    public BaseResponse deleteInformation(@PathVariable String id){
        log.debug("删除资讯id:"+id);
        BaseResponse response = null;
        try{
            response = iInformationSV.delInformationById(id);
        }catch (Exception e){
            log.debug(e.fillInStackTrace());
        }
        return response;
    }
    @GetMapping(value = {"/information/{id}"})
    public BaseResponse getInformation(@PathVariable String id){
        log.debug("查看资讯id:"+id);
        BaseResponse response = new BaseResponse();
        try{
            InformationVo informationVo = iRedisSV.getHashMapById(id);
            response.setData(informationVo);
        }catch (Exception e){
            log.debug(e.fillInStackTrace());
        }
        return response;
    }

    /**
     * 上传新的资讯
     * @param file
     * @throws Exception
     */
    @PostMapping(value = {"/addInformation/{type}"},consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseResponse uploadInformationFile(@RequestParam("news_file") MultipartFile file,
                                              @PathVariable Integer type)throws Exception{
        log.debug("接收的文件名:"+file.getOriginalFilename()+"  更新类型:"+type);
        BaseResponse response = null;
        if (type != Constants.InformationType.ADD_NEWS_TYPE
                && type != Constants.InformationType.ADD_ANNOUNCEMENT_TYPE){
            response = new BaseResponse();
            response.setData(400);
            response.setMessage("更新类型不正确!");
            return  response;
        }
        if (file==null || file.getSize() < 0){
            response = new BaseResponse();
            response.setData(400);
            response.setMessage("请选择文件上传!");
            return  response;
        }
        response = iInformationSV.saveNewInformation(file,type);
        return  response;
    }
}
