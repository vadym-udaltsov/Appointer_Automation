package com.bot.processor.impl.general.admin.dayoff.cancel;

import com.bot.model.Appointment;
import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.bot.processor.impl.general.user.appointment.AppointmentsFirstStepProcessor;
import com.bot.service.IAppointmentService;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.function.Supplier;

public class CancelDayOffSecondStepProcessor extends AppointmentsFirstStepProcessor implements IProcessor {

    public CancelDayOffSecondStepProcessor(IAppointmentService appointmentService) {
        super(appointmentService);
    }

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        return buildDayOffResponse(request);
    }

    @Override
    protected Supplier<List<Appointment>> getAppointmentsSupplier(ProcessRequest request, long startDate, long finishDate) {
        return getDayOffAppointmentsSupplier(request, startDate, finishDate);
    }
}
