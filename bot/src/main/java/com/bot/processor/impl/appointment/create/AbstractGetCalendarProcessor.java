package com.bot.processor.impl.appointment.create;

import com.bot.model.Appointment;
import com.bot.model.BuildKeyboardRequest;
import com.bot.model.ButtonsType;
import com.bot.model.KeyBoardType;
import com.bot.model.MessageHolder;
import com.bot.service.IAppointmentService;
import com.bot.util.DateUtils;
import com.bot.util.MessageUtils;
import com.commons.model.CustomerService;
import com.commons.model.Department;
import com.commons.model.Specialist;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class AbstractGetCalendarProcessor {

    private final IAppointmentService appointmentService;

    public List<MessageHolder> buildResponse(Department department, boolean isNextMonth, String message) {
        List<Specialist> specialists = department.getAvailableSpecialists();
        if (specialists.isEmpty()) {
            throw new RuntimeException("Specialists list can not be empty");
        }
        List<String> specialistIds = specialists.stream().map(Specialist::getId).collect(Collectors.toList());

        long startDate = DateUtils.now(department);
        LocalDateTime endDateTime = LocalDate.now()
                .atStartOfDay()
                .with(TemporalAdjusters.lastDayOfMonth())
                .plusDays(1);
        long endDate = endDateTime
                .toEpochSecond(ZoneOffset.ofHours(-department.getZoneOffset()));

        Month month = LocalDate.now().getMonth();
        if (isNextMonth) {
            startDate = endDate;
            endDate = endDateTime.plusMonths(1).toEpochSecond(ZoneOffset.ofHours(-department.getZoneOffset()));
            month = month.plus(1);
        }

        List<Appointment> allAppointments = appointmentService.getAppointmentsBySpecialists(specialistIds, startDate, endDate);

        List<String> busyDayTitles = allAppointments.isEmpty() ? List.of() : defineBusyDayTitles(allAppointments,
                department, month.getValue());

        BuildKeyboardRequest commonsRequest = BuildKeyboardRequest.builder()
                .type(KeyBoardType.TWO_ROW)
                .buttonsMap(MessageUtils.buildButtons(List.of(), true))
                .build();
        String messageText = StringUtils.isEmpty(message) ? "Select date" : message;
        MessageHolder commonButtonsHolder = MessageUtils.holder(messageText, ButtonsType.KEYBOARD, commonsRequest);
        BuildKeyboardRequest datePickerRequest = BuildKeyboardRequest.builder()
                .type(KeyBoardType.TWO_ROW)
                .buttonsMap(MessageUtils.buildButtons(MessageUtils.commonButtons(busyDayTitles), false))
                .params(Map.of("department", department, "isNextMonth", isNextMonth))
                .build();
        MessageHolder datePicker = MessageUtils.holder(month.name(), ButtonsType.DATE_PICKER, datePickerRequest);
        return List.of(commonButtonsHolder, datePicker);
    }

    private List<String> defineBusyDayTitles(List<Appointment> allAppointments, Department department, int month) {
        List<Pair<String, List<Appointment>>> appointmentsBySpecialist = allAppointments.stream()
                .collect(Collectors.groupingBy(Appointment::getSpecialist))
                .entrySet().stream()
                .map(e -> Pair.of(e.getKey(), e.getValue()))
                .collect(Collectors.toList());

        Pair<String, List<Appointment>> firstPair = appointmentsBySpecialist.get(0);
        List<Integer> busyDays = getBusyDays(firstPair.getValue(), department, month);
        if (!busyDays.isEmpty() && appointmentsBySpecialist.size() > 1) {
            busyDays.removeIf(bd -> appointmentsBySpecialist.stream()
                    .skip(1)
                    .allMatch(pair -> isDayBusyForSpecialist(bd, pair.getValue(), department, month)));
        }
        return busyDays.stream().map(String::valueOf).collect(Collectors.toList());
    }

    private boolean isDayBusyForSpecialist(int dayOfMonth, List<Appointment> appointments, Department department, int month) {
        Map<Integer, List<Appointment>> appointmentsByDays = getAppointmentsByDays(appointments);
        List<Appointment> appointmentsForDay = appointmentsByDays.get(dayOfMonth);
        if (appointmentsForDay == null) {
            return false;
        }
        return freeSlotsAvailable(appointmentsForDay, dayOfMonth, department, month);
    }

    private List<Integer> getBusyDays(List<Appointment> appointments, Department department, int month) {
        Map<Integer, List<Appointment>> appointmentsByDays = getAppointmentsByDays(appointments);
        List<Integer> busyDays = new ArrayList<>();
        for (Map.Entry<Integer, List<Appointment>> dailyAppointmentsEntry : appointmentsByDays.entrySet()) {
            Integer dayOfMonth = dailyAppointmentsEntry.getKey();
            boolean slotsAvailable = freeSlotsAvailable(dailyAppointmentsEntry.getValue(), dayOfMonth, department, month);
            if (!slotsAvailable) {
                busyDays.add(dayOfMonth);
            }
        }
        return busyDays;
    }

    private Map<Integer, List<Appointment>> getAppointmentsByDays(List<Appointment> appointments) {
        return appointments.stream().collect(Collectors.groupingBy(app -> {
            long date = app.getDate();
            Instant instant = Instant.ofEpochSecond(date);
            ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, ZoneOffset.UTC);
            return zonedDateTime.getDayOfMonth();
        }));
    }

    private boolean freeSlotsAvailable(List<Appointment> dailyAppointments, int dayOfMonth, Department department,
                                       int month) {
        CustomerService shortestService = department.getServices().stream()
                .min(Comparator.comparingInt(CustomerService::getDuration))
                .orElseThrow(() -> new RuntimeException("Shortest service was not found"));
        dailyAppointments.sort(Comparator.comparing(Appointment::getDate));
        List<Long> freeSlots = getFreeSlots(dailyAppointments, department, dayOfMonth, month);
        return freeSlots.stream().anyMatch(slot -> slot > shortestService.getDuration() * 60L);
    }

    private List<Long> getFreeSlots(List<Appointment> appointments, Department department, int dayOfMonth, int month) {
        LocalDateTime now = LocalDateTime.now();
        long nowLong = now.toEpochSecond(ZoneOffset.ofHours(-department.getZoneOffset()));
        long finish = DateUtils.getPointOfDay(month, dayOfMonth, department.getEndWork());
        List<Long> result = new ArrayList<>();
        Map<String, Integer> durationsByServices = department.getServices().stream()
                .collect(Collectors.toMap(CustomerService::getName, CustomerService::getDuration));
        long currentPoint = DateUtils.getPointOfDay(month, dayOfMonth, department.getStartWork());
        for (Appointment appointment : appointments) {
            long freeSlot = appointment.getDate() - currentPoint;
            if (currentPoint > nowLong) {
                result.add(freeSlot);
            }
            currentPoint = appointment.getDate() + durationsByServices.get(appointment.getService()) * 60L;
        }
        result.add(finish - currentPoint);
        return result;
    }
}
