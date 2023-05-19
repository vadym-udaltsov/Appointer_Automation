package com.bot.processor.impl.start;

import com.bot.model.Context;
import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.bot.service.IContextService;
import com.bot.util.Constants;
import com.bot.util.MessageUtils;
import com.commons.model.Department;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class AskLanguageProcessor implements IProcessor {

    private final IContextService contextService;

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) {
        long userId = MessageUtils.getUserIdFromUpdate(request.getUpdate());
        MessageHolder messageHolder = MessageUtils.getLanguageMessageHolder();
        Context context = request.getContext();
        Department department = request.getDepartment();
        if (context == null) {
            context = buildContext(userId, department.getId());
            contextService.create(context);
        }
        return Collections.singletonList(messageHolder);
    }

    private Context buildContext(long userId, String departmentId) {
        Context context = new Context();
        context.setUserId(userId);
        context.setDepartmentId(departmentId);
        context.setNavigation(List.of(Constants.Processors.ASK_LANG));
        context.setParams(Map.of());
        return context;
    }

}
