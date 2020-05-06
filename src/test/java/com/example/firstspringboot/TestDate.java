package com.example.firstspringboot;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestDate {
    public static void main(String[] args) {
//        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
//        Date date = new Date(System.currentTimeMillis());
//        System.out.printf(sf.format(date));

//        String dataStr = "17035010475|460070350150146|07372K|20200408114520|";
        String dataStr = "17035010475|460070350150146|||";
        String[] data = dataStr.split("\\|");
        System.out.println(data.length);
        String PolicyId = data[2];
        String startTime = data[3];
        String endTime = data[4];
        Timestamp timestampS = getTimestamp(startTime,"yyyyMMddHHmmss");
        Timestamp timestampE = getTimestamp(endTime,"yyyyMMddHHmmss");
        System.out.println(PolicyId+","+timestampS+","+timestampE);
        Timestamp nowTime = new Timestamp(System.currentTimeMillis());
        boolean b1 = timestampS.before(nowTime);
        boolean b2 = timestampE.after(nowTime);
        System.out.println(b1+","+b2);
//        try {
//            Test();
//            testString();
//        }catch(Exception e){
//            System.out.println("main方法捕捉到异常："+e.getMessage());
//        }


    }
    public static Timestamp getTimestamp(String time, String pattern) {
        DateFormat format = new SimpleDateFormat(pattern);
        format.setLenient(false);
        Timestamp ts = null;

        try {
            ts = new Timestamp(format.parse(time).getTime());
        } catch (ParseException var5) {
            System.out.println(var5.toString()+", "+var5);
        }

        return ts;
    }

    public static void Test() throws Exception{
        try{
            String aa = "1";
            if ("1".equals(aa)){
                System.out.println("抛出异常");
                throw new RuntimeException("出现异常");
            }
        }catch (Exception e){
            System.out.println("捕捉异常信息："+e.getMessage());
        }
    }

    public static void testString(){
        String ss = null;
        StringBuffer sb = new StringBuffer();
        for (int i=0;i<10;i++){
            sb.append("test").append(i).append(",");
        }
        ss = sb.substring(0,sb.length()-1);
        System.out.println(ss);
    }
}
