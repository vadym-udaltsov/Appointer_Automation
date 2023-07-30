package com.bot.processor.impl.general.user.appointment.view;

import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.bot.processor.impl.general.user.appointment.AppointmentsFirstStepProcessor;
import com.bot.service.IAppointmentService;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

public class ViewAppointmentsFirstStepProcessor extends AppointmentsFirstStepProcessor implements IProcessor {

    public ViewAppointmentsFirstStepProcessor(IAppointmentService appointmentService) {
        super(appointmentService);
    }

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        return buildAppointmentsCalendar(request);
    }
}
