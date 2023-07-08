package com.bot.processor.impl.general.admin.appointments;

import com.bot.model.Appointment;
import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.bot.processor.impl.general.user.appointment.AppointmentsFirstStepProcessor;
import com.bot.service.IAppointmentService;
import com.commons.model.Department;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.function.Supplier;

public class AllAppointmentsFirstStepProcessor extends AppointmentsFirstStepProcessor implements IProcessor {

    private final IAppointmentService appointmentService;

    public AllAppointmentsFirstStepProcessor(IAppointmentService appointmentService) {
        super(appointmentService);
        this.appointmentService = appointmentService;
    }

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        return getAppointmentsFirstStepResponse(request);
    }

    @Override
    protected Supplier<List<Appointment>> getAppointmentsSupplier(ProcessRequest request, long startDate, long finishDate) {
        Department department = request.getDepartment();
        return () -> appointmentService.getAppointmentsByDepartment(department, startDate, finishDate);
    }
}
