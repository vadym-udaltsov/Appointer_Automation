package com.bot.processor.impl.general.user.appointment;

import com.bot.model.Appointment;
import com.bot.model.Context;
import com.bot.model.KeyBoardType;
import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.service.IAppointmentService;
import com.bot.util.Constants;
import com.bot.util.ContextUtils;
import com.bot.util.DateUtils;
import com.bot.util.MessageUtils;
import com.commons.model.Department;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class AppointmentsFirstStepProcessor {

    private final IAppointmentService appointmentService;

    public List<MessageHolder> buildAppointmentsCalendar(ProcessRequest request) {
        Context context = request.getContext();
        Department department = request.getDepartment();
        long startDate = DateUtils.getStartOfMonthDate(department, false);
        long currentMothEndDate = DateUtils.getEndOfMonthDate(department, false);
        long nextMonthEndDate = DateUtils.getEndOfMonthDate(department, true);
        List<Appointment> appointments = getAppointmentsSupplier(request, startDate, nextMonthEndDate).get();
        if (appointments.size() == 0) {
            ContextUtils.resetLocationToDashboard(context);
            String strategyKey = ContextUtils.getStrategyKey(context, department);
            return MessageUtils.buildCustomKeyboardHolders("You have no appointments for current and next months",
                    Constants.DASHBOARD_BUTTONS.get(strategyKey), KeyBoardType.TWO_ROW, false);
        }
        appointments.removeIf(a -> a.getDate() > currentMothEndDate);
        Set<String> appointmentDays = appointments.stream()
                .map(a -> DateUtils.getDayTitle(a.getDate()))
                .collect(Collectors.toSet());
        appointmentDays.addAll(List.of(Constants.NEXT_MONTH, Constants.CURRENT_MONTH));
        context.getParams().put(Constants.AVAILABLE_DATES, appointmentDays);
        Month month = LocalDate.now().getMonth();
        context.getParams().put(Constants.MONTH, month.getValue());
        return MessageUtils.buildDatePicker(appointmentDays, "Select available date", false);
    }

    protected Supplier<List<Appointment>> getAppointmentsSupplier(ProcessRequest request, long startDate, long finishDate) {
        long userId = request.getContext().getUserId();
        return () -> appointmentService.getAppointmentsByUserId(userId, startDate, finishDate);
    }
}
