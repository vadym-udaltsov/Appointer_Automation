package com.bot.processor.impl.general.admin.comments.delete;

import com.bot.model.KeyBoardType;
import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.bot.processor.impl.general.admin.comments.ViewCommentsSecondStep;
import com.bot.service.IContextService;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

public class DeleteCommentSecondStepProcessor extends ViewCommentsSecondStep implements IProcessor {

    public DeleteCommentSecondStepProcessor(IContextService contextService) {
        super(contextService);
    }

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        return buildResponse(request);
    }

    @Override
    protected KeyBoardType getKeyBoard() {
        return KeyBoardType.VERTICAL;
    }

    @Override
    protected List<String> getButtons(List<String> numbers) {
        return numbers;
    }

    @Override
    protected String getLastMessage() {
        return "Select number to delete comment";
    }
}
