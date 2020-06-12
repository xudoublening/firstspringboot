package com.example.firstspringboot;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @program: firstspringboot
 * @description:
 * @author: XNN
 * @create: 2020-06-08 16:16
 **/
public class TemplateStringTest {

    public static void main(String[] args) throws Exception {

        String column = "{\n" +
                "    \"touser\": \"oU8Ki0o6x2U6qczi0kf7wcR18rJ8\", \n" +
                "    \"template_id\": \"OdLIzT0zn7MNwbvdPe1S8J21N4agnXLvIBhcZ7Mmx5w\", \n" +
                "    \"url\": \"http://weixin.qq.com\", \n" +
                "    \"data\": {\n" +
                "        \"first\": {\n" +
                "            \"value\": \"${NOW_TIME}\", \n" +
                "            \"color\": \"#173177\"\n" +
                "        }, \n" +
                "        \"keyword1\": {\n" +
                "            \"value\": \"17809092345\", \n" +
                "            \"color\": \"#173177\"\n" +
                "        }, \n" +
                "        \"keyword2\": {\n" +
                "            \"value\": \"2014年9月22日\", \n" +
                "            \"color\": \"#173177\"\n" +
                "        }, \n" +
                "        \"keyword3\": {\n" +
                "            \"value\": \"39.8元\", \n" +
                "            \"color\": \"#173177\"\n" +
                "        }, \n" +
                "        \"keyword4\": {\n" +
                "            \"value\": \"39.8元\", \n" +
                "            \"color\": \"#173177\"\n" +
                "        }, \n" +
                "        \"remark\": {\n" +
                "            \"value\": \"${PRODUCT_NAME}\", \n" +
                "            \"color\": \"#173177\"\n" +
                "        }\n" +
                "    }\n" +
                "}";

        String paramVar = "${NOW_TIME}:2020060509 ^${PRODUCT_NAME}:华翔畅游30元流量包used0.00 M,over1024.00 M.";

        JSONObject columnJson = JSONObject.parseObject(column);
        JSONObject data = columnJson.getJSONObject("data");
        try {
            data = generatorData(data,paramVar);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("end===>>> data: " + data);
        /*test1();
        System.out.println(IDCardValidate("188450192105012260"));*/
    }

    public static boolean IDCardValidate(String IDStr) throws ParseException {
        IDStr = IDStr.trim();
        String errorInfo = "";// 记录错误信息
        String[] ValCodeArr = { "1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2" };
        String[] Wi = { "7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7", "9", "10", "5", "8", "4", "2" };
        String Ai = "";
        // ================ 号码的长度 15位或18位 ================
        if (IDStr.length() != 15 && IDStr.length() != 18) {
            //身份证号码长度应该为15位或18位
            System.out.println("身份证号码长度应该为15位或18位");
            return false;
        }
        // =======================(end)========================

        // ================ 数字 除最后以为都为数字 ================
        if (IDStr.length() == 18) {
            Ai = IDStr.substring(0, 17);
        } else if (IDStr.length() == 15) {
            Ai = IDStr.substring(0, 6) + "19" + IDStr.substring(6, 15);
        }
        if (isNumeric(Ai) == false) {
            //身份证15位号码都应为数字 ; 18位号码除最后一位外，都应为数字。
            System.out.println("身份证15位号码都应为数字 ; 18位号码除最后一位外，都应为数字");
            return false;
        }
        // =======================(end)========================

        // ================ 出生年月是否有效 ================
        String strYear = Ai.substring(6, 10);// 年份
        String strMonth = Ai.substring(10, 12);// 月份
        String strDay = Ai.substring(12, 14);// 月份
        if (isDataFormat(strYear + "-" + strMonth + "-" + strDay) == false) {
            //身份证生日无效。
            System.out.println("身份证生日无效");
            return false;
        }
        GregorianCalendar gc = new GregorianCalendar();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150
                || (gc.getTime().getTime() - s.parse(strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {
            //身份证生日不在有效范围。
            System.out.println("身份证生日不在有效范围");
            return false;
        }
        if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
            //身份证月份无效
            System.out.println("身份证月份无效");
            return false;
        }
        if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
            //身份证日期无效
            System.out.println("身份证日期无效");
            return false;
        }
        // =====================(end)=====================

        // ================ 地区码时候有效 ================
        Hashtable h = GetAreaCode();
        System.out.println(Ai.substring(0, 2));
        if (h.get(Ai.substring(0, 2)) == null) {
            //身份证地区编码错误。
            System.out.println("身份证地区编码错误");
            return false;
        }
        // ==============================================

        // ================ 判断最后一位的值 ================
        int TotalmulAiWi = 0;
        for (int i = 0; i < 17; i++) {
            TotalmulAiWi = TotalmulAiWi + Integer.parseInt(String.valueOf(Ai.charAt(i))) * Integer.parseInt(Wi[i]);
        }
        int modValue = TotalmulAiWi % 11;
        String strVerifyCode = ValCodeArr[modValue];
        Ai = Ai + strVerifyCode;

        if (IDStr.length() == 18) {
            if (Ai.equals(IDStr) == false) {
                //身份证无效，不是合法的身份证号码
                System.out.println("身份证无效，不是合法的身份证号码");
                return false;
            }
        } else {
            return true;
        }
        // =====================(end)=====================
        return true;
    }
    /**
     * 功能：设置地区编码
     */
    private static Hashtable GetAreaCode() {
        Hashtable hashtable = new Hashtable();
        hashtable.put("11", "北京");
        hashtable.put("12", "天津");
        hashtable.put("13", "河北");
        hashtable.put("14", "山西");
        hashtable.put("15", "内蒙古");
        hashtable.put("21", "辽宁");
        hashtable.put("22", "吉林");
        hashtable.put("23", "黑龙江");
        hashtable.put("31", "上海");
        hashtable.put("32", "江苏");
        hashtable.put("33", "浙江");
        hashtable.put("34", "安徽");
        hashtable.put("35", "福建");
        hashtable.put("36", "江西");
        hashtable.put("37", "山东");
        hashtable.put("41", "河南");
        hashtable.put("42", "湖北");
        hashtable.put("43", "湖南");
        hashtable.put("44", "广东");
        hashtable.put("45", "广西");
        hashtable.put("46", "海南");
        hashtable.put("50", "重庆");
        hashtable.put("51", "四川");
        hashtable.put("52", "贵州");
        hashtable.put("53", "云南");
        hashtable.put("54", "西藏");
        hashtable.put("61", "陕西");
        hashtable.put("62", "甘肃");
        hashtable.put("63", "青海");
        hashtable.put("64", "宁夏");
        hashtable.put("65", "新疆");
        hashtable.put("71", "台湾");
        hashtable.put("81", "香港");
        hashtable.put("82", "澳门");
        hashtable.put("91", "国外");
        return hashtable;
    }

    /**
     * 验证日期字符串是否是YYYY-MM-DD格式
     */
    public static boolean isDataFormat(String str) {
        boolean flag = false;
        // String
        // regxStr="[1-9][0-9]{3}-[0-1][0-2]-((0[1-9])|([12][0-9])|(3[01]))";
        String regxStr = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$";
        Pattern pattern1 = Pattern.compile(regxStr);
        Matcher isNo = pattern1.matcher(str);
        if (isNo.matches()) {
            flag = true;
        }
        return flag;
    }

    /**
     * 功能：判断字符串是否为数字
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (isNum.matches()) {
            return true;
        } else {
            return false;
        }
    }

    private static void test1(){
        StringBuffer sql = new StringBuffer("INSERT INTO his_wechat_inert_ (unique_id, remind_type, src_name, servicetype, template_id, verifyid, phone, gsmcontent, flag, create_time, Priority, bak_time)");
        sql.append(" VALUES(").append("null").append(",").append("null").append(",");
        sql.append("null").append(",").append("null").append(",");
        sql.append("null").append(",").append("null").append(",");
        sql.append("null").append(",").append("null").append(",");
        sql.append("null").append(",").append("null").append(",");
        sql.append("null").append(",").append("null").append(");");

        System.out.println(sql.toString());
    }

    private static JSONObject generatorData(JSONObject templateData, String paramVar) throws Exception{

        if (templateData.isEmpty()){
            System.out.println("消息模板占位符填充-模板为空");
            return null;
        }

        if (paramVar.isEmpty()){
            System.out.println("消息模板占位符填充-参数为空");
            return null;
        }

        String[] params = paramVar.split("\\^");
        /** 参数 key-value **/
        Map<String,String> paramMap = new HashMap<>();

        for (int i=0;i<params.length;i++) {
            String[] thisParam = params[i].split(":");
            if (thisParam.length != 2){
                System.out.println("当前["+params[i]+"]格式不正确！");
                return null;
            }
            paramMap.put(thisParam[0],thisParam[1]);
        }

        /** 消息模板占位符填充 **/
        String str = templateData.toJSONString();

        for(Map.Entry<String,String> entrySet : paramMap.entrySet()){
            System.out.println("key:"+entrySet.getKey()+"==value:"+entrySet.getValue());
            System.out.println(str.contains(entrySet.getKey()));
            if (str.contains(entrySet.getKey())){
                str = str.replace(entrySet.getKey(),entrySet.getValue());
            }
        }
        return JSONObject.parseObject(str);
    }
}
