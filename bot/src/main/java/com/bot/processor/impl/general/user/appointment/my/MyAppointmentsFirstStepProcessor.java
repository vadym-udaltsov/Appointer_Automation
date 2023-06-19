package com.bot.processor.impl.general.user.appointment.my;

import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.bot.processor.impl.general.user.appointment.AppointmentsFirstStepProcessor;
import com.bot.service.IAppointmentService;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

public class MyAppointmentsFirstStepProcessor extends AppointmentsFirstStepProcessor implements IProcessor {

    public MyAppointmentsFirstStepProcessor(IAppointmentService appointmentService) {
        super(appointmentService);
    }

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        return getAppointmentsFirstStepResponse(request);
    }
}
