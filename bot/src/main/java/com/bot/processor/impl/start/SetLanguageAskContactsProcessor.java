package com.bot.processor.impl.start;

import com.bot.model.Context;
import com.bot.model.Language;
import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.bot.service.IContextService;
import com.bot.util.ContextUtils;
import com.bot.util.MessageUtils;
import com.commons.model.Department;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MarkerFactory;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Collections;
import java.util.List;

@Slf4j
public class SetLanguageAskContactsProcessor implements IProcessor {

    private final IContextService contextService;

    public SetLanguageAskContactsProcessor(IContextService contextService) {
        this.contextService = contextService;
    }

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) {
        Update update = request.getUpdate();
        Context context = request.getContext();
        long id = MessageUtils.getUserIdFromUpdate(update);
        String languageName = MessageUtils.getTextFromUpdate(update);
        Language language = Language.fromValue(languageName);
        if (language == null) {
            ContextUtils.resetLocationToPreviousStep(context);
            log.warn(MarkerFactory.getMarker("SEV3"), "Default dictionary will be used");
            return Collections.singletonList(MessageUtils.getLanguageMessageHolder());
        }
        Department department = request.getDepartment();
        contextService.updateLocale(id, department.getId(), language);
        context.setLanguage(language);
        MessageHolder holder = MessageUtils.getContactsMessageHolder();
        return Collections.singletonList(holder);
    }
}
