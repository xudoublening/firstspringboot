package com.example.firstspringboot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class TestString {
    public static void main(String[] args) {
        String name = "华翔联信_20190527.docx";
        String[] str = name.split("_");
        System.out.println(str.length);
        if(str.length != 2){
            System.out.println("文件["+name+"]命名格式不符合规范(XXXXX_yyyyMMdd)!");
        }
        String date = str[1].substring(0,str[1].lastIndexOf("."));
        if (date.length() != 8){
            System.out.println("文件日期格式不规范,例如:20190527 而不是2019527");
        }
        SimpleDateFormat dfI = new SimpleDateFormat("yyyyMMdd");//输入格式
        SimpleDateFormat dfO = new SimpleDateFormat("yyyy-MM-dd");//目标格式
        String releaseTime = null;
        try {
            releaseTime = dfO.format(dfI.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(((new Date()).getTime()+"").substring(9,13));
        System.out.println(((new Date()).getTime()+""));
        System.out.println(releaseTime);
    }
}
