package com.bot.processor.impl.appointment.create;

import com.bot.model.Context;
import com.bot.model.MessageHolder;
import com.bot.processor.IProcessor;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

public class SetAppointmentDateAskSpecialistProcessor implements IProcessor {

    @Override
    public List<MessageHolder> processRequest(Update update, Context context) throws TelegramApiException {
        return null;
    }
}
