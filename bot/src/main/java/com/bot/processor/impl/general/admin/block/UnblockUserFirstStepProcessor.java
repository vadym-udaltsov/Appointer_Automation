package com.bot.processor.impl.general.admin.block;

import com.bot.model.Context;
import com.bot.model.KeyBoardType;
import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.bot.service.IContextService;
import com.bot.util.Constants;
import com.bot.util.ContextUtils;
import com.bot.util.MessageUtils;
import com.commons.model.Department;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

public class UnblockUserFirstStepProcessor extends AbstractUserFirstStepProcessor implements IProcessor {

    public UnblockUserFirstStepProcessor(IContextService contextService) {
        super(contextService);
    }

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        return buildResponse(request);
    }

    @Override
    protected List<MessageHolder> getNoUsersResponse(Context context, Department department) {
        ContextUtils.resetLocationToDashboard(context);
        String strategyKey = ContextUtils.getStrategyKey(context, department);
        return MessageUtils.buildCustomKeyboardHolders("You have no blocked clients",
                Constants.DASHBOARD_BUTTONS.get(strategyKey), KeyBoardType.TWO_ROW, false);
    }


    @Override
    protected void filterContexts(List<Context> userContexts) {
        userContexts.removeIf(c -> !c.isBlocked());
    }
}
