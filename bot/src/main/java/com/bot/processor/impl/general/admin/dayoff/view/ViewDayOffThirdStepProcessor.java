package com.bot.processor.impl.general.admin.dayoff.view;

import com.bot.model.Appointment;
import com.bot.model.Context;
import com.bot.model.LString;
import com.bot.model.MessageHolder;
import com.bot.model.MessageTemplate;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.bot.processor.impl.general.user.appointment.AppointmentsSecondStepProcessor;
import com.bot.service.IAppointmentService;
import com.bot.util.Constants;
import com.bot.util.ContextUtils;
import com.bot.util.MessageUtils;
import com.commons.model.Department;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ViewDayOffThirdStepProcessor extends AppointmentsSecondStepProcessor implements IProcessor {

    private final IAppointmentService appointmentService;
    private final IProcessor previousStepProcessor;

    public ViewDayOffThirdStepProcessor(IAppointmentService appointmentService, IProcessor previousStepProcessor) {
        super(appointmentService);
        this.appointmentService = appointmentService;
        this.previousStepProcessor = previousStepProcessor;
    }

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        Context context = request.getContext();
        Set<String> availableDates = (Set<String>) context.getParams().get(Constants.AVAILABLE_DATES);
        Update update = request.getUpdate();
        String selectedDay = MessageUtils.getTextFromUpdate(update);
        if (!availableDates.contains(selectedDay)) {
            ContextUtils.setPreviousStep(context);
            MessageUtils.setTextToUpdate(update, ContextUtils.getStringParam(context, Constants.SELECTED_SPEC));
            return previousStepProcessor.processRequest(request);
        }
        if (Constants.NEXT_MONTH.equals(selectedDay) || Constants.CURRENT_MONTH.equals(selectedDay)) {
            return buildResponse(request);
        }
        List<MessageHolder> messageHolders = buildResponse(request);
        ContextUtils.resetLocationToDashboard(context);
        return messageHolders;
    }

    @Override
    protected Supplier<List<Appointment>> getAppointmentSupplier(ProcessRequest request, long start, long finish) {
        Context context = request.getContext();
        String selectedSpecialist = ContextUtils.getStringParam(context, Constants.SELECTED_SPEC);
        return () -> {
            List<Appointment> appointmentsBySpecialist = appointmentService.getAppointmentsBySpecialist(request.getDepartment(),
                    selectedSpecialist, start, finish);
            return appointmentsBySpecialist.stream()
                    .filter(a -> Constants.DAY_OFF.equals(a.getService()))
                    .collect(Collectors.toList());
        };
    }

    @Override
    protected List<MessageHolder> getHolders(List<Appointment> appointments, String strategyKey) {
        List<LString> messagesToLocalize = new ArrayList<>();
        if (appointments == null || appointments.size() == 0) {
            messagesToLocalize.add(LString.builder().title("You have no days off for selected date").build());
            return List.of(MessageUtils.buildDashboardHolder("Select action", messagesToLocalize, strategyKey));
        }
        messagesToLocalize.add(LString.builder().title("Your days off:").build());
        messagesToLocalize.add(LString.empty());
        for (Appointment appointment : appointments) {
            MessageUtils.fillMessagesToLocalize(messagesToLocalize, appointment, MessageTemplate.DAY_OFF_ALL_FIELDS);
            messagesToLocalize.add(LString.empty());
        }
        return List.of(MessageUtils.buildDashboardHolder("Select action", messagesToLocalize, strategyKey));
    }

    @Override
    protected void updateAvailableDates(Context context, Set<String> appointmentDays) {
        appointmentDays.addAll(List.of(Constants.NEXT_MONTH, Constants.CURRENT_MONTH));
        context.getParams().put(Constants.AVAILABLE_DATES, appointmentDays);
    }
}
