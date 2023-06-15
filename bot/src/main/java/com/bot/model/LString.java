package com.bot.model;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class LString {
    private String title;
    private Map<String, String> placeholders;

    public static LString empty() {
        return LString.builder().title("").build();
    }
}
