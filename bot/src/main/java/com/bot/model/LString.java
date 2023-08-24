package com.bot.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class LString {
    private String title;
    private Map<String, String> placeholders;

    public static LString empty() {
        return LString.builder().title("").build();
    }
}
