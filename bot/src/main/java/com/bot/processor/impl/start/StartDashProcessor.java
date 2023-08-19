package com.bot.processor.impl.start;

import com.bot.model.Context;
import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.bot.util.Constants;
import com.bot.util.ContextUtils;
import com.bot.util.MessageUtils;
import com.bot.util.StringUtils;
import com.commons.model.Department;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Collections;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class StartDashProcessor implements IProcessor {

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        Update update = request.getUpdate();
        Context context = request.getContext();
        if (context.isBlocked()) {
            return Collections.singletonList(MessageUtils.getClientBlockedMessageHolder());
        }
        Department department = request.getDepartment();
        String text = MessageUtils.getTextFromUpdate(update);
        String message = Constants.Messages.INCORRECT_ACTION;
        if (!StringUtils.isBlank(text) && (text.equals(Constants.BACK) || text.equals(Constants.HOME))) {
            message = Constants.Messages.SELECT_ACTION;
        }
        String strategyKey = ContextUtils.getStrategyKey(context, department);
        return List.of(MessageUtils.buildDashboardHolder(message, List.of(), strategyKey));
    }
}
