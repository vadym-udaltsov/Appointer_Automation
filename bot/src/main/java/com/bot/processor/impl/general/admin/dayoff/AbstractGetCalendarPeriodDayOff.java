package com.bot.processor.impl.general.admin.dayoff;

import com.bot.model.BuildKeyboardRequest;
import com.bot.model.ButtonsType;
import com.bot.model.Context;
import com.bot.model.KeyBoardType;
import com.bot.model.MessageHolder;
import com.bot.util.Constants;
import com.bot.util.MessageUtils;
import com.commons.model.Department;

import java.time.Month;
import java.util.List;
import java.util.Map;

public class AbstractGetCalendarPeriodDayOff {

    protected List<MessageHolder> buildResponse(Department department, Context context, int month, String message,
                                                int year) {
        BuildKeyboardRequest commonsRequest = BuildKeyboardRequest.builder()
                .type(KeyBoardType.TWO_ROW)
                .buttonsMap(MessageUtils.buildButtons(List.of(), true))
                .build();

        MessageHolder commonButtonsHolder = MessageUtils.holder(message, ButtonsType.KEYBOARD, commonsRequest);

        context.getParams().put(Constants.SELECTED_MONTH, month);
        context.getParams().put(Constants.SELECTED_YEAR, year);

        BuildKeyboardRequest datePickerRequest = BuildKeyboardRequest.builder()
                .type(KeyBoardType.TWO_ROW)
                .buttonsMap(MessageUtils.buildButtons(MessageUtils.commonButtons(List.of()), false))
                .params(Map.of(
                        Constants.DEPARTMENT, department,
                        Constants.SELECTED_MONTH, month,
                        Constants.SELECTED_YEAR, year,
                        Constants.CONTEXT, context))
                .build();

        MessageHolder datePicker = MessageUtils.holder(Month.of(month).name(), ButtonsType.DATE_PICKER_PERIOD_DAY_OFF,
                datePickerRequest);
        return List.of(commonButtonsHolder, datePicker);
    }
}
