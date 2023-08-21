package com.bot.processor.impl.general.admin.cancelappointment;

import com.bot.model.BuildKeyboardRequest;
import com.bot.model.ButtonsType;
import com.bot.model.Context;
import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.bot.processor.impl.general.user.appointment.AppointmentsSecondStepProcessor;
import com.bot.service.IContextService;
import com.bot.util.Constants;
import com.bot.util.MessageUtils;
import com.commons.model.Appointment;
import com.commons.model.Department;
import com.commons.service.IAppointmentService;
import com.commons.utils.DateUtils;
import com.commons.utils.JsonUtils;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class CancelPhoneAppointmentSecondStepProcessor extends AppointmentsSecondStepProcessor implements IProcessor {

    private final IAppointmentService appointmentService;
    private final IContextService contextService;

    public CancelPhoneAppointmentSecondStepProcessor(IAppointmentService appointmentService,
                                                     IContextService contextService) {
        super(appointmentService);
        this.appointmentService = appointmentService;
        this.contextService = contextService;
    }

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        return buildResponse(request);
    }

    @Override
    protected void fillContextParams(List<Appointment> appointments, Context context) {
        Map<Long, Context> contextData = contextService.getContextListByAppointments(appointments).stream()
                .collect(Collectors.toMap(Context::getUserId, Function.identity()));
        Map<String, Object> params = context.getParams();
        List<String> availableTitles = new ArrayList<>();
        for (Appointment appointment : appointments) {
            String title = getAppointmentButtonTitle(appointment, contextData);
            params.put(title, JsonUtils.convertObjectToString(appointment));
            long userId = appointment.getUserId();
            params.put(String.valueOf(userId), JsonUtils.convertObjectToString(contextData.get(userId)));
            availableTitles.add(title);
        }
        params.put(Constants.AVAILABLE_APPOINTMENTS, availableTitles);
    }

    @Override
    protected Supplier<List<Appointment>> getAppointmentSupplier(ProcessRequest request, long start, long finish) {
        Department department = request.getDepartment();
        return () -> appointmentService.getAppointmentsByDepartment(department, start, finish)
                .stream()
                .filter(Appointment::isPhoneOrder)
                .collect(Collectors.toList());
    }

    @Override
    protected List<MessageHolder> getHolders(List<Appointment> appointments, String strategyKey) {
        Map<Long, Context> contextData = contextService.getContextListByAppointments(appointments).stream()
                .collect(Collectors.toMap(Context::getUserId, Function.identity()));
        List<String> buttons = appointments.stream()
                .map(a -> getAppointmentButtonTitle(a, contextData))
                .collect(Collectors.toList());
        BuildKeyboardRequest holderRequest = MessageUtils.buildVerticalHolderRequestWithCommon(buttons);
        return List.of(MessageUtils.holder("Select appointment", ButtonsType.KEYBOARD, holderRequest));
    }

    private String getAppointmentButtonTitle(Appointment appointment, Map<Long, Context> contextData) {
        Context context = contextData.get(appointment.getUserId());
        if (context == null) {
            throw new RuntimeException("Context not found for user id: " + appointment.getUserId());
        }
        String clientName = context.getName();
        StringBuilder builder = new StringBuilder();
        long date = appointment.getDate();
        String dateTitle = DateUtils.getDateTitle(date);
        String time = dateTitle.split(",")[1];
        String serviceName = appointment.getService();
        return builder.append(time).append(" ").append(serviceName).append(", ").append(clientName).toString();
    }
}
