package com.example.firstspringboot.utils;

import com.alibaba.fastjson.JSON;
import com.example.firstspringboot.dto.DocEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * 文本读取工具类(.doc  .docx  )
 */
public class PoiUtil {
    private static final Log logger = LogFactory.getLog(PoiUtil.class);

    public static List getDocEntityVo(String[] textS) {
        List<DocEntity> docEntities = new ArrayList<>();
        for (String text : textS) {
            DocEntity docEntity = new DocEntity();
            if (!"".equals(text) && text != null) {
                if (text.contains(DocEntity.DOC_HEAD)) {
                    text = text.replace(DocEntity.DOC_HEAD, "");
                    docEntity.setTextType(DocEntity.DOC_HEAD);
                } else if (text.contains(DocEntity.DOC_TITLE)) {
                    text = text.replace(DocEntity.DOC_TITLE, "");
                    docEntity.setTextType(DocEntity.DOC_TITLE);
                } else if (text.contains(DocEntity.DOC_IMG_BIG)) {
                    text = text.replace(DocEntity.DOC_IMG_BIG, "");
                    docEntity.setTextType(DocEntity.DOC_IMG_BIG);
                } else if (text.contains(DocEntity.DOC_IMG_MEDIUM)) {
                    text = text.replace(DocEntity.DOC_IMG_MEDIUM, "");
                    docEntity.setTextType(DocEntity.DOC_IMG_MEDIUM);
                } else if (text.contains(DocEntity.DOC_IMG_SMALL)) {
                    text = text.replace(DocEntity.DOC_IMG_SMALL, "");
                    docEntity.setTextType(DocEntity.DOC_IMG_SMALL);
                } else if (text.contains(DocEntity.DOC_FOOT)) {
                    text = text.replace(DocEntity.DOC_FOOT, "");
                    docEntity.setTextType(DocEntity.DOC_FOOT);
                } else if (text.contains(DocEntity.DOC_IMG_DESCRIPTION)) {
                    text = text.replace(DocEntity.DOC_IMG_DESCRIPTION, "");
                    docEntity.setTextType(DocEntity.DOC_IMG_DESCRIPTION);
                } else {
                    docEntity.setTextType(DocEntity.DOC_TEXT);
                }
                docEntity.setTextValue(text);
            } else {
                docEntity.setTextType(DocEntity.DOC_WRAP);
            }
            docEntities.add(docEntity);
        }
        return docEntities;
    }

    public static List<DocEntity> getTextByDoc(InputStream inputStream) throws Exception {

        List<DocEntity> docEntities = null;
        try {
            WordExtractor extractor = new WordExtractor(inputStream);
            String[] textS = extractor.getParagraphText();
            extractor.close();
            docEntities = getDocEntityVo(textS);
        } catch (Exception e) {
            logger.debug("文本读取出错:" + e.fillInStackTrace());
            throw new RuntimeException("文本读取出错!");
        }
        return docEntities;
    }

    public static List<DocEntity> getTextByDocx(InputStream inputStream) throws Exception {

        List<DocEntity> docEntities = null;
        try {
            OPCPackage opcPackage = OPCPackage.open(inputStream);
            XWPFWordExtractor extractor = new XWPFWordExtractor(opcPackage);
            String[] textS = extractor.getText().split("\n");
            extractor.close();
            docEntities = getDocEntityVo(textS);
        } catch (Exception e) {
            logger.debug("文本读取出错:" + e.fillInStackTrace());
            throw new RuntimeException("文本读取出错!");
        }
        return docEntities;
    }

