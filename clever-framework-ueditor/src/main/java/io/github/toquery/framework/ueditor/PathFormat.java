package io.github.toquery.framework.ueditor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author toquery
 * @version 1
 */
public class PathFormat {
    private static final String TIME = "time";
    private static final String FULL_YEAR = "yyyy";
    private static final String YEAR = "yy";
    private static final String MONTH = "mm";
    private static final String DAY = "dd";
    private static final String HOUR = "hh";
    private static final String MINUTE = "ii";
    private static final String SECOND = "ss";
    private static final String RAND = "rand";
    private static Date currentDate = null;

    public PathFormat() {
    }

    public static String parse(String input) {
        Pattern pattern = Pattern.compile("\\{([^\\}]+)\\}", 2);
        Matcher matcher = pattern.matcher(input);
        currentDate = new Date();
        StringBuffer sb = new StringBuffer();

        while(matcher.find()) {
            matcher.appendReplacement(sb, getString(matcher.group(1)));
        }

        matcher.appendTail(sb);
        return sb.toString();
    }

    public static String format(String input) {
        return input.replace("\\", "/");
    }

    public static String parse(String input, String filename) {
        Pattern pattern = Pattern.compile("\\{([^\\}]+)\\}", 2);
        Matcher matcher = pattern.matcher(input);
        String matchStr = null;
        currentDate = new Date();
        StringBuffer sb = new StringBuffer();

        while(matcher.find()) {
            matchStr = matcher.group(1);
            if (matchStr.contains("filename")) {
                filename = filename.replace("$", "\\$").replaceAll("[\\/:*?\"<>|]", "");
                matcher.appendReplacement(sb, filename);
            } else {
                matcher.appendReplacement(sb, getString(matchStr));
            }
        }

        matcher.appendTail(sb);
        return sb.toString();
    }

    private static String getString(String pattern) {
        pattern = pattern.toLowerCase();
        if (pattern.contains(TIME)) {
            return getTimestamp();
        } else if (pattern.contains(FULL_YEAR)) {
            return getFullYear();
        } else if (pattern.contains(YEAR)) {
            return getYear();
        } else if (pattern.contains(MONTH)) {
            return getMonth();
        } else if (pattern.contains(DAY)) {
            return getDay();
        } else if (pattern.contains(HOUR)) {
            return getHour();
        } else if (pattern.contains(MINUTE)) {
            return getMinute();
        } else if (pattern.contains(SECOND)) {
            return getSecond();
        } else {
            return pattern.contains(RAND) ? getRandom(pattern) : pattern;
        }
    }

    private static String getTimestamp() {
        return String.valueOf(System.currentTimeMillis());
    }

    private static String getFullYear() {
        return (new SimpleDateFormat(FULL_YEAR)).format(currentDate);
    }

    private static String getYear() {
        return (new SimpleDateFormat(YEAR)).format(currentDate);
    }

    private static String getMonth() {
        return (new SimpleDateFormat(MONTH)).format(currentDate);
    }

    private static String getDay() {
        return (new SimpleDateFormat(DAY)).format(currentDate);
    }

    private static String getHour() {
        return (new SimpleDateFormat(HOUR)).format(currentDate);
    }

    private static String getMinute() {
        return (new SimpleDateFormat(MINUTE)).format(currentDate);
    }

    private static String getSecond() {
        return (new SimpleDateFormat(SECOND)).format(currentDate);
    }

    private static String getRandom(String pattern) {
        // int length = false;
        pattern = pattern.split(":")[1].trim();
        int length = Integer.parseInt(pattern);
        return String.valueOf(Math.random()).replace(".", "").substring(0, length);
    }


}
