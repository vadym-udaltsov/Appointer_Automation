package com.bot.processor.impl.appointment.my;

import com.bot.model.Appointment;
import com.bot.model.BuildKeyboardRequest;
import com.bot.model.ButtonsType;
import com.bot.model.Context;
import com.bot.model.KeyBoardType;
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
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class MyAppointmentsFirstStepProcessor implements IProcessor {

    private final IAppointmentService appointmentService;

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        Context context = request.getContext();
        long userId = context.getUserId();
        Department department = request.getDepartment();
        long startDate = DateUtils.getStartOfMonthDate(department, false);
        long currentMothEndDate = DateUtils.getEndOfMonthDate(department, false);
        long nextMonthEndDate = DateUtils.getEndOfMonthDate(department, true);
        List<Appointment> appointments = appointmentService.getAppointmentsByUserId(userId, startDate, nextMonthEndDate);
        if (appointments.size() == 0) {
            ContextUtils.resetLocationToDashboard(context);
            BuildKeyboardRequest commonsRequest = BuildKeyboardRequest.builder()
                    .type(KeyBoardType.TWO_ROW)
                    .buttonsMap(MessageUtils.buildButtons(MessageUtils.commonButtons(MessageUtils.DASHBOARD), false))
                    .build();
            MessageHolder commonButtonsHolder = MessageUtils.holder("You have no appointments for current and next months",
                    ButtonsType.KEYBOARD, commonsRequest);
            return List.of(commonButtonsHolder);
        }
        appointments.removeIf(a -> a.getDate() > currentMothEndDate);
        Set<String> appointmentDays = appointments.stream()
                .map(a -> DateUtils.getDayTitle(a.getDate()))
                .collect(Collectors.toSet());
        BuildKeyboardRequest datePickerRequest = BuildKeyboardRequest.builder()
                .params(Map.of(
                        Constants.IS_NEXT_MONTH, false,
                        Constants.USER_APPOINTMENTS, appointmentDays))
                .build();
        Month month = LocalDate.now().getMonth();
        ContextUtils.setStringParameter(context, Constants.MONTH, String.valueOf(month.getValue()));
        MessageHolder datePicker = MessageUtils.holder(month.name(), ButtonsType.DATE_PICKER_MY_APP, datePickerRequest);
        BuildKeyboardRequest commonsRequest = BuildKeyboardRequest.builder()
                .type(KeyBoardType.TWO_ROW)
                .buttonsMap(MessageUtils.buildButtons(List.of(), true))
                .build();
        MessageHolder commonButtonsHolder = MessageUtils.holder("Select day", ButtonsType.KEYBOARD, commonsRequest);
        return List.of(commonButtonsHolder, datePicker);
    }
}
