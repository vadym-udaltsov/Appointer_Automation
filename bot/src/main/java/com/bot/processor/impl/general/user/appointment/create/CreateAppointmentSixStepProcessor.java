package com.bot.processor.impl.general.user.appointment.create;

import com.bot.model.Context;
import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.bot.service.IContextService;
import com.bot.service.ISendMessageService;
import com.bot.util.ContextUtils;
import com.bot.util.MessageUtils;
import com.commons.model.Appointment;
import com.commons.model.Department;
import com.commons.service.IAppointmentService;
import com.commons.utils.DateUtils;
import com.commons.utils.JsonUtils;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Random;

public class CreateAppointmentSixStepProcessor extends BaseCreateAppointmentProcessor implements IProcessor {
    private final IContextService contextService;

    public CreateAppointmentSixStepProcessor(IContextService contextService, IAppointmentService appointmentService,
                                             ISendMessageService sendMessageService) {
        super(contextService, appointmentService, sendMessageService);
        this.contextService = contextService;
    }

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        Update update = request.getUpdate();
        Department department = request.getDepartment();
        Context context = request.getContext();
        String client = MessageUtils.getTextFromUpdate(update);
        String appointmentString = ContextUtils.getStringParam(context, "appointment");
        Appointment appointment = JsonUtils.parseStringToObject(appointmentString, Appointment.class);
        Context clientContext = new Context();
        clientContext.setDepartmentId(department.getId());
        clientContext.setUserId(new Random().nextLong());
        clientContext.setName(client);
        clientContext.setPhoneNumber("n/a");
        clientContext.setCustom(true);
        clientContext.setExpiration(DateUtils.getExpirationDate(department));
        contextService.create(clientContext);
        appointment.setUserId(clientContext.getUserId());
        appointment.setPhoneOrder(true);
        return createAppointment(appointment, context, department, clientContext);
    }
}
