package com.bot.processor.impl.general.admin.comments.view;

import com.bot.model.Context;
import com.bot.model.KeyBoardType;
import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.bot.processor.impl.general.admin.comments.ViewCommentsSecondStep;
import com.bot.service.IContextService;
import com.bot.util.Constants;
import com.bot.util.ContextUtils;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

public class ViewCommentsSecondStepProcessor extends ViewCommentsSecondStep implements IProcessor {

    public ViewCommentsSecondStepProcessor(IContextService contextService) {
        super(contextService);
    }

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        return buildResponse(request);
    }

    @Override
    protected void resetLocation(Context context) {
        ContextUtils.resetLocationToStep(context, Constants.Processors.COMMENTS_DASH);
    }

    @Override
    protected KeyBoardType getKeyBoard() {
        return KeyBoardType.TWO_ROW;
    }

    @Override
    protected List<String> getButtons(List<String> numbers8) {
        return Constants.ADMIN_APPOINTMENT_BUTTONS;
    }
}
