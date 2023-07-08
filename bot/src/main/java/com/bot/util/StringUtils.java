package com.bot.util;

public class StringUtils {
    public static final String EMPTY = "";

    public static boolean isBlank(String text) {
        return (text == null || EMPTY.equalsIgnoreCase(text));
    }
}