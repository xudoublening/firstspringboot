package com.example.firstspringboot;

import com.alibaba.fastjson.JSON;
import com.sun.javaws.HtmlOptions;
import org.apache.poi.xwpf.converter.core.BasicURIResolver;
import org.apache.poi.xwpf.converter.core.FileImageExtractor;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.usermodel.*;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TestPoiImage {
    public static void testPoi2() throws IOException {
        System.out.println("开始格式转换>>>>>>>");
        String filepath = "C:\\MyFile\\styleTest.docx";
        String targetFileName = "target/test11.html";
        String imagePathStr = "target/docx/";
        OutputStreamWriter outputStreamWriter = null;
        try {
            XWPFDocument document = new XWPFDocument(new FileInputStream(filepath));
            XHTMLOptions options = XHTMLOptions.create();
            // 存放图片的文件夹
            options.setExtractor(new FileImageExtractor(new File(imagePathStr)));
            // html中图片的路径
            options.URIResolver(new BasicURIResolver("image"));
            outputStreamWriter = new OutputStreamWriter(new FileOutputStream(targetFileName), "utf-8");
            XHTMLConverter xhtmlConverter = (XHTMLConverter) XHTMLConverter.getInstance();
            System.out.println("开始转换>>>>>>>>>>");
            xhtmlConverter.convert(document, outputStreamWriter, options);
        }catch (Exception e){
            e.fillInStackTrace();
        }finally {
            if (outputStreamWriter != null) {
                outputStreamWriter.close();
            }
        }
    }
    public static void testPoi1() throws IOException {
        String importPath = "C:\\MyFile\\华翔联信xxx_2019年5月23日.docx";
        String absolutePath = "target/docx/image";
        try {
            FileInputStream inputStream = new FileInputStream(importPath);
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
//                System.out.println(id);
//                System.out.println(saveFile.getAbsolutePath());
                map.put(id, saveFile.getAbsolutePath());
            }

            boolean haveHead = false;
            boolean previousIsImage = false;
            StringBuffer title = new StringBuffer();
            for (XWPFParagraph paragraph : paragraphs) {
                String imageSrc = null;
//                System.out.println(paragraph.getParagraphText());
//                if (paragraph.getRuns().size() > 0){
//                    System.out.println("==字体大小:"+paragraph.getRuns().get(0).getFontSize()
//                            +"==字体风格:"+paragraph.getRuns().get(0).getFontFamily()
//                            +"==文本位置:"+paragraph.getRuns().get(0).getTextPosition()
//                            +"==是否加粗:"+paragraph.getRuns().get(0).isBold()
//                            +"==对齐方式:"+paragraph.getRuns().get(0).getTextScale());
//                }
                List<XWPFRun> runs = paragraph.getRuns();
//                System.out.println(paragraph.getStyle()+": "+paragraph.getText());
                StringBuffer sb = new StringBuffer();
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
//                        System.out.println(rIdText);
//                        System.out.println(rIdText.split("\"")[1].substring("rId".length()));
                        String id = rIdText.split("\"")[1];
//                        System.out.println("<img src = '" + map.get(id) + "'/>");
//                        text = text + "<img src = '" + map.get(id) + "'/>";
                        imageSrc = map.get(id);
                    }else{
                        sb.append(run);
                    }
                    //获取标题位置
//                    else if (run.getCTR().xmlText().indexOf("<w:sz w:val=") != -1){
//                        System.out.println(run.getCTR().xmlText());
//                        title.append(run);
//                    }else {
//                        text += run;
//                    }
                }
                if (!haveHead && paragraph.getRuns().size()>0 && !"".equals(sb.toString().replace(" ",""))){
                    haveHead = true;
                    System.out.println("添加标题:"+paragraph.getRuns());
                }//获取图片
                else if (imageSrc != null){
                    previousIsImage = true;//对下个word内容来说 上一个是图片
                    System.out.println("添加图片:"+imageSrc);
                }
                //获取小标题(默认为除大标题外 其他加粗字体)
                else if (!"".equals(sb.toString().replace(" ","")) && paragraph.getRuns().get(0).isBold()){

                    if (previousIsImage){
                        System.out.println("添加图片下说明:"+sb);
                    }else{
                        System.out.println("添加小标题:"+sb);
                    }
                    previousIsImage = false;
                }else if ("".equals(sb.toString()) || "".equals(sb.toString().replace(" ",""))){
                    System.out.println("添加回车:"+sb);
                    previousIsImage = false;
                }else{
                    if (previousIsImage){
                        System.out.println("添加图片下文本:"+sb);
                    }else{
                        System.out.println("添加文本:"+sb);
                    }
                    previousIsImage = false;
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        try {
            testPoi1();
//            testPoi2();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*public static void main(String[] args) {
        String importPath = "C:\\MyFile\\imageTest.docx";
        String absolutePath = "target/docx/";
        try {
            FileInputStream inputStream = new FileInputStream(importPath);
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
                System.out.println(id);
                System.out.println(saveFile.getAbsolutePath());
                map.put(id, saveFile.getAbsolutePath());
            }
            String text = "";
            for (XWPFParagraph paragraph : paragraphs) {
//                System.out.println(paragraph.getParagraphText());
                List<XWPFRun> runs = paragraph.getRuns();
                for (XWPFRun run : runs) {
                    System.out.println(run.getCTR().xmlText());
                    if (run.getCTR().xmlText().indexOf("<w:drawing>") != -1) {
                        String runXmlText = run.getCTR().xmlText();
                        int rIdIndex = runXmlText.indexOf("r:embed");
                        int rIdEndIndex = runXmlText.indexOf("/>", rIdIndex);
                        String rIdText = runXmlText.substring(rIdIndex, rIdEndIndex);
                        System.out.println(rIdText.split("\"")[1].substring("rId".length()));
                        String id = rIdText.split("\"")[1];
                        System.out.println(map.get(id));
                        text = text + "<img src = '" + map.get(id) + "'/>";
                    } else {
                        text = text + run;
                    }
                }
            }
            System.out.println(text);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }*/
}
