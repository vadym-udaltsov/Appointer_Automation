package com.bot.processor.impl.general.admin.dayoff.cancel;

import com.commons.model.Appointment;
import com.bot.model.Context;
import com.bot.model.KeyBoardType;
import com.bot.model.LString;
import com.bot.model.MessageHolder;
import com.bot.model.MessageTemplate;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.commons.service.IAppointmentService;
import com.bot.util.Constants;
import com.bot.util.ContextUtils;
import com.bot.util.MessageUtils;
import com.commons.model.Department;
import com.commons.utils.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class CancelDayOffFourthStepProcessor implements IProcessor {

    private final IAppointmentService appointmentService;

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        Update update = request.getUpdate();
        Context context = request.getContext();
        Department department = request.getDepartment();
        String selectedDayOff = MessageUtils.getTextFromUpdate(update);
        String dayOffStr = ContextUtils.getStringParam(context, selectedDayOff);
        if (dayOffStr == null) {
            ContextUtils.setPreviousStep(context);
            List<String> dayOffTitles = (List<String>) context.getParams().get(Constants.AVAILABLE_APPOINTMENTS);
            return MessageUtils.buildCustomKeyboardHolders("Select day off period from proposed", dayOffTitles,
                    KeyBoardType.VERTICAL, true);
        }
        Appointment dayOff = JsonUtils.parseStringToObject(dayOffStr, new TypeReference<>() {
        });
        appointmentService.delete(dayOff);
        ContextUtils.resetLocationToDashboard(context);
        String strategyKey = ContextUtils.getStrategyKey(context, department);
        List<LString> messagesToLocalize = new ArrayList<>();
        messagesToLocalize.add(LString.builder().title("Day off cancelled:").build());
        messagesToLocalize.add(LString.empty());
        MessageUtils.fillMessagesToLocalize(messagesToLocalize, dayOff, context, MessageTemplate.DAY_OFF_ALL_FIELDS);
        return List.of(MessageUtils.buildDashboardHolder("", messagesToLocalize, strategyKey));
    }
}
