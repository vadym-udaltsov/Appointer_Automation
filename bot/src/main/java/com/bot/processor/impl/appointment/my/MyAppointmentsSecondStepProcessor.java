package com.bot.processor.impl.appointment.my;

import com.bot.model.Appointment;
import com.bot.model.BuildKeyboardRequest;
import com.bot.model.ButtonsType;
import com.bot.model.Context;
import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.bot.service.IAppointmentService;
import com.bot.util.Constants;
import com.bot.util.ContextUtils;
import com.bot.util.DateUtils;
import com.bot.util.MessageUtils;
import com.commons.model.Department;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class MyAppointmentsSecondStepProcessor implements IProcessor {

    private final IAppointmentService appointmentService;

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        Update update = request.getUpdate();
        Department department = request.getDepartment();
        Context context = request.getContext();
        String selectedDay = MessageUtils.getTextFromUpdate(update);
        if (Constants.NEXT_MONTH.equals(selectedDay)) {
            return buildResponse(context, department, true);
        }
        if (Constants.CURRENT_MONTH.equals(selectedDay)) {
            return buildResponse(context, department, false);
        }
        return null;
    }

    private List<MessageHolder> buildResponse(Context context, Department department, boolean isNextMonth) {
        long startDate = DateUtils.getStartOfMonthDate(department, isNextMonth);
        long endDate = DateUtils.getEndOfMonthDate(department, isNextMonth);
        List<Appointment> appointments = appointmentService.getAppointmentsByUserId(context.getUserId(), startDate, endDate);
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
}
