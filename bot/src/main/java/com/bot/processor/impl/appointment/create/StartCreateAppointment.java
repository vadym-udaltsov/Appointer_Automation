package com.bot.processor.impl.appointment.create;

import com.bot.model.ButtonsType;
import com.bot.model.Context;
import com.bot.model.KeyBoardType;
import com.bot.model.MessageHolder;
import com.bot.processor.IProcessor;
import com.bot.util.MessageUtils;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

public class StartCreateAppointment implements IProcessor {

    @Override
    public List<MessageHolder> processRequest(Update update, Context context) throws TelegramApiException {
        Month month = LocalDate.now().getMonth();
        MessageHolder commonButtonsHolder = MessageUtils.holder(List.of(), "Select date", KeyBoardType.TWO_ROW, true, ButtonsType.KEYBOARD);
        MessageHolder datePicker = MessageUtils.holder(List.of(), month.name(), KeyBoardType.TWO_ROW,
                false, ButtonsType.DATE_PICKER);
        return List.of(commonButtonsHolder, datePicker);
    }
}
