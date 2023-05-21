package com.bot.processor.impl.appointment.create;

import com.bot.model.Appointment;
import com.bot.model.BuildKeyboardRequest;
import com.bot.model.ButtonsType;
import com.bot.model.Context;
import com.bot.model.KeyBoardType;
import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.bot.service.IAppointmentService;
import com.bot.service.IContextService;
import com.bot.util.Constants;
import com.bot.util.ContextUtils;
import com.bot.util.DateUtils;
import com.bot.util.MessageUtils;
import com.commons.model.Department;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class CreateAppointmentFifthStepProcessor implements IProcessor {

    private final IContextService contextService;
    private final IAppointmentService appointmentService;

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        Update update = request.getUpdate();
        Context context = request.getContext();
        Department department = request.getDepartment();
        String specialist = ContextUtils.getStringParam(context, Constants.SELECTED_SPEC);
        String serviceName = ContextUtils.getStringParam(context, Constants.SELECTED_SERVICE);
        String month = ContextUtils.getStringParam(context, Constants.MONTH);
        String day = ContextUtils.getStringParam(context, Constants.SELECTED_DAY);
        String timeString = MessageUtils.getTextFromUpdate(update);
        List<String> availableSlots = (List<String>) context.getParams().get(Constants.AVAILABLE_SLOTS);
        if (!availableSlots.contains(timeString)) {
            BuildKeyboardRequest holderRequest = BuildKeyboardRequest.builder()
                    .type(KeyBoardType.FOUR_ROW)
                    .buttonsMap(MessageUtils.buildButtons(MessageUtils.commonButtons(availableSlots), true))
                    .build();
            MessageHolder holder = MessageUtils.holder("Select time from proposed", ButtonsType.KEYBOARD, holderRequest);
            contextService.setPreviousStep(context);
            return List.of(holder);
        }
        int year = DateUtils.getNumberOfCurrentYear(department);
        String[] timeParts = timeString.split(":");
        int hour = Integer.parseInt(timeParts[0]);
        int minute = Integer.parseInt(timeParts[1]);
        LocalDateTime localDateTime = LocalDateTime.of(year, Integer.parseInt(month), Integer.parseInt(day), hour, minute);
        long appointmentDate = localDateTime.toEpochSecond(ZoneOffset.UTC);
        Appointment appointment = Appointment.builder()
                .specialist(specialist)
                .service(serviceName)
                .userId(context.getUserId())
                .date(appointmentDate)
                .build();
        appointmentService.save(appointment);
        contextService.resetLocationToDashboard(context);
        String date = localDateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy, HH:mm"));
        String message = String.format("Created appointment\n%s\nservice - %s\nspecialist - %s\n", date, serviceName, specialist);
        return List.of(MessageUtils.buildDashboardHolder(message));
    }
}
