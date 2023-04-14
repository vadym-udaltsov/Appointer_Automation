package com.bot.processor.impl.start;

import com.bot.model.Context;
import com.bot.model.Language;
import com.bot.model.MessageHolder;
import com.bot.processor.IProcessor;
import com.bot.service.IContextService;
import com.bot.util.MessageUtils;
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
    public List<MessageHolder> processRequest(Update update, Context context) {
        long id = MessageUtils.getUserIdFromUpdate(update);
        String languageName = MessageUtils.getTextFromUpdate(update);
        Language language = Language.fromValue(languageName);
        if (language == null) {
            log.warn(MarkerFactory.getMarker("SEV3"), "Default dictionary will be used");
            return Collections.singletonList(MessageUtils.getLanguageMessageHolder());
        }
        contextService.updateLocale(id, language);
        context.setLanguage(language);
        MessageHolder holder = MessageUtils.getContactsMessageHolder();
        return Collections.singletonList(holder);
    }
}
