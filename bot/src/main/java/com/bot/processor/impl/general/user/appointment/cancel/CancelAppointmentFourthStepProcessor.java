package com.bot.processor.impl.general.user.appointment.cancel;

import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

public class CancelAppointmentFourthStepProcessor implements IProcessor {

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        return null;
    }
}
