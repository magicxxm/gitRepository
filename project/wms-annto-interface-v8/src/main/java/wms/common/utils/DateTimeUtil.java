package wms.common.utils;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * 日期和时间实用类
 * Created by fengping.
 */
public class DateTimeUtil {

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
}
