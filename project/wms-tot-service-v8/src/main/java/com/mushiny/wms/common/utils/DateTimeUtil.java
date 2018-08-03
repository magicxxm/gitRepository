package com.mushiny.wms.common.utils;

import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.*;
/**
 * 日期和时间实用类
 * Created by fengping.
 */
public class DateTimeUtil {

    private static TimeZone timeZone=TimeZone.getTimeZone("GMT+8");
    private static final String custumFormat="yyyy-MM-dd HH:mm:ss";
    private static final String dayFormat="yyyy-MM-dd";
    private static final String monthFormat="yyyy-MM";
    /**
     * 采用静态方式获取时间差
     */
    public static String getTimeString(ZonedDateTime time1, ZonedDateTime time2) {
        Duration duration = Duration.between(time1, time2);
        long longSeconds = Math.abs(duration.getSeconds());
        int hours = (int) longSeconds / 3600;
        int remainder = (int) longSeconds - hours * 3600;
        int minutes = remainder / 60;
        int seconds = remainder - minutes * 60;
        return String.format("%02d", hours) +
                ":" +
                String.format("%02d", minutes) +
                ":" +
                String.format("%02d", seconds);
    }

    /**
     * 采用静态方式获取当前系统时间
     */
    public static ZonedDateTime getNowDateTime() {
        return ZonedDateTime.now();
    }


    // 获取当前时间所在月的周数
    public static String getWeekOf(String date) {
        Date temp=parseDate(date,dayFormat);
        StringBuilder sb=new StringBuilder(getDateFormat(temp,"yyyy年M月"));
        Calendar c = new GregorianCalendar( );
        c.setTimeZone(timeZone);
        c.setTime(temp);
        int week=c.get(Calendar.WEEK_OF_MONTH);
        sb.append("第").append(week).append("周");
        return sb.toString();
    }
    // 获取当前时间所在周的开始日期
    public static String getFirstDayOfWeek(String dateTime) {
        Date date=parseDate(dateTime,dayFormat);
        Date result=null;
        if(date!=null)
        {
            Calendar c = new GregorianCalendar();
            c.setTimeZone(timeZone);
            c.setFirstDayOfWeek(Calendar.SUNDAY);
            c.setTime(date);
            c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday
            c.set(Calendar.HOUR, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
            result=c.getTime();
        }

        return getDateFormat(result,custumFormat);
    }
    // 获取当前时间所在月的开始日期
    public static String getFirstDayOfMonth(String dateTime) {
        Date date=parseDate(dateTime,monthFormat);
        Date result=null;
        if(date!=null)
        {
            Calendar c = new GregorianCalendar();
            c.setTimeZone(timeZone);
            c.setTime(date);

            c.add(Calendar.MONTH, 0);
            c.set(Calendar.DAY_OF_MONTH,1);

            c.set(Calendar.HOUR, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
            result=c.getTime();

        }

        return getDateFormat(result,custumFormat);
    }
    // 获取当前时间所在月的结束日期
    public static String getLastDayOfMonth(String dateTime) {
        Date date=parseDate(dateTime,monthFormat);
        Date result=null;
        if(date!=null)
        {
            Calendar c = new GregorianCalendar();
            c.setTimeZone(timeZone);
            c.setTime(date);
            c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DATE));
            c.set(Calendar.HOUR, 23);
            c.set(Calendar.MINUTE, 59);
            c.set(Calendar.SECOND, 59);
            c.set(Calendar.MILLISECOND, 999);
            result=c.getTime();
        }

        return getDateFormat(result,custumFormat);
    }

    // 获取当前时间所在周的结束日期
    public static String getLastDayOfWeek(String dateTime) {
        Date date=parseDate(dateTime,dayFormat);
        Date result=null;
        if(date!=null)
        {
            Calendar c = new GregorianCalendar();

            c.setTimeZone(timeZone);
            c.setFirstDayOfWeek(Calendar.SUNDAY);
            c.setTime(date);
            c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6); // Sunday
            c.set(Calendar.HOUR, 23);
            c.set(Calendar.MINUTE, 59);
            c.set(Calendar.SECOND, 59);
            c.set(Calendar.MILLISECOND, 999);
            result=c.getTime();
        }

