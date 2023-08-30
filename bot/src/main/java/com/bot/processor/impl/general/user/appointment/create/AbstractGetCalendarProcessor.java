package com.bot.processor.impl.general.user.appointment.create;

import com.commons.model.Appointment;
import com.bot.model.BuildKeyboardRequest;
import com.bot.model.ButtonsType;
import com.bot.model.Context;
import com.bot.model.DatePickerRequest;
import com.bot.model.KeyBoardType;
import com.bot.model.MessageHolder;
import com.commons.service.IAppointmentService;
import com.bot.util.Constants;
import com.bot.util.ContextUtils;
import com.commons.utils.DateUtils;
import com.bot.util.MessageUtils;
import com.commons.model.CustomerService;
import com.commons.model.Department;
import com.commons.model.Specialist;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.utils.Pair;
import software.amazon.awssdk.utils.StringUtils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
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

    public List<MessageHolder> buildResponse(DatePickerRequest request) {
        Department department = request.getDepartment();
        Context context = request.getContext();
        boolean isNextMonth = request.isNextMonth();
        String message = request.getMessage();
        String selectedService = request.getSelectedService();
        String selectedSpecialist = request.getSelectedSpecialist();

        List<Specialist> specialists = department.getAvailableSpecialists();
        if (specialists.isEmpty()) {
            throw new RuntimeException("Specialists list can not be empty");
        }

        long startDate = DateUtils.nowZone(department);
        LocalDateTime endDateTime = LocalDate.now()
                .atStartOfDay()
                .with(TemporalAdjusters.lastDayOfMonth())
                .plusDays(1);
        long endDate = ZonedDateTime.of(endDateTime, ZoneId.of(department.getZone())).toEpochSecond();

        Month month = DateUtils.nowZoneDateTime(department).getMonth();
        if (isNextMonth) {
            startDate = endDate;

            LocalDateTime dateTimePlusMonth = endDateTime.plusMonths(1);
            endDate = ZonedDateTime.of(dateTimePlusMonth, ZoneId.of(department.getZone())).toEpochSecond();
            month = month.plus(1);
        }

        List<Appointment> allAppointments;
        if (StringUtils.isNotBlank(selectedSpecialist)) {
            allAppointments = appointmentService.getAppointmentsBySpecialist(department, selectedSpecialist, startDate, endDate);
        } else {
            allAppointments = appointmentService.getAppointmentsByDepartment(department, startDate, endDate);
        }
        allAppointments.removeIf(a -> specialists.stream().map(Specialist::getName).noneMatch(n -> n.equals(a.getSpecialist())));
        List<String> busyDayTitles = allAppointments.isEmpty()
                ? new ArrayList<>()
                : defineBusyDayTitles(allAppointments, department, month.getValue(), selectedService);
        if (!isNextMonth) {
            String currentDayTitle = getCurrentDayBusyTitle(department, selectedService);
            if (!"".equals(currentDayTitle)) {
                busyDayTitles.add(currentDayTitle);
            }
        }
        BuildKeyboardRequest commonsRequest = BuildKeyboardRequest.builder()
                .type(KeyBoardType.TWO_ROW)
                .buttonsMap(MessageUtils.buildButtons(List.of(), true))
                .build();
        String messageText = StringUtils.isEmpty(message) ? "Select date" : message;
        MessageHolder commonButtonsHolder = MessageUtils.holder(messageText, ButtonsType.KEYBOARD, commonsRequest);
        BuildKeyboardRequest datePickerRequest = BuildKeyboardRequest.builder()
                .type(KeyBoardType.TWO_ROW)
                .buttonsMap(MessageUtils.buildButtons(MessageUtils.commonButtons(busyDayTitles), false))
                .params(Map.of(
                        Constants.DEPARTMENT, department,
                        Constants.IS_NEXT_MONTH, isNextMonth,
                        Constants.CONTEXT, context))
                .build();
        MessageHolder datePicker = MessageUtils.holder(month.name(), ButtonsType.DATE_PICKER, datePickerRequest);
        return List.of(commonButtonsHolder, datePicker);
    }

    private String getCurrentDayBusyTitle(Department department, String serviceName) {
        long serviceDuration = getServiceDuration(department, serviceName);
        ZonedDateTime now = DateUtils.nowZoneDateTime(department);
        long finishDate = DateUtils.getPointOfDay(now.getMonthValue(), now.getDayOfMonth(), department.getEndWork(), department);
        long nowLong = DateUtils.nowZone(department);
        if ((finishDate - nowLong) < serviceDuration) {
            return String.valueOf(now.getDayOfMonth());
        }
        return "";
    }

    public List<String> defineBusyDayTitles(List<Appointment> allAppointments, Department department, int month,
                                             String serviceName) {
        List<Pair<String, List<Appointment>>> appointmentsBySpecialist = allAppointments.stream()
                .collect(Collectors.groupingBy(Appointment::getSpecialist))
                .entrySet().stream()
                .map(e -> Pair.of(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
        List<String> specialistsWithAppointments = appointmentsBySpecialist.stream()
                .map(Pair::left)
                .collect(Collectors.toList());

        if (StringUtils.isNotBlank(serviceName)) {
            List<Specialist> specialists = department.getAvailableSpecialists();
            for (Specialist specialist : specialists) {
                if (!specialistsWithAppointments.contains(specialist.getName())) {
                    appointmentsBySpecialist.add(Pair.of(specialist.getName(), List.of()));
                }
            }
        }

        Pair<String, List<Appointment>> firstPair = appointmentsBySpecialist.get(0);
        List<Integer> busyDays = getBusyDays(firstPair.right(), department, month, serviceName);
        if (!busyDays.isEmpty() && appointmentsBySpecialist.size() > 1) {
            busyDays.removeIf(bd -> appointmentsBySpecialist.stream()
                    .skip(1)
                    .anyMatch(pair -> isDayAvailableForSpecialist(bd, pair.right(), department, month, serviceName)));
        }
        return busyDays.stream().map(String::valueOf).collect(Collectors.toList());
    }

    private boolean isDayAvailableForSpecialist(int dayOfMonth, List<Appointment> appointments, Department department,
                                                int month, String serviceName) {
        if (appointments == null || appointments.size() == 0) {
            return true;
        }
        Map<Integer, List<Appointment>> appointmentsByDays = getAppointmentsByDays(appointments);
        List<Appointment> appointmentsForDay = appointmentsByDays.get(dayOfMonth);
        if (appointmentsForDay == null) {
            return true;
        }
        return freeSlotsAvailable(appointmentsForDay, dayOfMonth, department, month, serviceName);
    }

    private List<Integer> getBusyDays(List<Appointment> appointments, Department department, int month, String serviceName) {
        Map<Integer, List<Appointment>> appointmentsByDays = getAppointmentsByDays(appointments);
        List<Integer> busyDays = new ArrayList<>();
        for (Map.Entry<Integer, List<Appointment>> dailyAppointmentsEntry : appointmentsByDays.entrySet()) {
            Integer dayOfMonth = dailyAppointmentsEntry.getKey();
            boolean slotsAvailable = freeSlotsAvailable(dailyAppointmentsEntry.getValue(), dayOfMonth, department,
                    month, serviceName);
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
                                       int month, String serviceName) {
        long serviceDuration = getServiceDuration(department, serviceName);
        dailyAppointments.sort(Comparator.comparing(Appointment::getDate));
        List<Long> freeSlots = getFreeSlots(dailyAppointments, department, dayOfMonth, month);
        long finalServiceDuration = serviceDuration;
        return freeSlots.stream().anyMatch(slot -> slot >= finalServiceDuration);
    }

    private long getServiceDuration(Department department, String serviceName) {
        long serviceDuration = 3600L;
        if (StringUtils.isNotBlank(serviceName)) {
            CustomerService service = department.getServices().stream()
                    .filter(s -> serviceName.equals(s.getName()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Service was not found: " + serviceName));
            serviceDuration = service.getDuration() * 60L;
        }
        return serviceDuration;
    }

    private List<Long> getFreeSlots(List<Appointment> appointments, Department department, int dayOfMonth, int month) {
        long nowLong = DateUtils.nowZone(department);
        long finish = DateUtils.getPointOfDay(month, dayOfMonth, department.getEndWork(), department);
        List<Long> result = new ArrayList<>();
        long currentPoint = DateUtils.getPointOfDay(month, dayOfMonth, department.getStartWork(), department);
        for (Appointment appointment : appointments) {
            long freeSlot = appointment.getDate() - currentPoint;
            if (currentPoint > nowLong) {
                result.add(freeSlot);
            }
            currentPoint = appointment.getDate() + appointment.getDuration() * 60L;
        }
        result.add(finish - currentPoint);
        return result;
    }

    protected void updateContextData(Context context, Department department, boolean nextMonth) {
        int numberOfCurrentMonth = DateUtils.getNumberOfCurrentMonth(department);
        int monthToAdd = nextMonth ? 1 : 0;
        context.getParams().put(Constants.MONTH, numberOfCurrentMonth + monthToAdd);
        ContextUtils.resetLocationToPreviousStep(context);
    }
}
