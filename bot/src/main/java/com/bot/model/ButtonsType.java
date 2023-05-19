package com.bot.model;

import com.bot.util.KeyBoardUtils;
import lombok.Getter;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author Serhii_Udaltsov on 12/24/2021
 */
@Getter
public enum ButtonsType {
    KEYBOARD(KeyBoardUtils::buildReplyKeyboard),
    CONTACTS(KeyBoardUtils::buildContactsKeyboard),
    DATE_PICKER(KeyBoardUtils::buildDatePicker),
    INLINE(KeyBoardUtils::buildInlineKeyboard);

    private final Function<BuildKeyboardRequest, ReplyKeyboard> buttonsFunction;

    ButtonsType(Function<BuildKeyboardRequest, ReplyKeyboard> buttonsFunction) {
        this.buttonsFunction = buttonsFunction;
    }
}
