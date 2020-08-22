package io.github.toquery.framework.common.util;

import io.github.toquery.framework.common.constant.AppCommonConstant;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author toquery
 * @version 1
 */
public class DateTimeUtils {


    /**
     * 获取当前开始时间
     */
    public static LocalDateTime theTodayMin() {
        return LocalDate.now().atStartOfDay();
    }

    /**
     * 获取当前结束时间
     */
    public static LocalDateTime theTodayMax() {
        return LocalDate.now().atTime(LocalTime.MAX);
    }

    /**
     * 获取指定日期开始时间
     */
    public static Date theDayMin(Date date) {
        return localDateTime2Date(date2LocalDate(date).atStartOfDay());
    }

    /**
     * 获取指定日期结束时间
     */
    public static Date theDayMax(Date date) {
        return localDateTime2Date(date2LocalDate(date).atTime(LocalTime.MAX));
    }

    public static LocalDate date2LocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalDateTime date2LocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static Date localDateTime2Date(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static String getCurrentDateTime(String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return LocalDateTime.now().format(formatter);
    }

    public static String getCurrentDate() {
        return getCurrentDateTime(AppCommonConstant.DATE_PATTERN);
    }

    public static String getCurrentTime() {
        return getCurrentDateTime(AppCommonConstant.TIME_PATTERN);
    }

    public static String getCurrentDateTime() {
        return getCurrentDateTime(AppCommonConstant.DATE_TIME_PATTERN);
    }
}
