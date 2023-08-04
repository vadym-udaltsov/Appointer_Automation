package com.bot.processor.impl.general.user.appointment.cancel;

import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.bot.processor.impl.general.user.appointment.AppointmentsFirstStepProcessor;
import com.commons.service.IAppointmentService;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

public class CancelAppointmentFirstStepProcessor extends AppointmentsFirstStepProcessor implements IProcessor {

    public CancelAppointmentFirstStepProcessor(IAppointmentService appointmentService) {
        super(appointmentService);
    }

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        return buildAppointmentsCalendar(request);
    }
}
