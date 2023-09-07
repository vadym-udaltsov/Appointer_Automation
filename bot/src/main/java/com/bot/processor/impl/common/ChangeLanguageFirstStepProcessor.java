package com.bot.processor.impl.common;

import com.bot.model.Context;
import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.bot.service.IDictionaryService;
import com.bot.util.ContextUtils;
import com.bot.util.MessageUtils;
import com.commons.model.Department;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@RequiredArgsConstructor
public class ChangeLanguageFirstStepProcessor implements IProcessor {

    private final IDictionaryService dictionaryService;

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        Update update = request.getUpdate();
        Context context = request.getContext();
        String selectedButton = MessageUtils.getTextFromUpdate(update);
        if (!"Change language".equals(selectedButton)) {
            ContextUtils.resetLocationToPreviousStep(context);
            return MessageUtils.buildProfileDashboard("Select available option");
        }
        Department department = request.getDepartment();
        List<String> dictionaryFileKeys = dictionaryService.getDictionaryFileKeys(department);
        return List.of(MessageUtils.getLanguageMessageHolder(MessageUtils.filterLanguages(dictionaryFileKeys, department)));
    }
}
