package com.bot.processor.impl.general.user.appointment.create;

import com.bot.model.Context;
import com.bot.model.LString;
import com.bot.model.MessageHolder;
import com.bot.model.MessageTemplate;
import com.bot.service.IContextService;
import com.bot.service.ISendMessageService;
import com.bot.util.ContextUtils;
import com.bot.util.MessageUtils;
import com.commons.model.Appointment;
import com.commons.model.Department;
import com.commons.service.IAppointmentService;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class BaseCreateAppointmentProcessor {

    private final IContextService contextService;
    private final IAppointmentService appointmentService;
    private final ISendMessageService sendMessageService;

    protected List<MessageHolder> createAppointment(Appointment appointment, Context context, Department department,
                                                    Context clientContext) {
        appointmentService.save(appointment);
        contextService.resetLocationToDashboard(context);
        String title = "Appointment CREATED:";
        if (clientContext != null) {
            title = "Appointment by phone CREATED:";
        }
        List<LString> messagesToLocalize = new ArrayList<>();
        messagesToLocalize.add(LString.builder().title(title).build());
        messagesToLocalize.add(LString.empty());

        MessageUtils.fillMessagesToLocalize(messagesToLocalize, appointment, clientContext == null ? context : clientContext,
                MessageTemplate.APPOINTMENT_ALL_FIELDS);
        List<LString> adminMessages = MessageUtils.buildNotificationForAdmins(messagesToLocalize,
                clientContext == null ? context : clientContext, department);
        sendMessageService.sendNotificationToAdmins(adminMessages, department);
        String strategyKey = ContextUtils.getStrategyKey(context, department);
        if (clientContext != null) {
            return List.of(MessageUtils.buildDashboardHolder("Select action", List.of(), strategyKey));
        }
        return List.of(MessageUtils.buildDashboardHolder("", messagesToLocalize, strategyKey));
    }
}
