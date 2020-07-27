package com.example.firstspringboot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class TestString {

    private static String aa = "\n";

    public static void main(String[] args) {

        long aa = 456787699L;
        System.out.println(String.format("%06d", aa));
        /*String aa = "1";
        String bb = "11";
        bb = aa==null?""+"111":"aa"+"111";
        System.out.println("["+bb+"]");*/
        /*StringBuffer stringBuffer = new StringBuffer();
        String s = "<jisjdi>";
        System.out.println(stringBuffer.append(s + aa));

        test2(null,null," s_flag=:flag ORDER BY creat_time asc",0,100);*/

    }

    private static void test2(String aCustomerSQL, String[] aCols, String aCond, int aStartNum, int aEndNum){
        StringBuilder buffer = new StringBuilder();
        int offset;
        if (aCond != null && !aCond.trim().equalsIgnoreCase("")) {
            buffer.append(" select ");
            if (aCols != null && aCols.length > 0) {
                for(offset = 0; offset < aCols.length; ++offset) {
                    if (offset > 0) {
                        buffer.append(",");
                    }

                    buffer.append(aCols[offset]);
                }
            } else {
                buffer.append(" * ");
            }

            buffer.append(" from (");
            buffer.append(aCustomerSQL);
            buffer.append(" ) __MS");
            if (aCond != null && !aCond.trim().equalsIgnoreCase("")) {
                String t = aCond.trim().toLowerCase();
                if (!t.startsWith("order") && !t.startsWith("group")) {
                    buffer.append(" where ").append(aCond);
                } else {
                    t = t.substring(5).trim();
                    if (t.startsWith("by")) {
                        buffer.append(aCond);
                    } else {
                        buffer.append(" where ").append(aCond);
                    }
                }
            }
        } else {
            buffer.append(aCustomerSQL);
        }

        if (aStartNum >= 0 || aEndNum >= 0) {
            if (aStartNum < 0 && aEndNum >= 0) {
                buffer.append(" limit ").append("0").append(",").append(aEndNum);
            } else if (aEndNum < 0 && aStartNum >= 0) {
                if (aStartNum > 0) {
                    buffer.append(" limit ").append(aStartNum - 1).append(",").append("18446744073709551615");
                } else {
                    buffer.append(" limit ").append(aStartNum).append(",").append("18446744073709551615");
                }
            } else if (aEndNum < aStartNum) {
                buffer.append(" limit ").append("0").append(",").append("0");
            } else {
                offset = aEndNum - aStartNum + 1;
                int tmpStart;
                if (aStartNum > 0) {
                    tmpStart = aStartNum - 1;
                } else {
                    tmpStart = 0;
                }

                buffer.append(" limit ").append(tmpStart).append(",").append(offset);
            }
        }
        System.out.println(buffer.toString());
    }

    private static void test1(){
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
