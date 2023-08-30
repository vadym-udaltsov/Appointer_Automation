package com.bot.processor.impl.general.admin.dayoff.create.daily;

import com.bot.model.Context;
import com.bot.model.KeyBoardType;
import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.commons.service.IAppointmentService;
import com.bot.service.IContextService;
import com.bot.util.Constants;
import com.bot.util.ContextUtils;
import com.commons.utils.DateUtils;
import com.bot.util.MessageUtils;
import com.commons.model.Department;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

public class CreateDayOffFifthStepProcessor extends CreateDayOffProcessor implements IProcessor {

    public CreateDayOffFifthStepProcessor(IAppointmentService appointmentService, IContextService contextService) {
        super(appointmentService, contextService);
    }

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        Department department = request.getDepartment();
        Update update = request.getUpdate();
        Context context = request.getContext();

        String selectedPeriod = MessageUtils.getTextFromUpdate(update);
        List<String> availableDurations = (List<String>) context.getParams().get(Constants.AVAILABLE_DURATIONS);

        if (!availableDurations.contains(selectedPeriod)) {
            ContextUtils.resetLocationToPreviousStep(context);
            return MessageUtils.buildCustomKeyboardHolders("Select duration from proposed", availableDurations,
                    KeyBoardType.VERTICAL, true);
        }

        int month = ContextUtils.getIntParam(context, Constants.MONTH);
        int day = ContextUtils.getIntParam(context, Constants.SELECTED_DAY);
        int hour = ContextUtils.getIntParam(context, Constants.SELECTED_HOUR);
        int minute = ContextUtils.getIntParam(context, Constants.SELECTED_MINUTE);

        String specialist = ContextUtils.getStringParam(context, Constants.SELECTED_SPEC);
        int year = DateUtils.getNumberOfCurrentYear(department);

        String[] timeParts = selectedPeriod.split(":");
        int durationHour = Integer.parseInt(timeParts[0]);
        int durationMinute = Integer.parseInt(timeParts[1]);
        int duration = durationHour * 60 + durationMinute;
        return createDayOff(department, context, specialist, year, month, day, hour, minute, duration);
    }
}
