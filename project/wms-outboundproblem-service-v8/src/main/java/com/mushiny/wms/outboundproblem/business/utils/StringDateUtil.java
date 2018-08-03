package com.mushiny.wms.outboundproblem.business.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

//String类型时间转化为LocalDateTime
public class StringDateUtil {
    public static LocalDateTime getLocaDatetimeByString(String date){
        date=date.replaceAll("[a-zA-Z]"," ");
        Date d=null;
        LocalDateTime localDateTime=null;
        SimpleDateFormat  simpleDateFormat =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(!date.isEmpty()) {
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

    public static String getNowFormat() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

}
