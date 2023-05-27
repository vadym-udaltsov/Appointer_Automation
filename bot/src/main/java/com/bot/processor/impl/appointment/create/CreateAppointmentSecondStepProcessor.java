package com.bot.processor.impl.appointment.create;

import com.bot.model.BuildKeyboardRequest;
import com.bot.model.ButtonsType;
import com.bot.model.Context;
import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.bot.service.IAppointmentService;
import com.bot.util.Constants;
import com.bot.util.ContextUtils;
import com.bot.util.DateUtils;
import com.bot.util.MessageUtils;
import com.commons.model.CustomerService;
import com.commons.model.Department;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class CreateAppointmentSecondStepProcessor extends AbstractGetCalendarProcessor implements IProcessor {

    public CreateAppointmentSecondStepProcessor(IAppointmentService appointmentService) {
        super(appointmentService);
    }

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        Department department = request.getDepartment();
        Context context = request.getContext();
        Update update = request.getUpdate();

        String selectedServiceName = MessageUtils.getTextFromUpdate(update);
        List<String> availableServices = department.getServices().stream()
                .map(CustomerService::getName)
                .collect(Collectors.toList());

        if (!availableServices.contains(selectedServiceName) && !Constants.BACK.equals(selectedServiceName)) {
            ContextUtils.setPreviousStep(context);
            BuildKeyboardRequest holderRequest = MessageUtils.buildVerticalHolderRequestWithCommon(availableServices);
            return List.of(MessageUtils.holder("Select service from proposed", ButtonsType.KEYBOARD, holderRequest));
        }
        if (Constants.BACK.equals(selectedServiceName)) {
            selectedServiceName = ContextUtils.getStringParam(context, Constants.SELECTED_SERVICE);
        }
        int numberOfCurrentMonth = DateUtils.getNumberOfCurrentMonth(department);
        ContextUtils.setStringParameter(context, Constants.MONTH, String.valueOf(numberOfCurrentMonth));
        ContextUtils.setStringParameter(context, Constants.SELECTED_SERVICE, selectedServiceName);
        return buildResponse(department, false, "", context, selectedServiceName);
    }
}
