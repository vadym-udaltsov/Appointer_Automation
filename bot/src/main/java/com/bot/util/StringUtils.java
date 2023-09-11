package com.bot.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
    public static final String EMPTY = "";

    public static boolean isBlank(String text) {
        return (text == null || EMPTY.equalsIgnoreCase(text));
    }

    public static boolean isMatch(String text, String regex) {
        if (text == null) {
            return false;
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }
}