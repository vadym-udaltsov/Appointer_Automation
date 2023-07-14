package com.bot.processor.impl.general.user.appointment.my;

import com.bot.model.Appointment;
import com.bot.model.Context;
import com.bot.model.LString;
import com.bot.model.MessageHolder;
import com.bot.model.MessageTemplate;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.bot.processor.impl.general.user.appointment.AppointmentsSecondStepProcessor;
import com.bot.service.IAppointmentService;
import com.bot.util.ContextUtils;
import com.bot.util.MessageUtils;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class MyAppointmentsSecondStepProcessor extends AppointmentsSecondStepProcessor implements IProcessor {

    public MyAppointmentsSecondStepProcessor(IAppointmentService appointmentService) {
        super(appointmentService);
    }

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        return buildResponse(request);
    }

    @Override
    protected List<MessageHolder> getHolders(List<Appointment> appointments, String strategyKey) {
        List<LString> messagesToLocalize = new ArrayList<>();
        messagesToLocalize.add(LString.builder().title("Your appointments:").build());
        messagesToLocalize.add(LString.empty());
        for (Appointment appointment : appointments) {
            MessageUtils.fillMessagesToLocalize(messagesToLocalize, appointment, MessageTemplate.APPOINTMENT_ALL_FIELDS);
            messagesToLocalize.add(LString.empty());
        }
        return List.of(MessageUtils.buildDashboardHolder("Select action", messagesToLocalize, strategyKey));
    }

    @Override
    protected void resetLocationToDashboard(Context context) {
        ContextUtils.resetLocationToDashboard(context);
    }
}
