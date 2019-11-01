package com.example.firstspringboot;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.extractor.POITextExtractor;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.hwpf.model.PicturesTable;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;
import org.junit.Test;

import java.io.*;
import java.util.*;

public class TestPoi {
    private static final Log logger = LogFactory.getLog(TestPoi.class);

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

    public List getInformationByWord(String path) throws Exception {

        List<DocEntity> docEntities = null;
        try {
            if (path.endsWith(".doc")) {
                InputStream inputStream = new FileInputStream(new File(path));
                WordExtractor extractor = new WordExtractor(inputStream);
                String[] textS = extractor.getParagraphText();
                extractor.close();
                docEntities = getDocEntityVo(textS);
            } else if (path.endsWith(".docx")) {
                OPCPackage opcPackage = XWPFDocument.openPackage(path);
                XWPFWordExtractor extractor = new XWPFWordExtractor(opcPackage);
                String[] textS = extractor.getText().split("\n");
                extractor.close();
                docEntities = getDocEntityVo(textS);
            } else {
                throw new RuntimeException("文件格式不对!");
            }
        } catch (Exception e) {
            logger.debug("读取出错:" + e.fillInStackTrace());
            throw new RuntimeException("读取出错!");
        }

        return docEntities;
    }

    public static String getImageAndText(String path) {
        File file = new File(path);
        String content = null;
        if (file.exists() && file.isFile()) {
            InputStream is = null;
            HWPFDocument doc = null;
            XWPFDocument docx = null;
            POITextExtractor extractor = null;
            try {
                is = new FileInputStream(file);
                if (path.endsWith(".doc")) {
                    doc = new HWPFDocument(is);

                    //文档文本内容
                    content = doc.getDocumentText();
                    System.out.println(content);
                    //文档图片内容
                    PicturesTable pictures = doc.getPicturesTable();
                    List<Picture> pictureList = pictures.getAllPictures();
                    for (Picture picture : pictureList) {
                        FileOutputStream out = new FileOutputStream(
                                new File("target/doc/" + UUID.randomUUID() + "." + picture.suggestFileExtension()));
                        picture.writeImageContent(out);
                        out.close();
                    }

                } else if (path.endsWith(".docx")) {
                    docx = new XWPFDocument(is);
                    extractor = new XWPFWordExtractor(docx);

                    //文档文本内容
                    content = extractor.getText();
                    System.out.println(content);
                    //文档图片内容
                    List<XWPFPictureData> pictureDataList = docx.getAllPictures();
                    for (XWPFPictureData xwpfPictureData : pictureDataList) {
                        byte[] bytes = xwpfPictureData.getData();
                        FileOutputStream out = new FileOutputStream(
                                new File("target/docx/"+UUID.randomUUID()+xwpfPictureData.getFileName()));
                        out.write(bytes);
                        out.close();
                    }

                }else{
                    System.out.println("文件不是word文档!");
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void main(String[] args) {
        getImageAndText("C:\\MyFile\\imageTest.docx");
    }

    /*public static void main(String[] args) {

        List<DocEntity> information = new ArrayList<>();
        TestPoi testPoi = new TestPoi();
        try {
//            String information = testPoi.getInformationByWord("C:\\MyFile\\crm.docx");
            information = testPoi.getInformationByWord("C:\\MyFile\\300ts.doc");
//            information = testPoi.getInformationByWord("C:\\MyFile\\test3.docx");
            for (DocEntity docEntity : information) {
                System.out.println(docEntity.getTextType() + ":" + docEntity.getTextValue());
            }
        } catch (Exception e) {
            System.out.println("运行错误:" + e.getMessage());
        }

    }*/
}
