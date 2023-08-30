package com.bot.processor.impl.general.admin.messaging;

import com.bot.model.Context;
import com.bot.model.LString;
import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.bot.util.Constants;
import com.bot.util.ContextUtils;
import com.bot.util.MessageUtils;
import com.bot.util.StringUtils;
import com.commons.utils.JsonUtils;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class MassMessagingSecondStepProcessor implements IProcessor {

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        Context context = request.getContext();
        Update update = request.getUpdate();
        String textFromUpdate = MessageUtils.getTextFromUpdate(update);
        List<String> buttons = List.of("Submit");

        if (StringUtils.isBlank(textFromUpdate)) {
            String message = Constants.Messages.INPUT_MESSAGE;
            ContextUtils.resetLocationToPreviousStep(context);
            return List.of(MessageUtils.buildKeyboardHolder(message, List.of(), List.of()));
        }

        List<LString> submitMessageLines = new ArrayList<>();
        submitMessageLines.add(LString.builder().title(Constants.Messages.MASS_MESSAGE_HEADER).build());
        submitMessageLines.add(LString.builder().title(textFromUpdate).build());
        context.getParams().put(Constants.TEXT_FOR_SUBMIT, JsonUtils.convertObjectToString(submitMessageLines));

        List<LString> messageLines = new ArrayList<>();
        messageLines.add(LString.builder().title(Constants.Messages.SUBMIT_MESSAGE).build());
        messageLines.addAll(submitMessageLines);

        return List.of(MessageUtils.buildKeyboardHolder("", messageLines, buttons));
    }
}
