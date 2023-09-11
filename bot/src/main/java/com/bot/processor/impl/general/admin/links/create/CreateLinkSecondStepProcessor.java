package com.bot.processor.impl.general.admin.links.create;

import com.bot.model.Context;
import com.bot.model.KeyBoardType;
import com.bot.model.LString;
import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.bot.util.Constants;
import com.bot.util.ContextUtils;
import com.bot.util.MessageUtils;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CreateLinkSecondStepProcessor implements IProcessor {

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        Update update = request.getUpdate();
        Context context = request.getContext();
        String linkType = MessageUtils.getTextFromUpdate(update);
        if ("".equals(linkType) || !Constants.LINKS_BUTTONS.contains(linkType)) {
            ContextUtils.resetLocationToPreviousStep(context);
            String message = Constants.Messages.INCORRECT_ACTION;
            return MessageUtils.buildCustomKeyboardHolders(message, Constants.LINKS_BUTTONS,
                    KeyBoardType.TWO_ROW, true);
        }
        context.getParams().put(Constants.LINK_TYPE, linkType);
        request.setContext(context);
        List<LString> messageLines = new ArrayList<>();
        messageLines.add(LString.builder().title(Constants.Messages.LINK_CREATE_PROMPT)
                .placeholders(Map.of("media", linkType)).build());
        return MessageUtils.buildCustomKeyboardHolders("", List.of(),
                KeyBoardType.TWO_ROW, messageLines, true);
    }
}
