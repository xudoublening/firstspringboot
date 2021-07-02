package com.example.firstspringboot.xninyUtils;

import io.netty.util.internal.StringUtil;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import org.apache.dubbo.common.utils.StringUtils;
import org.omg.CORBA.SystemException;

/**
 * Describe：
 *
 * @author xnn
 * @createTime 2021/04/01 14:38
 */
public class TestUtil {
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATETIME_FORMAT2 = "yyyyMMdd HH:mm:ss";
    public static final String DATETIME_FORMAT3 = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String YYYYMMDD = "yyyyMMdd";
    public static final String YYYYMM = "yyyyMM";
    public static final String YYMMDD = "yyMMdd";
    public static final String YYYYMMDDHHMM = "yyyyMMddHHmm";
    public static final String YYMMDDHHMM = "yyMMddHHmm";
    public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    public static final String yyyyMMddHHmmssSSS = "yyyyMMddHHmmssSSS";
    
    public TestUtil() {
    }
    
    public static Timestamp getSysDate(){
        return new Timestamp(System.currentTimeMillis());
    }
    
    public static long getCurrentTimeMillis() {
        Timestamp time = new Timestamp(System.currentTimeMillis());
        return time.getTime();
    }
    
    public static String getCurrentTime() {
        return getDateString("yyyy-MM-dd HH:mm:ss");
    }
    
    
    public static String getDateString(String pattern) {
        Timestamp time = new Timestamp(System.currentTimeMillis());
        DateFormat dfmt = new SimpleDateFormat(pattern);
        return dfmt.format(time);
    }
    
    public static String getDateString(Timestamp time, String pattern) {
        DateFormat dfmt = new SimpleDateFormat(pattern);
        return time != null ? dfmt.format(time) : "";
    }
    
    public static String getDateString(Date date, String pattern) {
        SimpleDateFormat sdfmt = new SimpleDateFormat(pattern);
        return date != null ? sdfmt.format(date) : "";
    }
    
    public static boolean isValidDate(String str, String fomat) {
        boolean flag = true;
        
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(fomat);
            sdf.parse(str);
            flag = true;
        } catch (ParseException var4) {
            flag = false;
        }
        
