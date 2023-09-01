package com.bot.processor.impl.general.admin.dayoff;

import com.bot.model.BuildKeyboardRequest;
import com.bot.model.ButtonsType;
import com.bot.model.Context;
import com.bot.model.KeyBoardType;
import com.bot.model.MessageHolder;
import com.bot.util.Constants;
import com.bot.util.ContextUtils;
import com.bot.util.MessageUtils;
import com.commons.model.Appointment;
import com.commons.model.Department;
import com.commons.service.IAppointmentService;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class AbstractGetCalendarPeriodDayOff {

    protected final IAppointmentService appointmentService;

    protected List<MessageHolder> buildResponse(Department department, Context context, int month, String message,
                                                int year) {
        BuildKeyboardRequest commonsRequest = BuildKeyboardRequest.builder()
                .type(KeyBoardType.TWO_ROW)
                .buttonsMap(MessageUtils.buildButtons(List.of(), true))
                .build();

        MessageHolder commonButtonsHolder = MessageUtils.holder(message, ButtonsType.KEYBOARD, commonsRequest);

        context.getParams().put(Constants.SELECTED_MONTH, month);
        context.getParams().put(Constants.SELECTED_YEAR, year);
        String selectedSpecialist = ContextUtils.getStringParam(context, Constants.SELECTED_SPEC);
        List<String> dayTitles = getAppointmentTitles(department, selectedSpecialist, year, month);
        BuildKeyboardRequest datePickerRequest = BuildKeyboardRequest.builder()
                .type(KeyBoardType.TWO_ROW)
                .buttonsMap(MessageUtils.buildButtons(MessageUtils.commonButtons(List.of()), false))
                .params(Map.of(
                        Constants.DEPARTMENT, department,
                        Constants.SELECTED_MONTH, month,
                        Constants.SELECTED_YEAR, year,
                        "dayOffs", dayTitles,
                        Constants.CONTEXT, context))
                .build();

        MessageHolder datePicker = MessageUtils.holder(Month.of(month).name(), ButtonsType.DATE_PICKER_PERIOD_DAY_OFF,
                datePickerRequest);
        return List.of(commonButtonsHolder, datePicker);
    }

    protected List<String> getAppointmentTitles(Department department, String selectedSpecialist, int year, int month) {
        long start = LocalDate.of(year, month, 1).atStartOfDay(ZoneId.of(department.getZone())).toEpochSecond();
        long end = LocalDate.of(year, month, 1).atStartOfDay(ZoneId.of(department.getZone())).plusMonths(1).toEpochSecond();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd");
        List<Appointment> appointments = appointmentService.getAppointmentsBySpecialist(department, selectedSpecialist, start, end);
        return appointments.stream()
                .filter(a -> Constants.DAY_OFF.equals(a.getService()))
                .map(a -> {
                    ZonedDateTime zdt = ZonedDateTime.ofInstant(Instant.ofEpochSecond(a.getDate()), ZoneId.of(department.getZone()));
                    return zdt.format(formatter);
                })
                .collect(Collectors.toList());
    }
}
