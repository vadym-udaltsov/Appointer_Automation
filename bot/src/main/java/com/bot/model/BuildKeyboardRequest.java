package com.bot.model;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class BuildKeyboardRequest {

    private Map<String, String> buttonsMap;
    private KeyBoardType type;
    private Map<String, Object> params;
}
