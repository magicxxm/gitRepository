package com.mushiny.wms.masterdata.obbasics.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateHelper {

    public static final Date beginningOfDay(Date date) {
        if (date == null) {
            date = new Date();
        }

        GregorianCalendar cal;

        cal = new GregorianCalendar();
        cal.setTime(date);
        cal.set(GregorianCalendar.HOUR_OF_DAY, 0);
        cal.set(GregorianCalendar.MINUTE, 0);
        cal.set(GregorianCalendar.SECOND, 0);
        cal.set(GregorianCalendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static final Date endOfDay(Date date) {
        if (date == null) {
            date = new Date();
        }
        GregorianCalendar cal;

        cal = new GregorianCalendar();
        cal.setTime(date);
        cal.set(GregorianCalendar.HOUR_OF_DAY, 23);
        cal.set(GregorianCalendar.MINUTE, 59);
        cal.set(GregorianCalendar.SECOND, 59);
        cal.set(GregorianCalendar.MILLISECOND, 999);
        return cal.getTime();
    }

    public static final Date beginningOfYear(Date date) {
        if (date == null) {
            date = new Date();
        }
        GregorianCalendar cal;

        cal = new GregorianCalendar();
        cal.setTime(date);
        cal.set(GregorianCalendar.HOUR_OF_DAY, 0);
        cal.set(GregorianCalendar.MINUTE, 0);
        cal.set(GregorianCalendar.SECOND, 0);
        cal.set(GregorianCalendar.MILLISECOND, 0);
        cal.set(Calendar.DAY_OF_YEAR, 1);
        return cal.getTime();
    }

    public static final Date beginningOfMonth(Date date) {
        if (date == null) {
            date = new Date();
        }
        GregorianCalendar cal;

        cal = new GregorianCalendar();
        cal.setTime(date);
        cal.set(GregorianCalendar.HOUR_OF_DAY, 0);
        cal.set(GregorianCalendar.MINUTE, 0);
        cal.set(GregorianCalendar.SECOND, 0);
        cal.set(GregorianCalendar.MILLISECOND, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return cal.getTime();
    }


    /**
     * @param created
     * @param lastDateIN
     * @return 0: both dates are on the same day, 1: created is created after
     * lastDateIN, -1: created is created before lastDateIN
     */
    public static final int testSameDay(GregorianCalendar created,
                                        GregorianCalendar lastDateIN) {

        if (created == null || lastDateIN == null) {
            return 0;
        }

        int dayCreated = created.get(GregorianCalendar.DAY_OF_YEAR);
        int dayLastIn = lastDateIN.get(GregorianCalendar.DAY_OF_YEAR);

        int yearCreated = created.get(GregorianCalendar.YEAR);
        int yearLastIn = lastDateIN.get(GregorianCalendar.YEAR);

        if (yearCreated == yearLastIn) {
            if (dayCreated == dayLastIn) {
                return 0;
            } else if (dayCreated > dayLastIn) {
                return 1;
            } else {
                return -1;
            }
        } else if (yearCreated > yearLastIn) {
            return 1;
        } else {
            return -1;
        }
    }

    public static Date parseInputString(String dateStr) {
        if (dateStr == null || dateStr.trim().length() == 0) {
            return null;
        }
        SimpleDateFormat sdf;
        try {
            sdf = new SimpleDateFormat();
            return sdf.parse(dateStr);

        } catch (ParseException e) {
            // Continue with the next format
        }
        try {
            sdf = new SimpleDateFormat("dd.MM.yyyy");
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            // Continue with the next format
        }
        try {
            sdf = new SimpleDateFormat("dd.MM.yy");
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            // Continue with the next format
        }

        try {
            sdf = new SimpleDateFormat("ddMMyy");
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            // Continue with the next format
        }
        try {
            sdf = new SimpleDateFormat("MMyy");
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            // Continue with the next format
        }

        return null;
    }

    public static Date asDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date asDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDate asLocalDate(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalDateTime asLocalDateTime(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}
