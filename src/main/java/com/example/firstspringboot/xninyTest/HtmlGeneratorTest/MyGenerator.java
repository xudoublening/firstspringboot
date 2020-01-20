package com.example.firstspringboot.xninyTest.HtmlGeneratorTest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 功能描述: jsp页面生成器
 * 〈〉
 * Created by Xnn on 2020/1/19 10:11
 */
public class MyGenerator {
    
    // 文件输出地址
    private static final String outFileAbsolutePath = "C:\\MyFile\\myTest";
    
    public static byte[] getOutByte(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<div id='1'>测试div</div>");
        return stringBuilder.toString().getBytes();
    }
    
    public static byte[] getByteForFile(InputStream in){
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            byte[] bytes = new byte[2048];
            int len = 0;
            while (-1 != (len = in.read(bytes))){
                out.write(bytes,0,len);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try{
                if (in != null){
                    in.close();
                }
                out.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return out.toByteArray();
    }
    
    public static void outToFile(byte[] data,String fileName){
        
        String name = fileName + ".jsp";
        File file = new File(outFileAbsolutePath + File.separator + name);
        FileOutputStream fo = null;
        try {
            fo = new FileOutputStream(file);
            fo.write(getByteForFile(new FileInputStream(new File("C:\\MyFile\\spring_boot\\firstspringboot\\src\\main\\webapp\\WEB-INF\\jsp\\information\\headTemplate.jsp"))));
            fo.write(data);
            fo.write(getByteForFile(new FileInputStream(new File("C:\\MyFile\\spring_boot\\firstspringboot\\src\\main\\webapp\\WEB-INF\\jsp\\information\\tailTemplate.jsp"))));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fo != null){
                try {
                    fo.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public static void main(String[] args) {
        outToFile(getOutByte(),"20200119");
    }
    
}
