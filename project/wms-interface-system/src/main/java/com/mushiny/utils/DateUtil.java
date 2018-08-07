package com.mushiny.utils;

import com.mushiny.constants.ChangeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Created by 123 on 2018/2/22.
 */
public class DateUtil {

    private static final Logger logger = LoggerFactory.getLogger(DateUtil.class);

    /*public static void main(String[] ars){
        LocalDateTime before = LocalDateTime.parse("2018-02-28 15:30:00",DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println(updateDeliveryTime(before,"0002",30,1));
    }*/
    /**
     * 采用静态方法将字符串yyyyMMddHHmmss时间转换为LocalDateTime
     * @param dateTime
     * @return
     */
    public static LocalDateTime getLocalDateTime(String dateTime){

        //如果没有传期望发货时间，则给默认发货时间,取当前发货时间 加1小时之后的时间
        if(dateTime == null || "".equalsIgnoreCase(dateTime)){
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime nowPlus = now.plusHours(1);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String neesDate = formatter.format(nowPlus);
            return LocalDateTime.parse(neesDate,formatter);
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");

        Date date = null;
        try {
            date = format.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String ss = format2.format(date);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(ss,formatter);
    }

    /**
     * 调整优先级 改变发货时间点  0002  0普通、1紧急、2加急、默认0
     * 如果是紧急，给当前发货时间点加30分钟，，如果是加急，给当前发货时间点加1小时
     * @param beforeTime
     * @param ztbpri
     * @return
     */
    public static LocalDateTime updateDeliveryTime(LocalDateTime beforeTime,String ztbpri,long jinji,long jiaji){
        LocalDateTime afterTime = null;

        //获取系统当前时间
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String neesDate = formatter.format(now);
        LocalDateTime newTime = LocalDateTime.parse(neesDate,formatter);

        if(ChangeType.PU_TONG.equalsIgnoreCase(ztbpri)){
            return beforeTime;
        }

        if(ChangeType.JIN_JI.equalsIgnoreCase(ztbpri)){
//            afterTime = beforeTime.minusMinutes(jinji);
            afterTime = newTime.plusMinutes(jinji);
        }
        if(ChangeType.JIA_JI.equalsIgnoreCase(ztbpri)){
//            afterTime = beforeTime.minusHours(jiaji);
            afterTime = newTime.plusMinutes(jiaji);
        }

        return afterTime;
    }

    /**
     * 采用静态方法将字符串yyyyMMdd时间转换为LocalDateTime
     * @param dateTime
     * @return
     */
    public static LocalDate toLocalDate(String dateTime){
        if("00000000".equalsIgnoreCase(dateTime)){
            return null;
        }

        if("".equalsIgnoreCase(dateTime) || dateTime == null){
            return null;
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");

        Date date = null;
        try {
            date = format.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
        String ss = format2.format(date);

        return LocalDate.parse(ss);
    }

    public static String toStringDate(String dateTime){
        if("00000000".equalsIgnoreCase(dateTime)){
            return null;
        }

        if("".equalsIgnoreCase(dateTime) || dateTime == null){
            return null;
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");

        Date date = null;
        try {
            date = format.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
        String ss = format2.format(date);

        return ss;
    }

    /**
     * 将字符串yyyy-MM-dd 改为LocalDate格式
     * @param dateTime
     * @return
     */
    public static LocalDate stringToLocalDate(String dateTime){
        if("0000-00-00".equalsIgnoreCase(dateTime)){
            return null;
        }

        /*if("00000000".equalsIgnoreCase(dateTime)){
            return null;
        }*/

        if("".equalsIgnoreCase(dateTime) || dateTime == null){
            return null;
        }

        /*String[] formots = {"yyyyMMdd","yyyy-MM-dd"};
        Date date = null;
        String ss = "";
        for (int i = 0;i < formots.length;i++){

            SimpleDateFormat format = new SimpleDateFormat(formots[i]);
            try {
                date = format.parse(dateTime);
            } catch (ParseException e) {
                logger.error(e.getMessage(),e);
            }
            if(date != null){
                SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
                ss = format2.format(date);
                return LocalDate.parse(ss);
            }
        }
        return null;*/

        return LocalDate.parse(dateTime);
    }

    /**
     * 获取当前时间
     * @return
     */
    public static LocalDate getLotDate() {
        Date date = new Date();
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
        String ss = format2.format(date);

        return LocalDate.parse(ss);
    }

}
