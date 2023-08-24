package com.bot.processor.impl.general.admin.block;

import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

public class UnblockUserSecondStepProcessor extends AbstractBlockUserSecondStepProcessor implements IProcessor {

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        return buildResponse(request);
    }

    @Override
    protected String getSubmitMessage() {
        return "Submit client unblocking";
    }
}
