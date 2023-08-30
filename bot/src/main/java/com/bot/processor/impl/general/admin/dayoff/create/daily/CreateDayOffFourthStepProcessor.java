package com.bot.processor.impl.general.admin.dayoff.create.daily;

import com.bot.model.Context;
import com.commons.model.FreeSlot;
import com.bot.model.KeyBoardType;
import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.commons.service.IAppointmentService;
import com.bot.service.IContextService;
import com.bot.util.Constants;
import com.bot.util.ContextUtils;
import com.commons.utils.DateUtils;
import com.bot.util.MessageUtils;
import com.commons.model.Department;
import com.commons.utils.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CreateDayOffFourthStepProcessor extends CreateDayOffProcessor implements IProcessor {

    public CreateDayOffFourthStepProcessor(IAppointmentService appointmentService, IContextService contextService) {
        super(appointmentService, contextService);
    }

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        Department department = request.getDepartment();
        Update update = request.getUpdate();
        Context context = request.getContext();

        int month = ContextUtils.getIntParam(context, Constants.MONTH);
        int day = ContextUtils.getIntParam(context, Constants.SELECTED_DAY);
        String specialist = ContextUtils.getStringParam(context, Constants.SELECTED_SPEC);
        int year = DateUtils.getNumberOfCurrentYear(department);

        String selectedTimeTitle = MessageUtils.getTextFromUpdate(update);
        List<String> availableSlotTitles = (List<String>) context.getParams().get(Constants.AVAILABLE_SLOT_TITLES);
        if (!availableSlotTitles.contains(selectedTimeTitle)) {
            ContextUtils.resetLocationToPreviousStep(context);
            return MessageUtils.buildCustomKeyboardHolders("Select time from proposed", availableSlotTitles,
                    KeyBoardType.FOUR_ROW, true);
        }

        if (Constants.WHOLE_DAY.equals(selectedTimeTitle)) {
            int duration = (department.getEndWork() - department.getStartWork()) * 60;
            return createDayOff(department, context, specialist, year, month, day, department.getStartWork(),
                    0, duration);
        }
        List<String> availableSlots = (List<String>) context.getParams().get(Constants.AVAILABLE_SLOTS);
        List<FreeSlot> convertedSlots = availableSlots.stream()
                .map(s -> JsonUtils.parseStringToObject(s, new TypeReference<FreeSlot>() {
                }))
                .collect(Collectors.toList());
        String[] timeParts = selectedTimeTitle.split(":");
        int hour = Integer.parseInt(timeParts[0]);
        int minute = Integer.parseInt(timeParts[1]);
        LocalDateTime dayOffDateTime = LocalDateTime.of(year, month, day, hour, minute);
        long dayOffStart = ZonedDateTime.of(dayOffDateTime, ZoneId.of(department.getZone())).toEpochSecond();
        FreeSlot slot = defineSlot(convertedSlots, dayOffStart);
        List<String> durationTitles = getDurationTitles(slot, dayOffStart);
        context.getParams().put(Constants.AVAILABLE_DURATIONS, durationTitles);
        context.getParams().put(Constants.SELECTED_HOUR, hour);
        context.getParams().put(Constants.SELECTED_MINUTE, minute);
        return MessageUtils.buildCustomKeyboardHolders("Select duration", durationTitles, KeyBoardType.VERTICAL,
                true);
    }

    private List<String> getDurationTitles(FreeSlot slot, long dayOffStart) {
        long startPoint = slot.getStartPoint();
        long durationSec = slot.getDurationSec();
        long shift = dayOffStart - startPoint;
        if (shift < 0) {
            throw new RuntimeException("Start of day off earlier than start of free slot");
        }
        long periodsCount = (durationSec - shift) / 1800;
        List<String> titles = new ArrayList<>();
        for (int i = 0; i < periodsCount; i++) {
            titles.add(Constants.Numbers.PERIOD_TITLES.get(i));
        }
        return titles;
    }

    private FreeSlot defineSlot(List<FreeSlot> convertedSlots, long dayOffStart) {
        for (FreeSlot slot : convertedSlots) {
            long start = slot.getStartPoint();
            long end = slot.getStartPoint() + slot.getDurationSec();
            if (start <= dayOffStart && end > dayOffStart) {
                return slot;
            }
        }
        throw new RuntimeException("Slot not found for start day off: " + dayOffStart);
    }
}
