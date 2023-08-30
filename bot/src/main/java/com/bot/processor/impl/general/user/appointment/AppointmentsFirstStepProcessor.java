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
import com.commons.model.Specialist;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;

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
            return MessageUtils.buildCustomKeyboardHolders(getNoAppointmentsMessage(),
                    Constants.DASHBOARD_BUTTONS.get(strategyKey), KeyBoardType.TWO_ROW, false);
        }
        appointments.removeIf(a -> a.getDate() > currentMothEndDate);
        Set<String> appointmentDays = appointments.stream()
                .map(a -> DateUtils.getDayTitle(a.getDate(), department))
                .collect(Collectors.toSet());
        appointmentDays.addAll(List.of(Constants.NEXT_MONTH, Constants.SELECTED_MONTH));
        context.getParams().put(Constants.AVAILABLE_DATES, appointmentDays);
        Month month = DateUtils.nowZoneDateTime(department).getMonth();
        context.getParams().put(Constants.MONTH, month.getValue());
        return MessageUtils.buildDatePicker(appointmentDays, Constants.Messages.INCORRECT_DATE, false, department);
    }

    protected String getNoAppointmentsMessage() {
        return "You have no appointments for current and next months";
    }

    protected List<MessageHolder> buildDayOffResponse(ProcessRequest request) {
        Department department = request.getDepartment();
        Context context = request.getContext();
        Update update = request.getUpdate();
        List<Specialist> availableSpecialists = department.getAvailableSpecialists();
        String selectedSpecialist = MessageUtils.getTextFromUpdate(update);
        if (Constants.BACK.equals(selectedSpecialist)) {
            selectedSpecialist = ContextUtils.getStringParam(context, Constants.SELECTED_SPEC);
        }
        List<String> specialistNames = availableSpecialists.stream()
                .map(Specialist::getName)
                .collect(Collectors.toList());
        if (!specialistNames.contains(selectedSpecialist) && !Constants.BACK.equals(selectedSpecialist)) {
            ContextUtils.resetLocationToPreviousStep(context);
            BuildKeyboardRequest holderRequest = MessageUtils.buildVerticalHolderRequestWithCommon(specialistNames);
            return List.of(MessageUtils.holder(Constants.Messages.INCORRECT_SPECIALIST, ButtonsType.KEYBOARD, holderRequest));
        }
        ContextUtils.setStringParameter(context, Constants.SELECTED_SPEC, selectedSpecialist);
        return buildAppointmentsCalendar(request);
    }

    protected Supplier<List<Appointment>> getDayOffAppointmentsSupplier(ProcessRequest request, long startDate, long finishDate) {
        Context context = request.getContext();
        String selectedSpecialist = ContextUtils.getStringParam(context, Constants.SELECTED_SPEC);
        return () -> {
            List<Appointment> appointmentsBySpecialist = appointmentService.getAppointmentsBySpecialist(request.getDepartment(),
                    selectedSpecialist, startDate, finishDate);
            return appointmentsBySpecialist.stream()
                    .filter(a -> Constants.DAY_OFF.equals(a.getService()))
                    .collect(Collectors.toList());
        };
    }

    protected Supplier<List<Appointment>> getAppointmentsSupplier(ProcessRequest request, long startDate, long finishDate) {
        long userId = request.getContext().getUserId();
        return () -> appointmentService.getAppointmentsByUserId(userId, startDate, finishDate);
    }
}
