package com.bot.processor.impl.general.admin.dayoff.create;

import com.bot.model.BuildKeyboardRequest;
import com.bot.model.ButtonsType;
import com.bot.model.Context;
import com.bot.model.DatePickerRequest;
import com.bot.model.FreeSlot;
import com.bot.model.KeyBoardType;
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
import com.commons.utils.JsonUtils;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CreateDayOffThirdStepProcessor extends AbstractGetCalendarProcessor implements IProcessor {

    private final IAppointmentService appointmentService;

    public CreateDayOffThirdStepProcessor(IAppointmentService appointmentService) {
        super(appointmentService);
        this.appointmentService = appointmentService;
    }

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        Department department = request.getDepartment();
        Update update = request.getUpdate();
        Context context = request.getContext();

        String selectedDay = MessageUtils.getTextFromUpdate(update);

        int monthNumber = ContextUtils.getIntParam(context, Constants.MONTH);
        String selectedSpecialist = ContextUtils.getStringParam(context, Constants.SELECTED_SPEC);

        DatePickerRequest datePickerRequest = DatePickerRequest.builder()
                .department(department)
                .message("")
                .context(context)
                .selectedSpecialist(selectedSpecialist)
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
        if (Constants.BACK.equals(selectedDay)) {
            selectedDay = String.valueOf(ContextUtils.getIntParam(context, Constants.SELECTED_DAY));
        }
        List<String> availableDates = (List<String>) context.getParams().get(Constants.AVAILABLE_DATES);
        if (!availableDates.contains(selectedDay) && !Constants.BACK.equals(selectedDay)) {
            updateContextData(context, department, false);
            datePickerRequest.setNextMonth(false);
            datePickerRequest.setMessage("Select available date");
            return buildResponse(datePickerRequest);
        }
        int dayNumber = Integer.parseInt(selectedDay);
        List<FreeSlot> freeSlots = appointmentService.getFreeSlotsBySpecialist(department, selectedSpecialist,
                monthNumber, dayNumber);
        freeSlots.sort(Comparator.comparingLong(FreeSlot::getStartPoint));

        List<String> slotTitles = new ArrayList<>();
        boolean wholeDayAvailable = DateUtils.isWholeDayAvailable(department, freeSlots.get(0));
        if (wholeDayAvailable) {
            slotTitles = DateUtils.getSlotTitles(freeSlots.get(0), 3600L, 30);
            slotTitles.add(Constants.WHOLE_DAY);
        } else {
            for (FreeSlot slot : freeSlots) {
                slotTitles.addAll(DateUtils.getSlotTitles(slot, 3600L, 15));
            }
        }
        List<String> convertedSlots = freeSlots.stream()
                .map(JsonUtils::convertObjectToString)
                .collect(Collectors.toList());
        context.getParams().put(Constants.AVAILABLE_SLOTS, convertedSlots);
        context.getParams().put(Constants.AVAILABLE_SLOT_TITLES, slotTitles);
        context.getParams().put(Constants.SELECTED_DAY, dayNumber);
        BuildKeyboardRequest holderRequest = BuildKeyboardRequest.builder()
                .type(KeyBoardType.FOUR_ROW)
                .buttonsMap(MessageUtils.buildButtons(MessageUtils.commonButtons(slotTitles), true))
                .build();
        MessageHolder holder = MessageUtils.holder("Select time", ButtonsType.KEYBOARD, holderRequest);
        return List.of(holder);
    }
}
