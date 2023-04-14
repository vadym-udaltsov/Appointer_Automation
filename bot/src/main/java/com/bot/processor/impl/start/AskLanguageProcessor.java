package com.bot.processor.impl.start;

import com.bot.model.Context;
import com.bot.model.MessageHolder;
import com.bot.processor.IProcessor;
import com.bot.service.IContextService;
import com.bot.util.Constants;
import com.bot.util.MessageUtils;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
public class AskLanguageProcessor implements IProcessor {

    private final IContextService contextService;

    @Override
    public List<MessageHolder> processRequest(Update update, Context context) {
        long operatorId = MessageUtils.getUserIdFromUpdate(update);
        MessageHolder messageHolder = MessageUtils.getLanguageMessageHolder();
        if (context == null) {
            context = buildContext(operatorId);
            contextService.save(context);
        }
        return Collections.singletonList(messageHolder);
    }

    private Context buildContext(long chatId) {
        Context context = new Context();
        context.setUserId(chatId);
        context.setNavigation(List.of(Constants.Processors.ASK_LANG));
        return context;
    }

}