        return getDateFormat(result,custumFormat);

    }
    public static String getFirstTimeOfDay(String dateTime) {
        Date date=parseDate(dateTime,dayFormat);
        Date result=null;
        if(date!=null)
        {
            Calendar c = new GregorianCalendar();
            c.setTimeZone(timeZone);
            c.setTime(date);
            c.set(Calendar.HOUR, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
            result=c.getTime();
        }

        return getDateFormat(result,custumFormat);
    }
    public static String getLastTimeOfDay(String dateTime) {
        Date date=parseDate(dateTime,dayFormat);
        Date result=null;
        if(date!=null)
        {
            Calendar c = new GregorianCalendar();
            c.setTimeZone(timeZone);
            c.setTime(date);
            c.set(Calendar.HOUR, 23);
            c.set(Calendar.MINUTE, 59);
            c.set(Calendar.SECOND, 59);
            c.set(Calendar.MILLISECOND, 999);
            result=c.getTime();
        }

        return getDateFormat(result,custumFormat);
    }


    public static LocalDate getNowDate() {
        return LocalDate.now();
    }

    /**
     * 采用静态方式通过日期格式获取当前系统时间DateFormat模式
     */
    public static String getNowFormat() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(getNowDateTime());
    }

    /**
     * 采用静态方式通过日期格式获取当前系统时间字符串模式
     */
    public static String getNowFormat(String format) {
        return new SimpleDateFormat(format).format(getNowDateTime());
    }

    /**
     * 采用静态方式通过日期格式获取时间字符串模式
     */
    public static String getDateFormat(Date date, String format) {
        String result="";
        if(date!=null)
        {
            result=new SimpleDateFormat(format).format(date);
        }
        return result;
    }

    public static Date parseDate(String date, String format) {

       Date result=null;
       if(!StringUtils.isEmpty(date))
        try {
            return new SimpleDateFormat(format).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }
    public static Date  getDateforStr(String dateStr) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.parse(dateStr);
    }

    public static Date  getDateFirst(String dateStr) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String newStr = dateStr.substring(0,10)+" 00:00:00";
        return format.parse(newStr);
    }

    public static Date  getDateLast(String dateStr) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String newStr = dateStr.substring(0,10)+" 23:59:59";
        return format.parse(newStr);
    }


    public static  double  getHourAndMin(Date endDate, Date nowDate,String type){
        long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
        long nh = 1000 * 60 * 60;// 一小时的毫秒数
        long nm = 60 * 1000;//一分钟的毫秒数
        double day = 0;
        double hour = 0;
        double min = 0;
        long diff = 0;

        // 获得两个时间的毫秒时间差异
        diff = endDate.getTime() - nowDate.getTime();
        if (endDate.toString().contains("23:59:59")) diff +=1000;//全天时间算加一秒刚好24小时
        day = (double)diff / nd ;// 计算差多少天
        hour = (double)diff / nh;
        min = (double)diff / nm;

        if("hour".equals(type)){
            return hour;
        }else if("min".equals(type)){
            return min;
        }
        return  0;
    }

    /**
     * 保留小数点后位数
     * @param oldDecimal 原double数
     * @param digit 保留位数
     * @return 新double
     */
    public static  double  getNewDecimal(double oldDecimal, int digit){
        double newDecimal = new BigDecimal(oldDecimal).setScale(digit, BigDecimal.ROUND_HALF_UP).doubleValue();
        return newDecimal;
    }

    /**
     * 保留小数点后位数
     * @param oldDecimal 原double数(String)
     * @param digit 保留位数
     * @return 新double
     */
    public static  double  getNewDecimal(String oldDecimal, int digit){
        double newDecimal = new BigDecimal(oldDecimal).setScale(digit, BigDecimal.ROUND_HALF_UP).doubleValue();
        return newDecimal;
    }

//    public static void main(String[] args) {
     /*  SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat format3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
       String dateString="2017-7-1";
        String monthString="2017-6";
       Date date=null;
       Date date2=null;
        try {
            date=format.parse(dateString);
            date2=format2.parse(monthString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(monthString+"有"+getMothDays(monthString)+"天");
        System.out.println(monthString+"第一天"+format3.format(getFirstDayOfMonth(date2)));
        System.out.println(monthString+"最后一天"+format3.format(getLastDayOfMonth(date2)));
        System.out.println(dateString+"第"+getWeekOf(date)+"周");
        System.out.println(dateString+"第"+getWeekOf(date)+"周" +"开始时间"+format3.format(getFirstDayOfWeek(date)));
        System.out.println(dateString+"第"+getWeekOf(date)+"周" +"结束时间"+format3.format(getLastDayOfWeek(date)));*/

//    }
//    public static void main(String[] args) {
//
//        String start = "2017-06-26 09:11:07.0";
//        String end = "2017-06-26 10:51:27.0";
//        Date startDate = null;
//        Date endDate =null;
//        try {
//            startDate = getDateforStr(start);
//            endDate = getDateforStr(end);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        double hour = getHourAndMin(endDate,startDate,"h");
//        double min = getHourAndMin(endDate,startDate,"m");
//
//        System.out.println("======="+hour+"小时"+min+"分钟");
//    }

}