        return flag;
    }
    
    public static Date str2Date(String str) {
        Date date = null;
        if (!StringUtils.isBlank(str)) {
            date = to_date(str, "yyyy-MM-dd");
        }
        
        return date;
    }
    
    public static Timestamp getFutureTime() {
        Date d = str2Timestamp("2100-01-01 00:00:00");
        Timestamp beforeSecond = getBeforeSecond(new Timestamp(d.getTime()));
        return beforeSecond;
    }
    
    public static Date str2Timestamp(String str) {
        Date date = null;
        if (!StringUtils.isBlank(str)) {
            date = to_date(str, "yyyy-MM-dd HH:mm:ss");
        }
        
        return date;
    }
    
    public static Date to_date(String dateStr, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        
        try {
            java.util.Date date = df.parse(dateStr);
            Date d = new Date(date.getTime());
            return d;
        } catch (Exception var5) {
            System.out.println("系统转换日期字符串时出错！");
            return null;
        }
    }
    
    public static Date getDate() {
        String s = getDateString("yyyy-MM-dd HH:mm:ss");
        Date a = str2Date(s);
        return a;
    }
    
    public static Date getTheDayDate(Timestamp sysDate) {
        DateFormat dfmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = dfmt.format(sysDate);
        Date a = str2Date(dateString);
        return a;
    }
    
    public static Date getOffsetDaysDate(Timestamp sysDate, int offsetDays) {
        Timestamp t = getOffsetDaysTime(sysDate, offsetDays);
        Date d = getTheDayDate(t);
        return d;
    }
    
    public static Date getOffsetDaysDate(Date date, int offsetDays) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(5, offsetDays);
        return new Date(calendar.getTimeInMillis());
    }
    
    public static Timestamp getTheDayFirstSecond(Timestamp sysDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sysDate);
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        calendar.add(13, 0);
        return new Timestamp(calendar.getTimeInMillis());
    }
    
    public static Timestamp getTheDayLastSecond(Timestamp sysDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sysDate);
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        calendar.add(13, -1);
        calendar.add(5, 1);
        return new Timestamp(calendar.getTimeInMillis());
    }
    
    public static Timestamp getOffsetDaysTime(Timestamp sysDate, int offsetDays) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sysDate);
        calendar.add(5, offsetDays);
        return new Timestamp(calendar.getTimeInMillis());
    }
    
    public static Timestamp getOffsetMonthsTime(Timestamp sysDate, int offsetDays) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sysDate);
        calendar.add(2, offsetDays);
        return new Timestamp(calendar.getTimeInMillis());
    }
    
    public static Timestamp getOffsetNatureMonthsTime(Timestamp sysDate, int offsetDays) {
        Calendar calendar = Calendar.getInstance();
        Timestamp monthStartTime = getTimeThisMonthFirstSec(sysDate);
        calendar.setTime(monthStartTime);
        calendar.add(2, offsetDays);
        return new Timestamp(calendar.getTimeInMillis());
    }
    
    public static Timestamp getOffsetYearsTime(Timestamp sysDate, int offsetDays) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sysDate);
        calendar.add(1, offsetDays);
        return new Timestamp(calendar.getTimeInMillis());
    }
    
    public static Timestamp getTimeThisMonthLastSec(Timestamp sysDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sysDate);
        calendar.add(2, 1);
        calendar.set(5, 1);
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        calendar.add(13, -1);
        return new Timestamp(calendar.getTimeInMillis());
    }
    
    public static Timestamp getTimeThisMonthFirstSec(Timestamp sysDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sysDate);
        calendar.add(2, 0);
        calendar.set(5, 1);
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        calendar.add(13, 0);
        return new Timestamp(calendar.getTimeInMillis());
    }
    
    public static Timestamp getTimeNextMonthFirstSec(Timestamp sysDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sysDate);
        calendar.add(2, 1);
        calendar.set(5, 1);
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        calendar.add(13, 0);
        return new Timestamp(calendar.getTimeInMillis());
    }
    
    public static int getDaysOfThisMonth() {
        Timestamp currTimestamp = getSysDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currTimestamp);
        return calendar.getActualMaximum(5);
    }
    
    public static String getMonth(String yyyyMM) {
        if (!StringUtil.isNullOrEmpty(yyyyMM) && yyyyMM.length() == 6) {
            String month = yyyyMM.substring(4, 6);
            return month;
        } else {
            System.out.println("格式出错，无法获取月");
            return null;
        }
    }
    
    public static boolean isDateType(String str) {
        String withYYYYMMDDHHSSRegax = "^\\d{4}([1-9]|(1[0-2])|(0[1-9]))([1-9]|([12]\\d)|(3[01])|(0[1-9]))(([0-1][0-9])|([2][0-3]))([0-5][0-9])([0-5][0-9])$";
        String withYYYYMMDDRegax = "^\\d{4}([1-9]|(1[0-2])|(0[1-9]))([1-9]|([12]\\d)|(3[01])|(0[1-9]))$";
        String withYYYYMMRegax = "^\\d{4}((1[0-2])|(0[1-9]))$";
        if (StringUtils.isEmpty(str)) {
            return false;
        } else if (str.length() == 6) {
            return str.matches(withYYYYMMRegax);
        } else if (str.length() == 8) {
            return str.matches(withYYYYMMDDRegax);
        } else {
            return str.length() == 14 ? str.matches(withYYYYMMDDHHSSRegax) : false;
        }
    }
    
    public static long getTimeDifference(Timestamp formatTime1, Timestamp formatTime2) {
        SimpleDateFormat timeformat = new SimpleDateFormat("yyyy-MM-dd,HH:mm:ss");
        long t1 = 0L;
        long t2 = 0L;
        
        try {
            t1 = timeformat.parse(getTimeStampNumberFormat(formatTime1)).getTime();
        } catch (ParseException var12) {
            System.out.println(var12.toString());
        }
        
        try {
            t2 = timeformat.parse(getTimeStampNumberFormat(formatTime2)).getTime();
        } catch (ParseException var11) {
            System.out.println(var11.toString());
        }
        
        long diff = t1 - t2;
        long diffDays = diff / 86400000L;
        return diffDays;
    }
    
    public static int getTimeDifference(String beginDate, String endDate) {
        SimpleDateFormat timeformat = new SimpleDateFormat("yyyyMM");
        Calendar cal1 = new GregorianCalendar();
        GregorianCalendar cal2 = new GregorianCalendar();
        
        try {
            cal1.setTime(timeformat.parse(endDate));
            cal2.setTime(timeformat.parse(beginDate));
        } catch (ParseException var6) {
            System.out.println(var6.toString());
        }
        
        int c = (cal1.get(1) - cal2.get(1)) * 12 + cal1.get(2) - cal2.get(2);
        return c;
    }
    
    public static int getDates() {
        Timestamp currTimestamp = getSysDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currTimestamp);
        return calendar.get(5);
    }
    
    public static int getDaysBetween(String beginDate, String endDate) {
        SimpleDateFormat timeformat = new SimpleDateFormat("yyyyMMdd");
        Calendar cal = Calendar.getInstance();
        long between_days = 0L;
        
        try {
            cal.setTime(timeformat.parse(beginDate));
            long time1 = cal.getTimeInMillis();
            cal.setTime(timeformat.parse(endDate));
            long time2 = cal.getTimeInMillis();
            between_days = (time2 - time1) / 86400000L;
        } catch (ParseException var10) {
            System.out.println(var10.toString());
        }
        
        return Integer.parseInt(String.valueOf(between_days));
    }
    
    public static String getOffsetMonth(String date, int offsetMonth) {
        SimpleDateFormat timeformat = new SimpleDateFormat("yyyyMM");
        GregorianCalendar cal = new GregorianCalendar();
        
        try {
            cal.setTime(timeformat.parse(date));
        } catch (ParseException var5) {
            System.out.println(var5.toString());
        }
        
        cal.add(2, offsetMonth);
        return timeformat.format(cal.getTime());
    }
    
    public static long getMinuteDif(Timestamp formatTime1, Timestamp formatTime2) {
        SimpleDateFormat timeformat = new SimpleDateFormat("yyyy-MM-dd,HH:mm:ss");
        long t1 = 0L;
        long t2 = 0L;
        
        try {
            t1 = timeformat.parse(getTimeStampNumberFormat(formatTime1)).getTime();
        } catch (ParseException var12) {
            System.out.println(var12.toString());
        }
        
        try {
            t2 = timeformat.parse(getTimeStampNumberFormat(formatTime2)).getTime();
        } catch (ParseException var11) {
            System.out.println(var11.toString());
        }
        
        long diff = t1 - t2;
        long diffMins = diff / 60000L;
        return diffMins;
    }
    
    public static String getTimeStampNumberFormat(Timestamp formatTime) {
        SimpleDateFormat m_format = new SimpleDateFormat("yyyy-MM-dd,HH:mm:ss", new Locale("zh", "cn"));
        return m_format.format(formatTime);
    }
    
    public static int getMillis() {
        Timestamp currTimestamp = getSysDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currTimestamp);
        return calendar.get(14);
    }
    
    public static Timestamp getBeforeSecond(Timestamp currentDate) {
        Calendar calender = Calendar.getInstance();
        calender.setTime(currentDate);
        calender.add(13, -1);
        return new Timestamp(calender.getTimeInMillis());
    }
    
    public static Timestamp getAfterSecond(Timestamp currentDate) {
        Calendar calender = Calendar.getInstance();
        calender.setTime(currentDate);
        calender.add(13, 1);
        return new Timestamp(calender.getTimeInMillis());
    }
    
    public static Timestamp getTimestamp(String time) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        format.setLenient(false);
        Timestamp ts = null;
        
        try {
            ts = new Timestamp(format.parse(time).getTime());
        } catch (ParseException var4) {
            System.out.println(var4.toString());
        }
        
        return ts;
    }
    
    public static String getCurYM() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMM");
        Calendar calender = Calendar.getInstance();
        return df.format(calender.getTime());
    }
    
    public static Timestamp getTimestamp(String time, String pattern) {
        DateFormat format = new SimpleDateFormat(pattern);
        format.setLenient(false);
        Timestamp ts = null;
        
        try {
            ts = new Timestamp(format.parse(time).getTime());
        } catch (ParseException var5) {
            System.out.println(var5.toString());
        }
        
        return ts;
    }
    
    public static int getTimeDifferenceMonth(Timestamp formatTime1, Timestamp formatTime2) {
        Calendar calendarTime1 = Calendar.getInstance();
        calendarTime1.setTime(formatTime1);
        int yearTime1 = calendarTime1.get(1);
        int monthTime1 = calendarTime1.get(2);
        int dayTime1 = calendarTime1.get(5);
        Calendar calendarTime2 = Calendar.getInstance();
        calendarTime2.setTime(formatTime2);
        int yearTime2 = calendarTime2.get(1);
        int monthTime2 = calendarTime2.get(2);
        int dayTime2 = calendarTime2.get(5);
        int y = yearTime2 - yearTime1;
        int m = monthTime2 - monthTime1;
        int d = dayTime2 - dayTime1;
        return d > 0 ? y * 12 + m + 1 : y * 12 + m;
    }
    
    public static String trans2CnTime(Timestamp time) {
        DateFormat df = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
        return df.format(time);
    }
    
    public static String trans2CnTimestamp(Timestamp time) {
        DateFormat df = new SimpleDateFormat("MM月dd日 HH时mm分");
        return df.format(time);
    }
    
    public static String trans2CnDate(Timestamp time) {
        DateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
        return df.format(time);
    }
    
    public static Timestamp getTimeNextDay(Timestamp date) {
        long time = date.getTime();
        time += 86400000L;
        return new Timestamp(time);
    }
    
    public static long getSecondsFromDays(long days) {
        return 86400L * days;
    }
    
    public static Timestamp getTimeBeforeDay(Timestamp date) {
        long time = date.getTime();
        time -= 86400000L;
        return new Timestamp(time);
    }
    
    public static Timestamp getBeforeMonth(Timestamp currentDate) {
        Calendar calender = Calendar.getInstance();
        calender.setTime(currentDate);
        calender.add(2, -1);
        return new Timestamp(calender.getTimeInMillis());
    }
    
    public static Timestamp getTimeLastMonthLastSec(Timestamp sysDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(sysDate);
        cal.add(2, -1);
        int MaxDay = cal.getActualMaximum(5);
        cal.set(cal.get(1), cal.get(2), MaxDay, 23, 59, 59);
        return new Timestamp(cal.getTimeInMillis());
    }
    
    public static long getTimeDifferenceSecond(Timestamp formatTime1, Timestamp formatTime2) {
        SimpleDateFormat timeformat = new SimpleDateFormat("yyyy-MM-dd,HH:mm:ss");
        long t1 = 0L;
        long t2 = 0L;
        
        try {
            t1 = timeformat.parse(getTimeStampNumberFormat(formatTime1)).getTime();
        } catch (ParseException var12) {
            System.out.println(var12.toString());
        }
        
        try {
            t2 = timeformat.parse(getTimeStampNumberFormat(formatTime2)).getTime();
        } catch (ParseException var11) {
            System.out.println(var11.toString());
        }
        
        long diff = t2 - t1;
        long diffSeconds = diff / 1000L;
        return diffSeconds;
    }
    
    public static Timestamp getOffsetMinutesTime(Timestamp sysDate, int offsetMinutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sysDate);
        calendar.add(12, offsetMinutes);
        return new Timestamp(calendar.getTimeInMillis());
    }
    
    public static void main(String[] args) {
        
        // String token = "partner=2088911130951183&req_id=20210408160115&res_error=%3C%3Fxml+version%3D%221.0%22+encoding%3D%22utf-8%22%3F%3E%3Cerr%3E%3Ccode%3E0000%3C%2Fcode%3E%3Csub_code%3E0000%3C%2Fsub_code%3E%3Cmsg%3Esystem+exception%3C%2Fmsg%3E%3Cdetail%3E%E7%B3%BB%E7%BB%9F%E5%BC%82%E5%B8%B8%3C%2Fdetail%3E%3C%2Ferr%3E&sec_id=MD5&service=alipay.wap.trade.create.direct&v=2.0";
        String token = "res_data=%3C%3Fxml+version%3D%221.0%22+encoding%3D%22utf-8%22%3F%3E%3Cdirect_trade_create_res%3E%3Crequest_token%3EUTP5b94db4a3e6d4029889e15ec059f4688%2400%3C%2Frequest_token%3E%3C%2Fdirect_trade_create_res%3E&service=alipay.wap.trade.create.direct&sec_id=MD5&partner=2088911130951183&req_id=20210406182958&sign=4b6c38d8977108a14f2afb5baf727d3f&v=2.0";
    
        try {
            System.out.println(URLDecoder.decode(token,"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        
        /*Timestamp timestamp = getSysDate();
        System.out.println(timestamp);
        timestamp = getOffsetNatureMonthsTime(timestamp,1);
        timestamp = getOffsetMinutesTime(timestamp,60);
        System.out.println(getDateString(timestamp,DATETIME_FORMAT));*/
    }
}
