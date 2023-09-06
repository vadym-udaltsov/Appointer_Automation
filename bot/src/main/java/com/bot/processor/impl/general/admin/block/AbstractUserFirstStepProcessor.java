package com.bot.processor.impl.general.admin.block;

import com.bot.model.BuildKeyboardRequest;
import com.bot.model.ButtonsType;
import com.bot.model.Context;
import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.service.IContextService;
import com.bot.util.Constants;
import com.bot.util.MessageUtils;
import com.commons.model.Department;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public abstract class AbstractUserFirstStepProcessor {

    private final IContextService contextService;

    protected List<MessageHolder> buildResponse(ProcessRequest request) {
        Department department = request.getDepartment();
        Context context = request.getContext();
        List<String> admins = department.getAdmins();
        List<Context> userContexts = contextService.getUserContextsByDepartment(department);
        userContexts.removeIf(uc -> admins.contains(uc.getPhoneNumber()));
        filterContexts(userContexts);
        if (userContexts.size() == 0) {
            return getNoUsersResponse(context, department);
        }
        List<String> titles = new ArrayList<>();
        for (Context userContext : userContexts) {
            String title = buildButtonTitle(userContext);
            titles.add(title);
            context.getParams().put(title, userContext.getUserId());
        }
        context.getParams().put(Constants.AVAILABLE_TITLES, titles);
        BuildKeyboardRequest holderRequest = MessageUtils.buildVerticalHolderRequestWithCommon(titles);
        return List.of(MessageUtils.holder("Select client", ButtonsType.KEYBOARD, holderRequest));
    }

    protected abstract void filterContexts(List<Context> userContexts);

    protected abstract List<MessageHolder> getNoUsersResponse(Context context, Department department);

    private String buildButtonTitle(Context context) {
        if ("n/a".equals(context.getPhoneNumber())) {
            return context.getName();
        }
        return String.format("%s, %s", context.getPhoneNumber(), context.getName());
    }

}
