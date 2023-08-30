package com.bot.processor.impl.general.admin.block;

import com.bot.model.BuildKeyboardRequest;
import com.bot.model.ButtonsType;
import com.bot.model.Context;
import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.service.IContextService;
import com.bot.util.Constants;
import com.bot.util.ContextUtils;
import com.bot.util.MessageUtils;
import com.commons.model.Department;
import com.commons.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@RequiredArgsConstructor
public abstract class AbstractBlockUserThirdStepProcessor {

    private final IContextService contextService;

    protected List<MessageHolder> buildResponse(ProcessRequest request) {
        Update update = request.getUpdate();
        Context context = request.getContext();
        Department department = request.getDepartment();
        String cancelSubmit = MessageUtils.getTextFromUpdate(update);
        String strategyKey = ContextUtils.getStrategyKey(context, department);
        if (Constants.SUBMIT.equals(cancelSubmit)) {
            String selectedTitle = ContextUtils.getStringParam(context, Constants.SELECTED_TITLE);
            String selectedContextString = ContextUtils.getStringParam(context, selectedTitle);
            Context selectedContext = JsonUtils.parseStringToObject(selectedContextString, Context.class);
            executeContextAction(selectedContext);
            contextService.updateContext(selectedContext);
            ContextUtils.resetLocationToDashboard(context);
            return List.of(MessageUtils.buildDashboardHolder(getSuccessMessage(), List.of(), strategyKey));
        }
        ContextUtils.resetLocationToPreviousStep(context);
        BuildKeyboardRequest holderRequest = MessageUtils.buildVerticalHolderRequestWithCommon(List.of(Constants.SUBMIT));
        return List.of(MessageUtils.holder("Select option from proposed", ButtonsType.KEYBOARD, holderRequest));
    }

    protected abstract String getSuccessMessage();

    protected abstract void executeContextAction(Context selectedContext);
}
