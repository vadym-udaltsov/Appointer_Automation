package com.bot.processor.impl.common;

import com.bot.model.Context;
import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.bot.util.ContextUtils;
import com.bot.util.MessageUtils;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

public class ChangeLanguageFirstStepProcessor implements IProcessor {

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        Update update = request.getUpdate();
        Context context = request.getContext();
        String selectedButton = MessageUtils.getTextFromUpdate(update);
        if (!"Change language".equals(selectedButton)) {
            ContextUtils.resetLocationToPreviousStep(context);
            return MessageUtils.buildProfileDashboard("Select available option");
        }
        return List.of(MessageUtils.getLanguageMessageHolder());
    }
}
