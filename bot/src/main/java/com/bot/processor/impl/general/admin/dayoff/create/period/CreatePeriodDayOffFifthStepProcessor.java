package com.bot.processor.impl.general.admin.dayoff.create.period;

import com.bot.model.Context;
import com.bot.model.LString;
import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.bot.service.ISendMessageService;
import com.bot.util.ContextUtils;
import com.bot.util.MessageUtils;
import com.commons.model.Department;
import com.commons.utils.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CreatePeriodDayOffFifthStepProcessor implements IProcessor {

    private final ISendMessageService sendMessageService;

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        Department department = request.getDepartment();
        Update update = request.getUpdate();
        Context context = request.getContext();
        String message = MessageUtils.getTextFromUpdate(update);
        List<String> contextStr = (List<String>) context.getParams().get("contexts");
        List<Context> userContexts = contextStr.stream()
                .map(c -> JsonUtils.parseStringToObject(c, new TypeReference<Context>() {
                }))
                .collect(Collectors.toList());
        sendMessageService.sendNotificationToUsers(List.of(LString.builder().title(message).build()), userContexts,
                department);
        ContextUtils.resetLocationToDashboard(context);
        String strategyKey = ContextUtils.getStrategyKey(context, department);
        return List.of(MessageUtils.buildDashboardHolder("Messages were sent", List.of(), strategyKey));
    }
}
