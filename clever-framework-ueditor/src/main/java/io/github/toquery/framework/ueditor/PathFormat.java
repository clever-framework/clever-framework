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
        if (pattern.contains("time")) {
            return getTimestamp();
        } else if (pattern.contains("yyyy")) {
            return getFullYear();
        } else if (pattern.contains("yy")) {
            return getYear();
        } else if (pattern.contains("mm")) {
            return getMonth();
        } else if (pattern.contains("dd")) {
            return getDay();
        } else if (pattern.contains("hh")) {
            return getHour();
        } else if (pattern.contains("ii")) {
            return getMinute();
        } else if (pattern.contains("ss")) {
            return getSecond();
        } else {
            return pattern.contains("rand") ? getRandom(pattern) : pattern;
        }
    }

    private static String getTimestamp() {
        return String.valueOf(System.currentTimeMillis());
    }

    private static String getFullYear() {
        return (new SimpleDateFormat("yyyy")).format(currentDate);
    }

    private static String getYear() {
        return (new SimpleDateFormat("yy")).format(currentDate);
    }

    private static String getMonth() {
        return (new SimpleDateFormat("MM")).format(currentDate);
    }

    private static String getDay() {
        return (new SimpleDateFormat("dd")).format(currentDate);
    }

    private static String getHour() {
        return (new SimpleDateFormat("HH")).format(currentDate);
    }

    private static String getMinute() {
        return (new SimpleDateFormat("mm")).format(currentDate);
    }

    private static String getSecond() {
        return (new SimpleDateFormat("ss")).format(currentDate);
    }

    private static String getRandom(String pattern) {
        // int length = false;
        pattern = pattern.split(":")[1].trim();
        int length = Integer.parseInt(pattern);
        return String.valueOf(Math.random()).replace(".", "").substring(0, length);
    }


}
