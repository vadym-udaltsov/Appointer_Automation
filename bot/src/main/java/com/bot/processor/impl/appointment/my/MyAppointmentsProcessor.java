package com.bot.processor.impl.appointment.my;

import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

public class MyAppointmentsProcessor implements IProcessor {

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        return null;
    }
}
