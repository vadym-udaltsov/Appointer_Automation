package com.bot.processor.impl.general.user.appointment.view;

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

public class ViewAppointmentsSecondStepProcessor extends AppointmentsSecondStepProcessor implements IProcessor {

    public ViewAppointmentsSecondStepProcessor(IAppointmentService appointmentService) {
        super(appointmentService);
    }

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        Context context = request.getContext();
        Set<String> availableDates = (Set<String>) context.getParams().get(Constants.AVAILABLE_DATES);
        Update update = request.getUpdate();
        String selectedDay = MessageUtils.getTextFromUpdate(update);
        if (!availableDates.contains(selectedDay)) {
            ContextUtils.resetLocationToDashboard(context);
            Department department = request.getDepartment();
            String strategyKey = ContextUtils.getStrategyKey(context, department);
            return List.of(MessageUtils.buildDashboardHolder("You entered wrong date or have no appointments",
                    List.of(), strategyKey));
        }
        return buildResponse(request);
    }

    @Override
    protected List<MessageHolder> getHolders(List<Appointment> appointments, String strategyKey) {
        List<LString> messagesToLocalize = new ArrayList<>();
        if (appointments == null || appointments.size() == 0) {
            messagesToLocalize.add(LString.builder().title("You have no appointments for selected date").build());
            return List.of(MessageUtils.buildDashboardHolder("Select action", messagesToLocalize, strategyKey));
        }
        messagesToLocalize.add(LString.builder().title("Your appointments:").build());
        messagesToLocalize.add(LString.empty());
        for (Appointment appointment : appointments) {
            MessageUtils.fillMessagesToLocalize(messagesToLocalize, appointment, null, MessageTemplate.APPOINTMENT_ALL_FIELDS);
            messagesToLocalize.add(LString.empty());
        }
        return List.of(MessageUtils.buildDashboardHolder("Select action", messagesToLocalize, strategyKey));
    }

    @Override
    protected void resetLocationToDashboard(Context context) {
        ContextUtils.resetLocationToDashboard(context);
    }
}