    /**
     * 获取docx 文件内容 并保存包含的图片
     *
     * @param inputStream
     * @return
     * @throws Exception
     */
    public static List<DocEntity> getWordByDocx(InputStream inputStream) throws Exception {
        List<DocEntity> docEntities = new ArrayList<>();
        String absolutePath = "target/docx/image";
        XWPFDocument xDocument = new XWPFDocument(inputStream);
        List<XWPFParagraph> paragraphs = xDocument.getParagraphs();
        List<XWPFPictureData> pictures = xDocument.getAllPictures();
        Map<String, String> map = new HashMap<String, String>();
        for (XWPFPictureData picture : pictures) {
            String id = picture.getParent().getRelationId(picture);
            File folder = new File(absolutePath);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            String rawName = picture.getFileName();
            String fileExt = rawName.substring(rawName.lastIndexOf("."));
            String newName = System.currentTimeMillis() + UUID.randomUUID().toString() + fileExt;
            File saveFile = new File(absolutePath + File.separator + newName);
            @SuppressWarnings("resource") FileOutputStream fos = new FileOutputStream(saveFile);
            fos.write(picture.getData());
            map.put(id, saveFile.getAbsolutePath());
        }
        StringBuffer title = new StringBuffer();
        boolean haveHead = false;//文件大标题是否已经添加
        boolean previousIsImage = false;//上个word内容是否为图片
        for (XWPFParagraph paragraph : paragraphs) {
            DocEntity entity = new DocEntity();
            List<XWPFRun> runs = paragraph.getRuns();
            StringBuffer sb = new StringBuffer();
            String imageSrc = null;
            for (XWPFRun run : runs) {

                //获取图片位置
                if (run.getCTR().xmlText().indexOf("<w:drawing>") != -1) {
                    System.out.println(run.getCTR().xmlText());
                    //获取 xml 内容
                    String runXmlText = run.getCTR().xmlText();
                    //定位 r:embed 所在位置
                    int rIdIndex = runXmlText.indexOf("r:embed");
                    int rIdEndIndex = runXmlText.indexOf("/>", rIdIndex);
                    String rIdText = runXmlText.substring(rIdIndex, rIdEndIndex);
                    String id = rIdText.split("\"")[1];
                    System.out.println("<img src = '" + map.get(id) + "'/>");
                    imageSrc = map.get(id);
                } else {
                    sb.append(run);
                }
            }
            entity.setTextValue(sb.toString());
            //获取大标题(拿第一行的有效文字)
            if (!haveHead && paragraph.getRuns().size() > 0 && !"".equals(sb.toString().replace(" ", ""))) {
                haveHead = true;
                System.out.println("添加标题:" + paragraph.getRuns());
                entity.setTextType(DocEntity.DOC_HEAD);
            }
            //获取图片
            else if (imageSrc != null) {
                entity.setTextType(DocEntity.DOC_IMG_MEDIUM);
                //图片类型  覆盖前面的值
                entity.setTextValue(imageSrc);
                previousIsImage = true;//对下个内容来说 上个是图片类型
            }
            //获取小标题(默认为除大标题外 其他加粗字体)  或图片下方说明
            else if (!"".equals(sb.toString().replace(" ", "")) && paragraph.getRuns().get(0).isBold()) {

                if (previousIsImage) {
                    entity.setTextType(DocEntity.DOC_IMG_DESCRIPTION);
                } else {
                    entity.setTextType(DocEntity.DOC_TITLE);
                }
                previousIsImage = false;
            }
            //获取换行符
            else if ("".equals(sb.toString()) || "".equals(sb.toString().replace(" ", ""))) {
                if (!haveHead){
                    //文章名还没出现 就有换行符,这是没有灵魂的标题, 去掉去掉
                    continue;
                }
                entity.setTextType(DocEntity.DOC_WRAP);
                previousIsImage = false;
            }
            //获取文本 或图片下方说明
            else {
                if (previousIsImage) {
                    entity.setTextType(DocEntity.DOC_IMG_DESCRIPTION);
                } else {
                    entity.setTextType(DocEntity.DOC_TEXT);
                }
                previousIsImage = false;
            }
            docEntities.add(entity);
        }
        System.out.println(JSON.toJSONString(docEntities));
        logger.debug(JSON.toJSONString(docEntities));
        return docEntities;
    }
}
