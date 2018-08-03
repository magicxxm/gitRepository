package com.mushiny.wms.outboundproblem.business.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * 日期和时间实用类
 * Created by leon.
 */
public class LocalDateTimeUtil {

    /**
     * LocalDate转Date
     */
    public static Date LocalDateTimeToDate(LocalDateTime localDateTime) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        Date date = Date.from(instant);
        return  date;
    }
}
