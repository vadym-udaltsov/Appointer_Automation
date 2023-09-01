package com.bot.processor.impl.general.admin.dayoff;

import com.bot.model.BuildKeyboardRequest;
import com.bot.model.ButtonsType;
import com.bot.model.Context;
import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.bot.processor.impl.general.admin.dayoff.AbstractGetCalendarPeriodDayOff;
import com.bot.util.Constants;
import com.bot.util.ContextUtils;
import com.bot.util.MessageUtils;
import com.commons.model.Department;
import com.commons.model.Specialist;
import com.commons.service.IAppointmentService;
import com.commons.utils.DateUtils;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class PeriodDayOffCommonSecondStepProcessor extends AbstractGetCalendarPeriodDayOff implements IProcessor {

    public PeriodDayOffCommonSecondStepProcessor(IAppointmentService appointmentService) {
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
            ContextUtils.resetLocationToPreviousStep(context);
            BuildKeyboardRequest holderRequest = MessageUtils.buildVerticalHolderRequestWithCommon(specialistNames);
            return List.of(MessageUtils.holder(Constants.Messages.INCORRECT_SPECIALIST, ButtonsType.KEYBOARD, holderRequest));
        }
        if (!Constants.BACK.equals(selectedSpecialist)) {
            context.getParams().put(Constants.SELECTED_SPEC, selectedSpecialist);
        }
        ZonedDateTime dateTime = DateUtils.nowZoneDateTime(department);
        int month = dateTime.getMonth().getValue();
        int year = dateTime.getYear();
        return buildResponse(department, context, month, Constants.Messages.SELECT_START_DATE, year);
    }
}
