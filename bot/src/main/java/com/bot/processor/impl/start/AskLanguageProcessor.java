package com.bot.processor.impl.start;

import com.bot.model.Context;
import com.bot.model.Language;
import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.bot.service.IContextService;
import com.bot.service.IDictionaryService;
import com.bot.util.Constants;
import com.bot.util.MessageUtils;
import com.commons.model.Department;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class AskLanguageProcessor implements IProcessor {

    private final IContextService contextService;
    private final IDictionaryService dictionaryService;

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) {
        long userId = MessageUtils.getUserIdFromUpdate(request.getUpdate());
        Context context = request.getContext();
        Department department = request.getDepartment();
        if (departmentNotReady(department)) {
            return Collections.singletonList(MessageUtils.getBotNotReadyMessageHolder());
        }
        if (context == null) {
            context = buildContext(userId, department.getId());
            contextService.create(context);
        }

        List<String> dictKeys = dictionaryService.getDictionaryFileKeys(department);

        MessageHolder messageHolder = MessageUtils.getLanguageMessageHolder(MessageUtils.filterLanguages(dictKeys, department));
        return Collections.singletonList(messageHolder);
    }

    private boolean departmentNotReady(Department department) {
        return department.getServices() == null || department.getServices().size() == 0
                || department.getZone() == null || "".equals(department.getZone())
                || department.getAvailableSpecialists() == null || department.getAvailableSpecialists().size() == 0
                || department.getStartWork() == 0
                || department.getEndWork() == 0
                || department.getStartWork() > department.getEndWork();
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
