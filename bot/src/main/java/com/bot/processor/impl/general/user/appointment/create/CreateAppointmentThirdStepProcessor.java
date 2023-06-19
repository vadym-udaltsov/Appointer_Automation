package com.bot.processor.impl.general.user.appointment.create;

import com.bot.model.BuildKeyboardRequest;
import com.bot.model.ButtonsType;
import com.bot.model.Context;
import com.bot.model.FreeSlot;
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
import com.commons.model.Specialist;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class CreateAppointmentThirdStepProcessor extends AbstractGetCalendarProcessor implements IProcessor {

    private final IAppointmentService appointmentService;
    private final IProcessor nextStepProcessor;

    public CreateAppointmentThirdStepProcessor(IAppointmentService appointmentService, IProcessor nextStepProcessor) {
        super(appointmentService);
        this.appointmentService = appointmentService;
        this.nextStepProcessor = nextStepProcessor;
    }

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        Department department = request.getDepartment();
        Update update = request.getUpdate();
        Context context = request.getContext();

        String selectedDay = MessageUtils.getTextFromUpdate(update);
        String selectedService = ContextUtils.getStringParam(context, Constants.SELECTED_SERVICE);
        int monthNumber = Integer.parseInt(ContextUtils.getStringParam(context, Constants.MONTH));

        if (Constants.NEXT_MONTH.equals(selectedDay)) {
            updateContextData(context, department, true);
            return buildResponse(department, true, "", context, selectedService);
        }
        if (Constants.CURRENT_MONTH.equals(selectedDay)) {
            updateContextData(context, department, false);
            return buildResponse(department, false, "", context, selectedService);
        }

        List<String> availableDates = (List<String>) context.getParams().get(Constants.AVAILABLE_DATES);
        if (!availableDates.contains(selectedDay) && !Constants.BACK.equals(selectedDay)) {
            updateContextData(context, department, false);
            return buildResponse(department, false, "Select available date", context, selectedService);
        }

        if (Constants.BACK.equals(selectedDay)) {
            selectedDay = ContextUtils.getStringParam(context, Constants.SELECTED_DAY);
        }

        List<Specialist> availableSpecialists = department.getAvailableSpecialists();
        List<String> specialistNames = availableSpecialists.stream()
                .map(Specialist::getName)
                .collect(Collectors.toList());

        int dayNumber = Integer.parseInt(selectedDay);

        Map<String, List<FreeSlot>> slotsBySpecialists = appointmentService.getFreeSlotsByDepartment(department,
                monthNumber, dayNumber);
        Integer serviceDurationMinutes = department.getServices().stream()
                .filter(cs -> selectedService.equals(cs.getName()))
                .findFirst()
                .map(CustomerService::getDuration)
                .orElseThrow(() -> new RuntimeException("Could not find service with name:" + selectedService));

        List<String> filteredSpecialists = slotsBySpecialists.entrySet().stream()
                .filter(e -> e.getValue().stream()
                        .anyMatch(s -> s.getDurationSec() >= serviceDurationMinutes * 60L))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        ContextUtils.setStringParameter(context, Constants.SELECTED_DAY, String.valueOf(dayNumber));
        ContextUtils.putSlotsToContextParams(slotsBySpecialists, context);
        context.getParams().put(Constants.AVAILABLE_SPECIALISTS, filteredSpecialists);

        if (specialistNames.size() == 1) {
            String specialistName = specialistNames.get(0);
            MessageUtils.setTextToUpdate(update, specialistName);
            ContextUtils.addNextStepToLocation(context, Constants.ANY, department);
            return nextStepProcessor.processRequest(request);
        }

        BuildKeyboardRequest holderRequest = MessageUtils.buildVerticalHolderRequestWithCommon(filteredSpecialists);
        MessageHolder holder = MessageUtils.holder("Select specialist", ButtonsType.KEYBOARD, holderRequest);
        return List.of(holder);
    }

    private void updateContextData(Context context, Department department, boolean nextMonth) {
        int numberOfCurrentMonth = DateUtils.getNumberOfCurrentMonth(department);
        int monthToAdd = nextMonth ? 1 : 0;
        ContextUtils.setStringParameter(context, Constants.MONTH, String.valueOf(numberOfCurrentMonth + monthToAdd));
        ContextUtils.setPreviousStep(context);
    }
}
