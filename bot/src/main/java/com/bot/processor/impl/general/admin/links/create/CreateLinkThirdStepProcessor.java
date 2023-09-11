package com.bot.processor.impl.general.admin.links.create;

import com.bot.model.Context;
import com.bot.model.KeyBoardType;
import com.bot.model.LString;
import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.bot.util.Constants;
import com.bot.util.ContextUtils;
import com.bot.util.MessageUtils;
import com.bot.util.StringUtils;
import com.commons.model.Department;
import com.commons.service.IDepartmentService;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class CreateLinkThirdStepProcessor implements IProcessor {

    private final IDepartmentService departmentService;

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        Update update = request.getUpdate();
        Context context = request.getContext();
        Department department = request.getDepartment();
        List<LString> messageLines = new ArrayList<>();
        String link = MessageUtils.getTextFromUpdate(update);
        String linkType = ContextUtils.getStringParam(context, Constants.LINK_TYPE);
        if (!isLinkValid(link)) {
            ContextUtils.resetLocationToPreviousStep(context);
            messageLines.add(LString.builder().title("Enter correct link").build());
            messageLines.add(LString.builder().title(Constants.Messages.LINK_CREATE_PROMPT)
                    .placeholders(Map.of("media", linkType)).build());
            return MessageUtils.buildCustomKeyboardHolders("", List.of(),
                    KeyBoardType.TWO_ROW, messageLines, true);
        }
        Map<String, String> links = department.getLinks();
        if (links == null) {
            links = new HashMap<>();
        }
        links.put(linkType, link);
        department.setLinks(links);
        departmentService.updateDepartment(department);
        ContextUtils.resetLocationToStep(context, Constants.Processors.LINKS_DASH);
        messageLines.add(LString.builder().title(Constants.Messages.LINK_CREATE_SUCCESS)
                .placeholders(Map.of("media", linkType)).build());
        messageLines.add(LString.builder().title(Constants.Messages.SELECT_ACTION).build());
        return MessageUtils.buildCustomKeyboardHolders("", Constants.ADMIN_APPOINTMENT_BUTTONS,
                KeyBoardType.TWO_ROW, messageLines, true);
    }

    private boolean isLinkValid(String link) {
        List<String> regexList = List.of("https:\\/\\/.*", "http:\\/\\/.*", "tg:\\/\\/.*");
        for (String regex : regexList) {
            if (StringUtils.isMatch(link, regex)) {
                return true;
            }
        }
        return false;
    }
}
