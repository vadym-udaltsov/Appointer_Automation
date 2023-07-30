package com.bot.processor.impl.general.admin.dayoff.view;

import com.bot.model.Appointment;
import com.bot.model.BuildKeyboardRequest;
import com.bot.model.ButtonsType;
import com.bot.model.Context;
import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.bot.processor.impl.general.user.appointment.AppointmentsFirstStepProcessor;
import com.bot.service.IAppointmentService;
import com.bot.util.Constants;
import com.bot.util.ContextUtils;
import com.bot.util.MessageUtils;
import com.commons.model.Department;
import com.commons.model.Specialist;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ViewDayOffSecondStepProcessor extends AppointmentsFirstStepProcessor implements IProcessor {

    private final IAppointmentService appointmentService;

    public ViewDayOffSecondStepProcessor(IAppointmentService appointmentService) {
        super(appointmentService);
        this.appointmentService = appointmentService;
    }

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        Department department = request.getDepartment();
        Context context = request.getContext();
        Update update = request.getUpdate();
        List<Specialist> availableSpecialists = department.getAvailableSpecialists();
        String selectedSpecialist = MessageUtils.getTextFromUpdate(update);
        List<String> specialistNames = availableSpecialists.stream()
                .map(Specialist::getName)
                .collect(Collectors.toList());
        if (!specialistNames.contains(selectedSpecialist) && !Constants.BACK.equals(selectedSpecialist)) {
            ContextUtils.setPreviousStep(context);
            BuildKeyboardRequest holderRequest = MessageUtils.buildVerticalHolderRequestWithCommon(specialistNames);
            return List.of(MessageUtils.holder("Select specialist from proposed", ButtonsType.KEYBOARD, holderRequest));
        }
        ContextUtils.setStringParameter(context, Constants.SELECTED_SPEC, selectedSpecialist);
        return buildAppointmentsCalendar(request);
    }

    @Override
    protected Supplier<List<Appointment>> getAppointmentsSupplier(ProcessRequest request, long startDate, long finishDate) {
        Context context = request.getContext();
        String selectedSpecialist = ContextUtils.getStringParam(context, Constants.SELECTED_SPEC);
        return () -> {
            List<Appointment> appointmentsBySpecialist = appointmentService.getAppointmentsBySpecialist(request.getDepartment(),
                    selectedSpecialist, startDate, finishDate);
            return appointmentsBySpecialist.stream()
                    .filter(a -> Constants.DAY_OFF.equals(a.getService()))
                    .collect(Collectors.toList());
        };
    }
}
