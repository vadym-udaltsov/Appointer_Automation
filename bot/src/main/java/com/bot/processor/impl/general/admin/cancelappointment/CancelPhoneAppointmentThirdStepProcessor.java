package com.bot.processor.impl.general.admin.cancelappointment;

import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.bot.processor.impl.general.user.appointment.cancel.CancelAppointmentThirdStep;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

public class CancelPhoneAppointmentThirdStepProcessor extends CancelAppointmentThirdStep implements IProcessor {

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        return buildResponse(request);
    }
}
