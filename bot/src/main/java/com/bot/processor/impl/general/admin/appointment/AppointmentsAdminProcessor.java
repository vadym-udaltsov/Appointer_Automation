package com.bot.processor.impl.general.admin.appointment;

import com.commons.model.Appointment;
import com.bot.model.BuildKeyboardRequest;
import com.bot.model.ButtonsType;
import com.bot.model.Context;
import com.bot.model.LString;
import com.bot.model.MessageHolder;
import com.bot.model.MessageTemplate;
import com.bot.model.ProcessRequest;
import com.commons.service.IAppointmentService;
import com.bot.service.IContextService;
import com.bot.util.Constants;
import com.bot.util.ContextUtils;
import com.commons.utils.DateUtils;
import com.bot.util.MessageUtils;
import com.commons.model.Department;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
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
        long startOfDay = DateUtils.getStartOrEndOfDay(month, Integer.parseInt(selectedDay), false);
        long endOfDay = DateUtils.getStartOrEndOfDay(month, Integer.parseInt(selectedDay), true);

        ContextUtils.resetLocationToDashboard(context);
        List<Appointment> appointments = getAppointmentSupplier(request, startOfDay, endOfDay).get();
        String strategyKey = ContextUtils.getStrategyKey(context, department);
        return getHolders(appointments, strategyKey);
    }

    protected Supplier<List<Appointment>> getAppointmentSupplier(ProcessRequest request, long start, long finish) {
        Department department = request.getDepartment();
        return () -> appointmentService.getAppointmentsByDepartment(department, start, finish)
                .stream()
                .filter(a -> !Constants.DAY_OFF.equals(a.getService()))
                .collect(Collectors.toList());
    }

    protected List<MessageHolder> getHolders(List<Appointment> appointments, String strategyKey) {
        List<LString> messagesToLocalize = new ArrayList<>();
        if (appointments.size() == 0) {
            messagesToLocalize.add(LString.builder().title(Constants.Messages.NO_APP_FOR_DATE).build());
            messagesToLocalize.add(LString.builder().title(Constants.Messages.SELECT_ACTION).build());
            return List.of(MessageUtils.buildDashboardHolder("", messagesToLocalize, strategyKey));
        }
        String date = getReportDate(appointments);
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
                Context context = contextMap.get(appointment.getUserId());
                MessageUtils.fillMessagesToLocalize(messagesToLocalize, appointment, context,
                        MessageTemplate.APPOINTMENT_TIME_SERVICE_CLIENT);
                messagesToLocalize.add(LString.empty());
            }
        }
        messagesToLocalize.add(LString.builder().title(Constants.Messages.SELECT_ACTION).build());
        return List.of(MessageUtils.buildDashboardHolder("", messagesToLocalize, strategyKey));
    }

    private List<MessageHolder> buildAnotherMonthResponse(ProcessRequest request, boolean isNextMonth) {
        Department department = request.getDepartment();
        Context context = request.getContext();
        long startDate = DateUtils.getStartOfMonthDate(department, isNextMonth);
        long endDate = DateUtils.getEndOfMonthDate(department, isNextMonth);
        List<Appointment> appointments = getAppointmentSupplier(request, startDate, endDate).get();
        Set<String> appointmentDays = appointments.stream()
                .map(a -> DateUtils.getDayTitle(a.getDate()))
                .collect(Collectors.toSet());
        updateContextData(context, department, isNextMonth);
        BuildKeyboardRequest datePickerRequest = BuildKeyboardRequest.builder()
                .params(Map.of(
                        Constants.IS_NEXT_MONTH, isNextMonth,
                        Constants.USER_APPOINTMENTS, appointmentDays))
                .build();
        Month month = LocalDate.now().getMonth().plus(isNextMonth ? 1 : 0);
        MessageHolder datePicker = MessageUtils.holder(month.name(), ButtonsType.DATE_PICKER_MY_APP, datePickerRequest);
        return List.of(datePicker);
    }

    protected void updateContextData(Context context, Department department, boolean nextMonth) {
        int numberOfCurrentMonth = DateUtils.getNumberOfCurrentMonth(department);
        int monthToAdd = nextMonth ? 1 : 0;
        context.getParams().put(Constants.MONTH, (numberOfCurrentMonth + monthToAdd));
        ContextUtils.setPreviousStep(context);
    }

    private String getReportDate(List<Appointment> appointments) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(appointments.get(0).getDate()), ZoneId.systemDefault());
        return dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}
