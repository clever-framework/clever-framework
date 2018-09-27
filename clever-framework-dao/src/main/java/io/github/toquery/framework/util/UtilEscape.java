package io.github.toquery.framework.util;

import com.google.common.base.Strings;

public class UtilEscape {

    public static String escapeSQL(String input) {
        if (Strings.isNullOrEmpty(input)) {
            return "";
        }

        input = input.replaceAll("[%_$]", "/$0");

        input = input.replaceAll("'", "''");

        return input;
    }

}
