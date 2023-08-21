package com.bot.processor.impl.general.user.appointment.cancel;

import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.bot.service.ISendMessageService;
import com.commons.service.IAppointmentService;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

public class CancelAppointmentFourthStepProcessor extends CancelAppointmentFourthStep implements IProcessor {

    public CancelAppointmentFourthStepProcessor(IAppointmentService appointmentService, ISendMessageService sendMessageService) {
        super(appointmentService, sendMessageService);
    }


    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        return buildResponse(request);
    }
}
