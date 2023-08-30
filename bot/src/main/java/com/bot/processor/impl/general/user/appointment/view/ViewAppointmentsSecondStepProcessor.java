package com.bot.processor.impl.general.user.appointment.view;

import com.bot.model.Context;
import com.bot.model.LString;
import com.bot.model.MessageHolder;
import com.bot.model.MessageTemplate;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.bot.processor.impl.general.user.appointment.AppointmentsSecondStepProcessor;
import com.bot.util.Constants;
import com.bot.util.ContextUtils;
import com.bot.util.MessageUtils;
import com.commons.model.Appointment;
import com.commons.model.Department;
import com.commons.service.IAppointmentService;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class ViewAppointmentsSecondStepProcessor extends AppointmentsSecondStepProcessor implements IProcessor {

    public ViewAppointmentsSecondStepProcessor(IAppointmentService appointmentService) {
        super(appointmentService);
    }

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        return buildResponse(request);
    }

    @Override
    protected List<MessageHolder> getHolders(List<Appointment> appointments, String strategyKey, Department department) {
        List<LString> messagesToLocalize = new ArrayList<>();
        if (appointments == null || appointments.size() == 0) {
            messagesToLocalize.add(LString.builder().title(Constants.Messages.NO_APP_FOR_DATE).build());
            return List.of(MessageUtils.buildDashboardHolder("Select action", messagesToLocalize, strategyKey));
        }
        messagesToLocalize.add(LString.builder().title("Your appointments:").build());
        messagesToLocalize.add(LString.empty());
        for (Appointment appointment : appointments) {
            MessageUtils.fillMessagesToLocalize(messagesToLocalize, appointment, null,
                    MessageTemplate.APPOINTMENT_ALL_FIELDS, department);
            messagesToLocalize.add(LString.empty());
        }
        return List.of(MessageUtils.buildDashboardHolder("Select action", messagesToLocalize, strategyKey));
    }

    @Override
    protected void resetLocationToDashboard(Context context) {
        ContextUtils.resetLocationToDashboard(context);
    }
}
