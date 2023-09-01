package com.bot.processor.impl.general.admin.messaging;

import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.bot.util.Constants;
import com.bot.util.MessageUtils;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

public class SendTextMessageFirstStepProcessor implements IProcessor {

    public SendTextMessageFirstStepProcessor() {
    }

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        String message = Constants.Messages.INPUT_MESSAGE_TEXT;
        return List.of(MessageUtils.buildKeyboardHolder(message, List.of(), List.of()));
    }
}
