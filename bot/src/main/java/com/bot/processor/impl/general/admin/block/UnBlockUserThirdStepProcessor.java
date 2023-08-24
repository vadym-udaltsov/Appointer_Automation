package com.bot.processor.impl.general.admin.block;

import com.bot.model.Context;
import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.bot.service.IContextService;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

public class UnBlockUserThirdStepProcessor extends AbstractBlockUserThirdStepProcessor implements IProcessor {

    public UnBlockUserThirdStepProcessor(IContextService contextService) {
        super(contextService);
    }

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        return buildResponse(request);
    }

    @Override
    protected void executeContextAction(Context selectedContext) {
        selectedContext.setBlocked(false);
    }

    @Override
    protected String getSuccessMessage() {
        return "Client was unblocked";
    }
}
