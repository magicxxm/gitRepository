package com.mushiny.wms.common.utils;

import com.mushiny.wms.application.service.impl.OutboundInstructServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Date;

/**
 * 日期和时间实用类
 * Created by fengping.
 */
public class DateTimeUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(DateTimeUtil.class);
    private static String defaultFormat="yyyy-MM-dd HH:mm:ss";
    private static String defaultFormat2="yyyy/MM/dd HH:mm:ss";
    /**
     * 采用静态方式获取时间差
     */
    public static String getTimeString(LocalDateTime time1, LocalDateTime time2) {
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
        return new SimpleDateFormat(format).format(date);
    }

    public static Date strToDateLong(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat(defaultFormat);
        Date strtodate = null;
        try {
            strtodate = formatter.parse(strDate);
        } catch (ParseException e) {
            LOGGER.error(e.getMessage(),e);
            formatter=new SimpleDateFormat(defaultFormat2);
            try {
                strtodate = formatter.parse(strDate);
            } catch (ParseException e1) {
                LOGGER.error(e1.getMessage(),e);
            }

        }
        return strtodate;
     }
    public static Date strToDateLong(String strDate,String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Date strtodate = null;
        try {
            strtodate = formatter.parse(strDate);
        } catch (ParseException e) {
            LOGGER.error(e.getMessage(),e);
        }
        return strtodate;
    }


    public static Timestamp strToTimeStamp(String strDate) {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        try {
            ts = Timestamp.valueOf(strDate);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
        }
        return ts;
    }

    //String 类型时间转为LocalDateTime
    public static LocalDateTime getLocalDatetime(String date) {
        date = date.replaceAll("[a-zA-Z]", " ");
        Date d = null;
        LocalDateTime localDateTime = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (!date.isEmpty()) {
            try {
                d = simpleDateFormat.parse(date.trim().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Instant instant = d.toInstant();
            localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        }
        return localDateTime;
    }

}
