package com.bot.model;

import com.bot.util.KeyBoardUtils;
import lombok.Getter;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.util.function.Function;

/**
 * @author Serhii_Udaltsov on 12/24/2021
 */
@Getter
public enum ButtonsType {
    KEYBOARD(KeyBoardUtils::buildReplyKeyboard),
    LOCATION(KeyBoardUtils::buildReplyKeyboard),
    CONTACTS(KeyBoardUtils::buildContactsKeyboard),
    DATE_PICKER(KeyBoardUtils::buildDatePickerCreateAppointment),
    DATE_PICKER_MY_APP(KeyBoardUtils::buildDatePickerMyAppointments),
    DATE_PICKER_PERIOD_DAY_OFF(KeyBoardUtils::buildDatePickerDayOffPeriod),
    INLINE(KeyBoardUtils::buildInlineKeyboard),
    INLINE_LINKS(KeyBoardUtils::buildInlineLinksKeyboard);

    private final Function<BuildKeyboardRequest, ReplyKeyboard> buttonsFunction;

    ButtonsType(Function<BuildKeyboardRequest, ReplyKeyboard> buttonsFunction) {
        this.buttonsFunction = buttonsFunction;
    }
}
