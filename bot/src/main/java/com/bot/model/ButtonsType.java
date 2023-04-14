package com.bot.model;

import com.bot.util.KeyBoardUtils;
import lombok.Getter;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.util.Map;
import java.util.function.BiFunction;

/**
 * @author Serhii_Udaltsov on 12/24/2021
 */
@Getter
public enum ButtonsType {
    KEYBOARD(KeyBoardUtils::buildReplyKeyboard),
    CONTACTS(KeyBoardUtils::buildContactsKeyboard),
    DATE_PICKER(KeyBoardUtils::buildDatePicker),
    INLINE(KeyBoardUtils::buildInlineKeyboard);

    private final BiFunction<Map<String, String>, KeyBoardType, ReplyKeyboard> buttonsFunction;

    ButtonsType(BiFunction<Map<String, String>, KeyBoardType, ReplyKeyboard> buttonsFunction) {
        this.buttonsFunction = buttonsFunction;
    }
}
