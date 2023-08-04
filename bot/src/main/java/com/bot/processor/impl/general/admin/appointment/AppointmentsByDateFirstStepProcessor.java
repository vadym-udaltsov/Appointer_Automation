package com.bot.processor.impl.general.admin.appointment;

import com.commons.model.Appointment;
import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.bot.processor.impl.general.user.appointment.AppointmentsFirstStepProcessor;
import com.commons.service.IAppointmentService;
import com.bot.util.Constants;
import com.commons.model.Department;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class AppointmentsByDateFirstStepProcessor extends AppointmentsFirstStepProcessor implements IProcessor {

    private final IAppointmentService appointmentService;

    public AppointmentsByDateFirstStepProcessor(IAppointmentService appointmentService) {
        super(appointmentService);
        this.appointmentService = appointmentService;
    }

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        return buildAppointmentsCalendar(request);
    }

    @Override
    protected Supplier<List<Appointment>> getAppointmentsSupplier(ProcessRequest request, long startDate, long finishDate) {
        Department department = request.getDepartment();
        return () -> appointmentService.getAppointmentsByDepartment(department, startDate, finishDate)
                .stream()
                .filter(a -> !Constants.DAY_OFF.equals(a.getService()))
                .collect(Collectors.toList());
    }
}
