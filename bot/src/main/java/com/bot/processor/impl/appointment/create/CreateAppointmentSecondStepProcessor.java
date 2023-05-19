package com.bot.processor.impl.appointment.create;

import com.bot.model.Context;
import com.bot.model.FreeSlot;
import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.bot.service.IAppointmentService;
import com.bot.service.IContextService;
import com.bot.util.Constants;
import com.bot.util.ContextUtils;
import com.bot.util.DateUtils;
import com.bot.util.MessageUtils;
import com.commons.model.Department;
import com.commons.model.Specialist;
import com.commons.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class CreateAppointmentSecondStepProcessor extends AbstractGetCalendarProcessor implements IProcessor {

    private final IContextService contextService;
    private final IAppointmentService appointmentService;
    private final IProcessor nextStepProcessor;

    public CreateAppointmentSecondStepProcessor(IAppointmentService appointmentService, IContextService contextService,
                                                IProcessor nextStepProcessor) {
        super(appointmentService);
        this.contextService = contextService;
        this.appointmentService = appointmentService;
        this.nextStepProcessor = nextStepProcessor;
    }

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        Department department = request.getDepartment();
        Update update = request.getUpdate();
        Context context = request.getContext();
        String text = MessageUtils.getTextFromUpdate(update);
        if (Constants.NEXT_MONTH.equals(text)) {
            updateContextData(context, department, true);
            return buildResponse(department, true, StringUtils.EMPTY);
        }
        if (Constants.CURRENT_MONTH.equals(text)) {
            updateContextData(context, department, false);
            return buildResponse(department, false, StringUtils.EMPTY);
        }
        if (Constants.IGNORE.equals(text)) {
            updateContextData(context, department, false);
            return buildResponse(department, false, "Select available date");
        }
        List<String> specialists = department.getAvailableSpecialists().stream()
                .map(Specialist::getId)
                .collect(Collectors.toList());

        int dayNumber = Integer.parseInt(text);
        int monthNumber = Integer.parseInt(ContextUtils.getStringParam(context, Constants.MONTH));

        Map<String, List<FreeSlot>> slots = appointmentService.getFreeSlotsBySpecialists(specialists, department,
                monthNumber, dayNumber);

        if (specialists.size() == 1) {
            String specialistId = specialists.get(0);
            List<Map<String, Object>> freeSlots = slots.get(specialistId).stream()
                    .map(JsonUtils::parseObjectToMap)
                    .collect(Collectors.toList());
            context.getParams().put(specialistId, freeSlots);
            ContextUtils.setStringParameter(context, Constants.SELECTED_DAY, String.valueOf(dayNumber));
            MessageUtils.setTextToUpdate(update, specialistId);
            contextService.skipNextStep(context, Constants.ANY);
            return nextStepProcessor.processRequest(request);
        }

        return buildResponse(department, false, "Select available date");
    }

    private void updateContextData(Context context, Department department, boolean nextMonth) {
        List<String> navigation = context.getNavigation();
        navigation.remove(navigation.size() - 1);
        int numberOfCurrentMonth = DateUtils.getNumberOfCurrentMonth(department);
        int monthToAdd = nextMonth ? 1 : 0;
        ContextUtils.setStringParameter(context, Constants.MONTH, String.valueOf(numberOfCurrentMonth + monthToAdd));
        contextService.update(context);
    }
}
