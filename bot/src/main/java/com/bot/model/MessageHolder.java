package com.bot.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Serhii_Udaltsov on 6/7/2021
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageHolder {
    private List<Button> buttons;
    private String message;
    private KeyBoardType keyBoardType;
    private ButtonsType buttonsType;
    private boolean withCommonButtons;
    private Map<String, String> placeholders = new HashMap<>();
}
