package com.bot.processor.impl.general.admin.dayoff.create;

import com.bot.model.BuildKeyboardRequest;
import com.bot.model.ButtonsType;
import com.bot.model.Context;
import com.bot.model.DatePickerRequest;
import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.bot.processor.impl.general.user.appointment.create.AbstractGetCalendarProcessor;
import com.bot.service.IAppointmentService;
import com.bot.util.Constants;
import com.bot.util.ContextUtils;
import com.bot.util.DateUtils;
import com.bot.util.MessageUtils;
import com.commons.model.Department;
import com.commons.model.Specialist;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.stream.Collectors;

public class CreateDayOffSecondStepProcessor extends AbstractGetCalendarProcessor implements IProcessor {

    public CreateDayOffSecondStepProcessor(IAppointmentService appointmentService) {
        super(appointmentService);
    }

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        Department department = request.getDepartment();
        Context context = request.getContext();
        Update update = request.getUpdate();
        List<Specialist> availableSpecialists = department.getAvailableSpecialists();
        String selectedSpecialist = MessageUtils.getTextFromUpdate(update);
        List<String> specialistNames = availableSpecialists.stream()
                .map(Specialist::getName)
                .collect(Collectors.toList());
        if (!specialistNames.contains(selectedSpecialist) && !Constants.BACK.equals(selectedSpecialist)) {
            ContextUtils.setPreviousStep(context);
            BuildKeyboardRequest holderRequest = MessageUtils.buildVerticalHolderRequestWithCommon(specialistNames);
            return List.of(MessageUtils.holder("Select specialist from proposed", ButtonsType.KEYBOARD, holderRequest));
        }

        if (Constants.BACK.equals(selectedSpecialist)) {
            selectedSpecialist = ContextUtils.getStringParam(context, Constants.SELECTED_SPEC);
        }

        int numberOfCurrentMonth = DateUtils.getNumberOfCurrentMonth(department);
        context.getParams().put(Constants.MONTH, numberOfCurrentMonth);
        ContextUtils.setStringParameter(context, Constants.SELECTED_SPEC, selectedSpecialist);
        DatePickerRequest datePickerRequest = DatePickerRequest.builder()
                .department(department)
                .isNextMonth(false)
                .message("")
                .selectedSpecialist(selectedSpecialist)
                .context(context)
                .build();
        return buildResponse(datePickerRequest);
    }
}
