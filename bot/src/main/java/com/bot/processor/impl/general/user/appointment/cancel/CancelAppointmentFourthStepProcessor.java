package com.bot.processor.impl.general.user.appointment.cancel;

import com.commons.model.Appointment;
import com.bot.model.BuildKeyboardRequest;
import com.bot.model.ButtonsType;
import com.bot.model.Context;
import com.bot.model.LString;
import com.bot.model.MessageHolder;
import com.bot.model.MessageTemplate;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.commons.service.IAppointmentService;
import com.bot.service.ISendMessageService;
import com.bot.util.Constants;
import com.bot.util.ContextUtils;
import com.bot.util.MessageUtils;
import com.commons.model.Department;
import com.commons.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class CancelAppointmentFourthStepProcessor implements IProcessor {

    private final IAppointmentService appointmentService;
    private final ISendMessageService sendMessageService;

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        Update update = request.getUpdate();
        Context context = request.getContext();
        Department department = request.getDepartment();
        String cancelSubmit = MessageUtils.getTextFromUpdate(update);
        String strategyKey = ContextUtils.getStrategyKey(context, department);
        if (Constants.SUBMIT.equals(cancelSubmit)) {
            String selectedAppointmentStr = (String) context.getParams().get(Constants.SELECTED_APPOINTMENT);
            Appointment appointment = JsonUtils.parseStringToObject(selectedAppointmentStr, Appointment.class);
            appointmentService.delete(appointment);
            ContextUtils.resetLocationToDashboard(context);
            List<LString> messagesToLocalize = new ArrayList<>();
            messagesToLocalize.add(LString.builder().title("Appointment CANCELED:").build());
            messagesToLocalize.add(LString.empty());
            MessageUtils.fillMessagesToLocalize(messagesToLocalize, appointment, context, MessageTemplate.APPOINTMENT_ALL_FIELDS);
            List<LString> adminMessages = MessageUtils.buildNotificationForAdmins(messagesToLocalize, context, department);
            sendMessageService.sendNotificationToAdmins(adminMessages, department);
            return List.of(MessageUtils.buildDashboardHolder("Appointment was canceled", List.of(), strategyKey));
        }
        ContextUtils.setPreviousStep(context);
        BuildKeyboardRequest holderRequest = MessageUtils.buildVerticalHolderRequestWithCommon(List.of(Constants.SUBMIT));
        return List.of(MessageUtils.holder("Select option from proposed", ButtonsType.KEYBOARD, holderRequest));
    }
}
