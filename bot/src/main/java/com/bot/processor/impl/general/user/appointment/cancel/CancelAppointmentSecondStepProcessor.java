package com.bot.processor.impl.general.user.appointment.cancel;

import com.bot.model.Appointment;
import com.bot.model.BuildKeyboardRequest;
import com.bot.model.ButtonsType;
import com.bot.model.Context;
import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.bot.processor.impl.general.user.appointment.AppointmentsSecondStepProcessor;
import com.bot.service.IAppointmentService;
import com.bot.util.Constants;
import com.bot.util.DateUtils;
import com.bot.util.MessageUtils;
import com.commons.utils.JsonUtils;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CancelAppointmentSecondStepProcessor extends AppointmentsSecondStepProcessor implements IProcessor {

    public CancelAppointmentSecondStepProcessor(IAppointmentService appointmentService) {
        super(appointmentService);
    }

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        return buildResponse(request);
    }

    @Override
    protected void fillContextParams(List<Appointment> appointments, Context context) {
        Map<String, Object> params = context.getParams();
        List<String> availableTitles = new ArrayList<>();
        for (Appointment appointment : appointments) {
            String title = getAppointmentButtonTitle(appointment);
            params.put(title, JsonUtils.convertObjectToString(appointment));
            availableTitles.add(title);
        }
        params.put(Constants.AVAILABLE_APPOINTMENTS, availableTitles);
    }

    @Override
    protected List<MessageHolder> getHolders(List<Appointment> appointments, String strategyKey) {
        List<String> buttons = appointments.stream().map(this::getAppointmentButtonTitle).collect(Collectors.toList());
        BuildKeyboardRequest holderRequest = MessageUtils.buildVerticalHolderRequestWithCommon(buttons);
        return List.of(MessageUtils.holder("Select appointment", ButtonsType.KEYBOARD, holderRequest));
    }

    private String getAppointmentButtonTitle(Appointment appointment) {
        StringBuilder builder = new StringBuilder();
        long date = appointment.getDate();
        String dateTitle = DateUtils.getDateTitle(date);
        String time = dateTitle.split(",")[1];
        String serviceName = appointment.getService();
        return builder.append(time).append(" ").append(serviceName).toString();
    }
}
