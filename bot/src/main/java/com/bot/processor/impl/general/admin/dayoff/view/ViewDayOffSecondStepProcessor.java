package com.bot.processor.impl.general.admin.dayoff.view;

import com.bot.util.Constants;
import com.commons.model.Appointment;
import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.bot.processor.impl.general.user.appointment.AppointmentsFirstStepProcessor;
import com.commons.service.IAppointmentService;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.function.Supplier;

public class ViewDayOffSecondStepProcessor extends AppointmentsFirstStepProcessor implements IProcessor {

    public ViewDayOffSecondStepProcessor(IAppointmentService appointmentService) {
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

    @Override
    protected String getNoAppointmentsMessage() {
        return Constants.NO_DAYS_OFF;
    }
}
