package com.bot.processor.impl.general.admin.block;

import com.bot.model.BuildKeyboardRequest;
import com.bot.model.ButtonsType;
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
import com.commons.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class BlockUserFirstStepProcessor implements IProcessor {

    private final IContextService contextService;

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        Department department = request.getDepartment();
        Context context = request.getContext();
        List<String> admins = department.getAdmins();
        List<Context> userContexts = contextService.getUserContextsByDepartment(department);
        userContexts.removeIf(uc -> admins.contains(uc.getPhoneNumber()) || uc.isBlocked());
        if (userContexts.size() == 0) {
            ContextUtils.resetLocationToDashboard(context);
            String strategyKey = ContextUtils.getStrategyKey(context, department);
            return MessageUtils.buildCustomKeyboardHolders("You have no clients",
                    Constants.DASHBOARD_BUTTONS.get(strategyKey), KeyBoardType.TWO_ROW, false);
        }
        List<String> titles = new ArrayList<>();
        for (Context userContext : userContexts) {
            String title = buildButtonTitle(userContext);
            titles.add(title);
            context.getParams().put(title, JsonUtils.convertObjectToString(userContext));
        }
        context.getParams().put(Constants.AVAILABLE_TITLES, titles);
        BuildKeyboardRequest holderRequest = MessageUtils.buildVerticalHolderRequestWithCommon(titles);
        return List.of(MessageUtils.holder("Select client", ButtonsType.KEYBOARD, holderRequest));
    }

    private String buildButtonTitle(Context context) {
        return String.format("%s, %s", context.getPhoneNumber(), context.getName());
    }
}
