package com.bot.processor.impl.general.admin.comments.view;

import com.bot.model.Context;
import com.bot.model.KeyBoardType;
import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.bot.processor.impl.general.admin.block.AbstractUserFirstStepProcessor;
import com.bot.service.IContextService;
import com.bot.util.Constants;
import com.bot.util.ContextUtils;
import com.bot.util.MessageUtils;
import com.commons.model.Department;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

public class ViewCommentsFirstStepProcessor extends AbstractUserFirstStepProcessor implements IProcessor {

    public ViewCommentsFirstStepProcessor(IContextService contextService) {
        super(contextService);
    }

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        return buildResponse(request);
    }

    @Override
    protected List<MessageHolder> getNoUsersResponse(Context context, Department department) {
        ContextUtils.resetLocationToStep(context, Constants.Processors.COMMENTS_DASH);
        return MessageUtils.buildCustomKeyboardHolders("You have no clients with comments", Constants.ADMIN_APPOINTMENT_BUTTONS, KeyBoardType.TWO_ROW,
                List.of(), true);
    }

    @Override
    protected void filterContexts(List<Context> userContexts) {
        userContexts.removeIf(uc -> uc.isBlocked()
                || uc.isCustom()
                || uc.getComments() == null
                || uc.getComments().size() == 0);
    }
}
