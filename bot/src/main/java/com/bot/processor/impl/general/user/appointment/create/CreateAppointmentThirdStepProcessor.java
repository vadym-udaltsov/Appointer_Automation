package com.bot.processor.impl.general.user.appointment.create;

import com.bot.model.BuildKeyboardRequest;
import com.bot.model.ButtonsType;
import com.bot.model.Context;
import com.bot.model.DatePickerRequest;
import com.bot.model.FreeSlot;
import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.bot.service.IAppointmentService;
import com.bot.util.Constants;
import com.bot.util.ContextUtils;
import com.bot.util.MessageUtils;
import com.commons.model.CustomerService;
import com.commons.model.Department;
import com.commons.model.Specialist;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CreateAppointmentThirdStepProcessor extends AbstractGetCalendarProcessor implements IProcessor {

    private final IAppointmentService appointmentService;
    private final IProcessor nextStepProcessor;
    private final IProcessor previousProcessor;

    public CreateAppointmentThirdStepProcessor(IAppointmentService appointmentService, IProcessor nextStepProcessor,
                                               IProcessor previousProcessor) {
        super(appointmentService);
        this.appointmentService = appointmentService;
        this.nextStepProcessor = nextStepProcessor;
        this.previousProcessor = previousProcessor;
    }

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        Department department = request.getDepartment();
        Update update = request.getUpdate();
        Context context = request.getContext();

        String selectedDay = MessageUtils.getTextFromUpdate(update);
        String selectedService = ContextUtils.getStringParam(context, Constants.SELECTED_SERVICE);
        int monthNumber = Integer.parseInt(ContextUtils.getStringParam(context, Constants.MONTH));
        DatePickerRequest datePickerRequest = DatePickerRequest.builder()
                .department(department)
                .message("")
                .context(context)
                .selectedService(selectedService)
                .build();
        if (Constants.NEXT_MONTH.equals(selectedDay)) {
            updateContextData(context, department, true);
            datePickerRequest.setNextMonth(true);
            return buildResponse(datePickerRequest);
        }
        if (Constants.CURRENT_MONTH.equals(selectedDay)) {
            updateContextData(context, department, false);
            datePickerRequest.setNextMonth(false);
            return buildResponse(datePickerRequest);
        }

        List<String> availableDates = (List<String>) context.getParams().get(Constants.AVAILABLE_DATES);
        if (!availableDates.contains(selectedDay) && !Constants.BACK.equals(selectedDay)) {
            updateContextData(context, department, false);
            datePickerRequest.setNextMonth(false);
            datePickerRequest.setMessage("Select available date");
            return buildResponse(datePickerRequest);
        }

        List<Specialist> availableSpecialists = department.getAvailableSpecialists();
        List<String> specialistNames = availableSpecialists.stream()
                .map(Specialist::getName)
                .collect(Collectors.toList());

        if (Constants.BACK.equals(selectedDay)) {
            if (availableSpecialists.size() == 1) {
                ContextUtils.setPreviousStep(context);
                return previousProcessor.processRequest(request);
            }
            selectedDay = ContextUtils.getStringParam(context, Constants.SELECTED_DAY);
        }

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
        MessageHolder holder = MessageUtils.holder(Constants.Messages.SELECT_SPECIALIST, ButtonsType.KEYBOARD, holderRequest);
        return List.of(holder);
    }
}
