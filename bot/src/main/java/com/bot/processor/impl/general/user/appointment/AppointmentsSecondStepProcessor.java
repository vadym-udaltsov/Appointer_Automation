package com.bot.processor.impl.general.user.appointment;

import com.commons.model.Appointment;
import com.bot.model.BuildKeyboardRequest;
import com.bot.model.ButtonsType;
import com.bot.model.Context;
import com.bot.model.KeyBoardType;
import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.commons.service.IAppointmentService;
import com.bot.util.Constants;
import com.bot.util.ContextUtils;
import com.commons.utils.DateUtils;
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
        if (Constants.BACK.equals(selectedDay)) {
            List<String> availableAppointments = (List<String>) context.getParams().get(Constants.AVAILABLE_APPOINTMENTS);
            BuildKeyboardRequest holderRequest = MessageUtils.buildVerticalHolderRequestWithCommon(availableAppointments);
            return List.of(MessageUtils.holder("Select appointment", ButtonsType.KEYBOARD, holderRequest));
        }
        if (Constants.NEXT_MONTH.equals(selectedDay)) {
            return buildAnotherMonthResponse(request, true);
        }
        if (Constants.CURRENT_MONTH.equals(selectedDay)) {
            return buildAnotherMonthResponse(request, false);
        }
        Set<String> availableDates = (Set<String>) context.getParams().get(Constants.AVAILABLE_DATES);
        if (!availableDates.contains(selectedDay)) {
            List<MessageHolder> holders = MessageUtils.buildCustomKeyboardHolders(Constants.Messages.INCORRECT_DATE, List.of(),
                    KeyBoardType.TWO_ROW, true);
            holders.addAll(buildAnotherMonthResponse(request, false));
            return holders;
        }

        int month = ContextUtils.getIntParam(context, Constants.MONTH);
        long startOfDay = DateUtils.getStartOrEndOfDay(month, Integer.parseInt(selectedDay), false);
        long endOfDay = DateUtils.getStartOrEndOfDay(month, Integer.parseInt(selectedDay), true);

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
        updateAvailableDates(context, appointmentDays);
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

    protected void updateAvailableDates(Context context, Set<String> appointmentDays) {
        appointmentDays.addAll(List.of(Constants.NEXT_MONTH, Constants.CURRENT_MONTH));
        context.getParams().put(Constants.AVAILABLE_DATES, appointmentDays);
    }

    protected void fillContextParams(List<Appointment> appointments, Context context) {
    }

    protected void resetLocationToDashboard(Context context) {
    }
}
