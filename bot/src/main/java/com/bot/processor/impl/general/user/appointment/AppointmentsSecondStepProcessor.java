package com.bot.processor.impl.general.user.appointment;

import com.bot.model.Appointment;
import com.bot.model.BuildKeyboardRequest;
import com.bot.model.ButtonsType;
import com.bot.model.Context;
import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.service.IAppointmentService;
import com.bot.util.Constants;
import com.bot.util.ContextUtils;
import com.bot.util.DateUtils;
import com.bot.util.MessageUtils;
import com.commons.model.Department;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public abstract class AppointmentsSecondStepProcessor {

    private final IAppointmentService appointmentService;

    protected List<MessageHolder> buildResponse(ProcessRequest request) {
        Update update = request.getUpdate();
        Department department = request.getDepartment();
        Context context = request.getContext();
        String selectedDay = MessageUtils.getTextFromUpdate(update);
        if (Constants.NEXT_MONTH.equals(selectedDay)) {
            return buildResponse(request, true);
        }
        if (Constants.CURRENT_MONTH.equals(selectedDay)) {
            return buildResponse(request, false);
        }
        String monthStr = ContextUtils.getStringParam(context, Constants.MONTH);
        long startOfDay = DateUtils.getStartOrEndOfDay(Integer.parseInt(monthStr), Integer.parseInt(selectedDay), false);
        long endOfDay = DateUtils.getStartOrEndOfDay(Integer.parseInt(monthStr), Integer.parseInt(selectedDay), true);

        resetLocationToDashboard(context);
        List<Appointment> appointments = getAppointmentSupplier(request, startOfDay, endOfDay).get();
        fillContextParams(appointments, context);
        String strategyKey = ContextUtils.getStrategyKey(context, department);
        return getHolders(appointments, strategyKey);
    }

    protected Supplier<List<Appointment>> getAppointmentSupplier(ProcessRequest request, long start, long finish) {
        long userId = request.getContext().getUserId();
        return () -> appointmentService.getAppointmentsByUserId(userId, start, finish);
    }

    protected abstract List<MessageHolder> getHolders(List<Appointment> appointments, String strategyKey);

    private List<MessageHolder> buildResponse(ProcessRequest request, boolean isNextMonth) {
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

    private void updateContextData(Context context, Department department, boolean nextMonth) {
        int numberOfCurrentMonth = DateUtils.getNumberOfCurrentMonth(department);
        int monthToAdd = nextMonth ? 1 : 0;
        ContextUtils.setStringParameter(context, Constants.MONTH, String.valueOf(numberOfCurrentMonth + monthToAdd));
        ContextUtils.setPreviousStep(context);
    }

    protected void fillContextParams(List<Appointment> appointments, Context context) {
    }

    protected void resetLocationToDashboard(Context context) {
    }
}
