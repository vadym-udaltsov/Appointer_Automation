package com.bot.processor.impl.general.user.appointment.cancel;

import com.bot.model.Appointment;
import com.bot.model.BuildKeyboardRequest;
import com.bot.model.ButtonsType;
import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.bot.processor.impl.general.user.appointment.AppointmentsSecondStepProcessor;
import com.bot.service.IAppointmentService;
import com.bot.util.DateUtils;
import com.bot.util.MessageUtils;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
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
    protected List<MessageHolder> getHolders(List<Appointment> appointments) {
        List<String> buttons = appointments.stream().map(a -> {
            StringBuilder builder = new StringBuilder();
            long date = a.getDate();
            String dateTitle = DateUtils.getDateTitle(date);
            String time = dateTitle.split(",")[1];
            String serviceName = a.getService();
            return builder.append(time).append(System.lineSeparator()).append(serviceName).toString();
        }).collect(Collectors.toList());
        BuildKeyboardRequest holderRequest = MessageUtils.buildVerticalHolderRequestWithCommon(buttons);
        return List.of(MessageUtils.holder("Select appointment", ButtonsType.KEYBOARD, holderRequest));
    }
}
