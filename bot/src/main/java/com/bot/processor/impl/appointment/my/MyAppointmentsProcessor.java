package com.bot.processor.impl.appointment.my;

import com.bot.model.ButtonsType;
import com.bot.model.Context;
import com.bot.model.KeyBoardType;
import com.bot.model.MessageHolder;
import com.bot.processor.IProcessor;
import com.bot.util.MessageUtils;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

public class MyAppointmentsProcessor implements IProcessor {

    @Override
    public List<MessageHolder> processRequest(Update update, Context context) throws TelegramApiException {
        return List.of(MessageUtils.holder(List.of("first", "second"), "test message", KeyBoardType.TWO_ROW,
                true, ButtonsType.KEYBOARD));
    }
}
