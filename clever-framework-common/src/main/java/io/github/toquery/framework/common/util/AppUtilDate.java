package io.github.toquery.framework.common.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * @author toquery
 * @version 1
 */
public class AppUtilDate {

    public static Date theDayMin(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static LocalDateTime theTodayMin() {
        return LocalDate.now().atStartOfDay();
    }

    public static LocalDateTime theTodayMax() {
        return LocalDate.now().atTime(LocalTime.MAX);
    }

    public static LocalDateTime date2LocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static Date localDateTime2Date(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }


    public static String getCurrentDateTime() {
        return getCurrentDateTime("yyyy-MM-dd HH:mm:ss");
    }



    public static String getCurrentDateTime(String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return LocalDateTime.now().format(formatter);
    }

    public static String getCurrentDate() {
        return getCurrentDateTime("yyyy-MM-dd");
    }

    public static String getCurrentTime() {
        return getCurrentDateTime("HH:mm:ss");
    }
}
