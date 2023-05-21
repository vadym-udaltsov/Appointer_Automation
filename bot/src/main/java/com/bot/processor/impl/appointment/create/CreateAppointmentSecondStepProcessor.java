package com.bot.processor.impl.appointment.create;

import com.bot.model.BuildKeyboardRequest;
import com.bot.model.Button;
import com.bot.model.ButtonsType;
import com.bot.model.Context;
import com.bot.model.FreeSlot;
import com.bot.model.KeyBoardType;
import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.bot.service.IAppointmentService;
import com.bot.service.IContextService;
import com.bot.util.Constants;
import com.bot.util.ContextUtils;
import com.bot.util.DateUtils;
import com.bot.util.MessageUtils;
import com.commons.model.CustomerService;
import com.commons.model.Department;
import com.commons.model.Specialist;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.bcel.Const;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
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
            return buildResponse(department, true, "", context);
        }
        if (Constants.CURRENT_MONTH.equals(text)) {
            updateContextData(context, department, false);
            return buildResponse(department, false, "", context);
        }

        if (Constants.BACK.equals(text)) {
            text = ContextUtils.getStringParam(context, Constants.SELECTED_DAY);
        }

        List<String> availableDates = (List<String>) context.getParams().get(Constants.AVAILABLE_DATES);
        if (!availableDates.contains(text) && !Constants.BACK.equals(text)) {
            updateContextData(context, department, false);
            return buildResponse(department, false, "Select available date", context);
        }

        List<Specialist> availableSpecialists = department.getAvailableSpecialists();
        List<String> specialistNames = availableSpecialists.stream()
                .map(Specialist::getName)
                .collect(Collectors.toList());

        int dayNumber = Integer.parseInt(text);
        int monthNumber = Integer.parseInt(ContextUtils.getStringParam(context, Constants.MONTH));

        Map<String, List<FreeSlot>> slotsBySpecialists = appointmentService.getFreeSlotsBySpecialists(specialistNames, department,
                monthNumber, dayNumber);
        CustomerService shortestService = department.getServices().stream()
                .min(Comparator.comparingInt(CustomerService::getDuration))
                .orElseThrow(() -> new RuntimeException("Could not find service with shortest duration"));
        List<String> filteredSpecialists = slotsBySpecialists.entrySet().stream()
                .filter(e -> e.getValue().stream()
                        .anyMatch(s -> s.getDurationSec() >= shortestService.getDuration() * 60L))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        ContextUtils.setStringParameter(context, Constants.SELECTED_DAY, String.valueOf(dayNumber));
        ContextUtils.putSlotsToContextParams(slotsBySpecialists, context);
        context.getParams().put(Constants.AVAILABLE_SPECIALISTS, filteredSpecialists);

        if (specialistNames.size() == 1) {
            String specialistId = specialistNames.get(0);
            MessageUtils.setTextToUpdate(update, specialistId);
            contextService.skipNextStep(context, Constants.ANY);
            return nextStepProcessor.processRequest(request);
        }

        contextService.updateContext(context);

        BuildKeyboardRequest holderRequest = MessageUtils.getHolderRequest(filteredSpecialists);
        MessageHolder holder = MessageUtils.holder("Select specialist", ButtonsType.KEYBOARD, holderRequest);
        return List.of(holder);
    }

    private void updateContextData(Context context, Department department, boolean nextMonth) {
        int numberOfCurrentMonth = DateUtils.getNumberOfCurrentMonth(department);
        int monthToAdd = nextMonth ? 1 : 0;
        ContextUtils.setStringParameter(context, Constants.MONTH, String.valueOf(numberOfCurrentMonth + monthToAdd));
        contextService.setPreviousStep(context);
    }
}
