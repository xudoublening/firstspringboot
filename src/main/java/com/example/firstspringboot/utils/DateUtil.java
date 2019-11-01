package com.example.firstspringboot.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateUtil {
    /**
     * 获取当前日期和时间的中文显示
     * @return
     */
    public static String nowCn(){
        return new SimpleDateFormat("yyyy年MM月dd日 HH时:mm分:ss秒").format(new Date());
    }
    /**
     * 获取当前日期和时间的英文显示
     * @return
     */
    public static String nowEn(){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    public static String nowEns(){
        return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    }
    /**
     * 获取当前日期的中文显示
     * @return
     */
    public static String nowDateCn(){
        return new SimpleDateFormat("yyyy年MM月dd日").format(new Date());
    }
    /**
     * 获取当前日期的英文显示
     * @return
     */
    public static String nowDateEn(){
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    public static String yesterDateEn(){
        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        return new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
    }
    public static String yesterDateEn2(){
        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        return new SimpleDateFormat("yyyyMMdd").format(calendar.getTime());
    }
    /**
     * 获取当前日期的yyyyMMdd显示
     * @return
     */
    public static String nowDateyyyyMMdd(){
        return new SimpleDateFormat("yyyyMMdd").format(new Date());
    }
    /**
     * 获取当前日期的yyyyMM显示
     * @return
     */
    public static String nowDateyyyyMM(){
        return new SimpleDateFormat("yyyyMM").format(new Date());
    }
    /**
     * 获取当前时间的中文显示
     * @return
     */
    public static String nowTimeCn(){
        return new SimpleDateFormat("HH时:mm分:ss秒").format(new Date());
    }
    /**
     * 获取当前时间的英文显示
     * @return
     */
    public static String nowTimeEn(){
        return new SimpleDateFormat("HH:mm:ss").format(new Date());
    }
    /**
     * 获取当前时间的英文显示
     * @return
     */
    public static String getNowDate(){
        return new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
    }
    /**
     * 获取某年某月第一天日期
     * @param year 年
     * @param month 月份
     * @param format 显示格式
     * @return
     * @author liangmeng
     */
    public static String getCurrMonthFirst(int year ,int month,String format){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR,  year);
        cal.set(Calendar.MONTH, month-1);
        cal.set(Calendar.DAY_OF_MONTH, cal.getMinimum(Calendar.DATE));
        return new SimpleDateFormat(format).format(cal.getTime());
    }

    /**
     * 获取某年某月最后一天日期
     * @param year 年
     * @param month 月份
     * @param format 显示格式
     * @return
     * @author liangmeng
     */
    public static String getCurrMonthLast(int year,int month,String format){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month-1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        int value = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.DAY_OF_MONTH, value);
        return new SimpleDateFormat(format).format(cal.getTime());
    }
    /**
     * 获取当前月向前X个月的list
     * @param count 向前推的月份数量
     * @return
     * @author liangmeng
     */
    public static List<Map<String,Object>> getPerMonth(int count){
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        for(int i=0;i<count;i++){
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, -i);
            Map<String,Object> map = new HashMap<String, Object>();
            int year = cal.get(Calendar.YEAR);
            int month = (cal.get(Calendar.MONTH)+1);
            map.put("formatStr",  year+ "年" + month+"月");
            map.put("year", year);
            map.put("month", month);
            map.put("yyyyMM", String.valueOf(year) + (month<10? "0"+month : month));
            list.add(map);
        }
        return list;
    }
    /**
     * 获取当前月向前X个月的list
     * @param count 向前推的月份数量
     * @return
     * @author liangmeng
     */
    public static List<Map<String,Object>> getMonthFromPerMonth(int count){
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        for(int i=1;i<count+1;i++){
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, -i);
            Map<String,Object> map = new HashMap<String, Object>();
            int year = cal.get(Calendar.YEAR);
            int month = (cal.get(Calendar.MONTH)+1);
            map.put("formatStr",  year+ "年" + month+"月");
            map.put("year", year);
            map.put("month", month);
            map.put("yyyyMM", String.valueOf(year) + (month<10? "0"+month : month));
            list.add(map);
        }
        return list;
    }
    /**
     * 获取当前毫秒数
     */
    public static long getNowDateTime() {
        return (new Date()).getTime();
    }

    public static String timstamp2String(Timestamp timestamp){
        if(timestamp==null)
            return "";
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//定义格式，不显示毫秒
        String str = df.format(timestamp);
        if(str!=null && str.length()>10)
            str = str.substring(0, 10);
        return str;
    }

    public static String timstamp3String(Timestamp timestamp){
        if(timestamp==null)
            return "";
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//定义格式，不显示毫秒
        String str = df.format(timestamp);
        if(str!=null && str.length()>10)
            str = str.substring(0, 10);
        return str;
    }

    public static Timestamp string2Datestamp(String timestamp) throws Exception{
        if(timestamp==null||timestamp.length()<10)
            return null;
        Timestamp ts = Timestamp.valueOf(timestamp.trim()+" 00:00:00");
        return ts;
    }

    public static Timestamp string2Timstamp(String timestamp) throws Exception{
        Timestamp ts = Timestamp.valueOf(timestamp);
        return ts;
    }

    public static Timestamp sysTimestamp() throws Exception{
        Timestamp ts = new Timestamp(new Date().getTime());
        return ts;
    }

    public static long getNextMonthStartTime(){
        Calendar cal = Calendar.getInstance ();
        int month = cal.get(Calendar. MONTH);
        int year = cal.get(Calendar. YEAR);
        if(month==11){
            year+=1;
            month=0;
        }else{
            month+=1;
        }
        cal.set(Calendar.YEAR,  year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, cal.getMinimum(Calendar.DATE));
        return cal.getTime().getTime();
    }
    public static String timstamp2Dt(Timestamp timestamp){
        if(timestamp==null)
            return "";
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//定义格式，不显示毫秒
        String str = df.format(timestamp);
        return str;
    }

    /**
     * 格式化日期串
     * @param timestamp
     * @return
     */
    public static String getFormatDate(Date date, String formatStr){
        if(formatStr==null || null==date){
            return "";
        }else{
            return new SimpleDateFormat(formatStr).format(date);
        }
    }
    /**
     * 当前月份
     */
    public static String getCurrentDateMonth_(){
        return getFormatDate(new Date(),"yyyy-MM");
    }
    /**
     * 当前月份
     */
    public static String getCurrentDateMonth(){
        return getFormatDate(new Date(),"yyyyMM");
    }

    /**
     * 获取当月的首日和末日  例如：
     * 2014-09-01  2014-09-30

     * @param args
     */
    public static String getMothFday() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat datef=new SimpleDateFormat("yyyy-MM-dd");
        //当前月的第一天
        cal.set(GregorianCalendar.DAY_OF_MONTH, 1);
        Date beginTime=cal.getTime();
        return     datef.format(beginTime);
    }

    public static String getMothLday(){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat datef=new SimpleDateFormat("yyyy-MM-dd");
        //当前月的最后一天
        cal.set( Calendar.DATE, 1 );
        cal.roll(Calendar.DATE, - 1 );
        Date endTime=cal.getTime();
        return     datef.format(endTime);
    }

    /**
     * 传入时间戳  返回YYYYMM
     * @param timestamp
     * @return
     * @author yangzh
     */
    public static String timstamp4String(Timestamp timestamp){
        if(timestamp==null)
            return "";
        SimpleDateFormat df = new SimpleDateFormat("yyyyMM");//定义格式，不显示毫秒
        String str = df.format(timestamp);
        if(str!=null && str.length()>10)
            str = str.substring(0, 10);
        return str;
    }
}
