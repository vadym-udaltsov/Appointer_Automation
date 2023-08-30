package com.bot.processor.impl.common;

import com.bot.model.Context;
import com.bot.model.Language;
import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.bot.util.ContextUtils;
import com.bot.util.MessageUtils;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Collections;
import java.util.List;

public class ChangeLanguageSecondStepProcessor implements IProcessor {

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        Update update = request.getUpdate();
        Context context = request.getContext();
        String languageName = MessageUtils.getTextFromUpdate(update);
        Language language = Language.fromValue(languageName);
        if (language == null) {
            ContextUtils.resetLocationToPreviousStep(context);
            return Collections.singletonList(MessageUtils.getLanguageMessageHolder("Select language from available"));
        }
        context.setLanguage(language);
        String strategyKey = ContextUtils.getStrategyKey(context, request.getDepartment());
        ContextUtils.resetLocationToDashboard(context);
        return List.of(MessageUtils.buildDashboardHolder("Language was changed", List.of(), strategyKey));
    }
}
