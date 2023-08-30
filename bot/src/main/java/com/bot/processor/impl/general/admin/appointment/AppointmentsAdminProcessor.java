package com.bot.processor.impl.general.admin.appointment;

import com.bot.model.BuildKeyboardRequest;
import com.bot.model.ButtonsType;
import com.bot.model.Context;
import com.bot.model.KeyBoardType;
import com.bot.model.LString;
import com.bot.model.MessageHolder;
import com.bot.model.MessageTemplate;
import com.bot.model.ProcessRequest;
import com.bot.service.IContextService;
import com.bot.util.Constants;
import com.bot.util.ContextUtils;
import com.bot.util.MessageUtils;
import com.commons.model.Appointment;
import com.commons.model.Department;
import com.commons.service.IAppointmentService;
import com.commons.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.Instant;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class AppointmentsAdminProcessor {

    private final IAppointmentService appointmentService;
    private final IContextService contextService;

    protected List<MessageHolder> buildResponse(ProcessRequest request) {
        Update update = request.getUpdate();
        Department department = request.getDepartment();
        Context context = request.getContext();
        String selectedDay = MessageUtils.getTextFromUpdate(update);
        if (Constants.NEXT_MONTH.equals(selectedDay)) {
            return buildAnotherMonthResponse(request, true);
        }
        if (Constants.CURRENT_MONTH.equals(selectedDay)) {
            return buildAnotherMonthResponse(request, false);
        }
        int month = ContextUtils.getIntParam(context, Constants.MONTH);
        long startOfDay = DateUtils.getStartOrEndOfDay(month, Integer.parseInt(selectedDay), false, department);
        long endOfDay = DateUtils.getStartOrEndOfDay(month, Integer.parseInt(selectedDay), true, department);

        List<Appointment> appointments = getAppointmentSupplier(request, startOfDay, endOfDay).get();
        return getHolders(appointments, context, department);
    }

    protected void resetLocationToDashboard(Context context) {
        ContextUtils.resetLocationToDashboard(context);
    }

    protected Supplier<List<Appointment>> getAppointmentSupplier(ProcessRequest request, long start, long finish) {
        Department department = request.getDepartment();
        return () -> appointmentService.getAppointmentsByDepartment(department, start, finish)
                .stream()
                .filter(a -> !Constants.DAY_OFF.equals(a.getService()))
                .collect(Collectors.toList());
    }

    protected List<MessageHolder> getHolders(List<Appointment> appointments, Context context, Department department) {
        List<LString> messagesToLocalize = new ArrayList<>();
        if (appointments.size() == 0) {
            ContextUtils.resetLocationToStep(context, Constants.Processors.START_APP_DASH);
            return MessageUtils.buildCustomKeyboardHolders(Constants.Messages.NO_APP_FOR_DATE, Constants.VIEW_ADMIN_APP_BUTTONS,
                    KeyBoardType.THREE_ROW, true);
        }
        String date = getReportDate(appointments, department);
        messagesToLocalize.add(LString.builder().title(Constants.Messages.APP_FOR_DATE)
                .placeholders(Map.of("date", date)).build());
        messagesToLocalize.add(LString.empty());
        Map<String, List<Appointment>> specialistMap = appointments.stream()
                .collect(Collectors.groupingBy(Appointment::getSpecialist));
        Map<Long, Context> contextMap = contextService.getContextListByAppointments(appointments)
                .stream()
                .collect(Collectors.toMap(Context::getUserId, Function.identity()));
        for (Map.Entry<String, List<Appointment>> entry : specialistMap.entrySet()) {
            messagesToLocalize.add(LString.builder().title(Constants.Messages.APP_SPECIALIST)
                    .placeholders(Map.of("specialist", entry.getKey())).build());
            messagesToLocalize.add(LString.empty());
            for (Appointment appointment : entry.getValue()) {
                Context userContext = contextMap.get(appointment.getUserId());
                MessageUtils.fillMessagesToLocalize(messagesToLocalize, appointment, userContext,
                        MessageTemplate.APPOINTMENT_TIME_SERVICE_CLIENT, department);
                messagesToLocalize.add(LString.empty());
            }
        }
        ContextUtils.resetLocationToStep(context, Constants.Processors.START_APP_DASH);
        messagesToLocalize.add(LString.builder().title(Constants.Messages.SELECT_ACTION).build());
        return MessageUtils.buildCustomKeyboardHolders(Constants.Messages.NO_APP_FOR_DATE, Constants.VIEW_ADMIN_APP_BUTTONS,
                KeyBoardType.THREE_ROW, messagesToLocalize, true);
    }

    protected List<MessageHolder> getNoAppointmentsMessage(Context context) {
        ContextUtils.resetLocationToStep(context, Constants.Processors.START_APP_DASH);
        return MessageUtils.buildCustomKeyboardHolders(Constants.Messages.NO_APP_FOR_DATE, Constants.VIEW_ADMIN_APP_BUTTONS,
                KeyBoardType.THREE_ROW, true);
    }

    private List<MessageHolder> buildAnotherMonthResponse(ProcessRequest request, boolean isNextMonth) {
        Department department = request.getDepartment();
        Context context = request.getContext();
        long startDate = DateUtils.getStartOfMonthDate(department, isNextMonth);
        long endDate = DateUtils.getEndOfMonthDate(department, isNextMonth);
        List<Appointment> appointments = getAppointmentSupplier(request, startDate, endDate).get();
        Set<String> appointmentDays = appointments.stream()
                .map(a -> DateUtils.getDayTitle(a.getDate(), department))
                .collect(Collectors.toSet());
        updateContextData(context, department, isNextMonth);
        BuildKeyboardRequest datePickerRequest = BuildKeyboardRequest.builder()
                .params(Map.of(
                        Constants.IS_NEXT_MONTH, isNextMonth,
                        Constants.USER_APPOINTMENTS, appointmentDays))
                .build();
        Month month = DateUtils.nowZoneDateTime(department).getMonth().plus(isNextMonth ? 1 : 0);
//        Month month = LocalDate.now().getMonth().plus(isNextMonth ? 1 : 0);
        MessageHolder datePicker = MessageUtils.holder(month.name(), ButtonsType.DATE_PICKER_MY_APP, datePickerRequest);
        return List.of(datePicker);
    }

    protected void updateContextData(Context context, Department department, boolean nextMonth) {
        int numberOfCurrentMonth = DateUtils.getNumberOfCurrentMonth(department);
        int monthToAdd = nextMonth ? 1 : 0;
        context.getParams().put(Constants.MONTH, (numberOfCurrentMonth + monthToAdd));
        ContextUtils.resetLocationToPreviousStep(context);
    }

    private String getReportDate(List<Appointment> appointments, Department department) {
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(Instant.ofEpochSecond(appointments.get(0).getDate()), ZoneId.of(department.getZone()));
//        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(appointments.get(0).getDate()), ZoneId.systemDefault());
        return zonedDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}
