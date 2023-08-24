package com.bot.processor.impl.general.admin.block;

import com.bot.model.Context;
import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.bot.service.IContextService;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

public class UnblockUserFirstStepProcessor extends AbstractBlockUserFirstStepProcessor implements IProcessor {

    public UnblockUserFirstStepProcessor(IContextService contextService) {
        super(contextService);
    }

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        return buildResponse(request);
    }

    @Override
    protected String getNoUsersMessage() {
        return "You have no blocked clients";
    }

    @Override
    protected void filterContexts(List<Context> userContexts) {
        userContexts.removeIf(c -> !c.isBlocked());
    }
}
