package com.example.firstspringboot;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TestEncryption {
    private static final Log logger = LogFactory.getLog(TestEncryption.class);

    public static String getJiaMiText(String text){
        try{
            text = DES.encode(text);
        }catch (Exception e){
            e.fillInStackTrace();
            logger.debug(e.getMessage());
        }
        return text;
    }

    public static String getJieMiText(String text){
        try{
            text = DES.decode(text);
        }catch (Exception e){
            e.fillInStackTrace();
            logger.debug(e.getMessage());
        }
        return text;
    }

    public static void main(String[] args) {
        String passWord = "xnn112233";
        passWord = getJiaMiText(passWord);
        System.out.println("加密后结果:"+passWord);
        passWord = getJieMiText(passWord);
        System.out.println("解密后结果:"+passWord);
    }
}
