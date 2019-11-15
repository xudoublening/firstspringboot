package com.example.firstspringboot.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * 功能描述: MD5相关操作
 * 〈〉
 * @param:
 * @return:
 * @author: xnn
 * on 2019/11/1 14:56
 */
public class MD5Util {
    
    private static final String key = "xnn_project";
    
    public static String encryption(String str,String key){
        
        byte[] hash = null;
        try {
            hash = MessageDigest.getInstance("MD5").digest((str+key).getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) {
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }
    
    public static String encryption2(String str,String key){
        
        return DigestUtils.md5Hex(getBytesByCharsetName(str+key,"UTF-8"));
    }
    
    public static String myDigest(String md5Str,String key){
        
        md5Str = md5Str + key;
        StringBuffer buffer = new StringBuffer();
        char[] chars = md5Str.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            int dec = (int)chars[i]+100;
            buffer.append(""+dec);
        }
    
        return buffer.toString();
    }
    
    public static byte[] getBytesByCharsetName(String str,String charsetName){
        if (charsetName.isEmpty()){
            return str.getBytes();
        }
        try {
            return str.getBytes(charsetName);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException("指定编码集错误["+charsetName+"]");
        }
    }
    
    public static void main(String[] args) {
    
        System.out.println(myDigest(DateUtil.nowEns(),key));
    }
    
}
