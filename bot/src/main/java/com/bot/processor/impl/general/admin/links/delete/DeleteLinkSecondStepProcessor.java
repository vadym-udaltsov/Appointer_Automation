package com.bot.processor.impl.general.admin.links.delete;

import com.bot.model.Context;
import com.bot.model.KeyBoardType;
import com.bot.model.LString;
import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.bot.util.Constants;
import com.bot.util.ContextUtils;
import com.bot.util.MessageUtils;
import com.commons.model.Department;
import com.commons.service.IDepartmentService;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class DeleteLinkSecondStepProcessor implements IProcessor {

    private final IDepartmentService departmentService;

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        Department department = request.getDepartment();
        Update update = request.getUpdate();
        Context context = request.getContext();
        String linkType = MessageUtils.getTextFromUpdate(update);
        Map<String, String> linksMap = department.getLinks();
        if ("".equals(linkType) || !Constants.LINKS_BUTTONS.contains(linkType)) {
            ContextUtils.resetLocationToPreviousStep(context);
            String message = Constants.Messages.INCORRECT_ACTION;
            return MessageUtils.buildCustomKeyboardHolders(message, new ArrayList<>(linksMap.keySet()),
                    KeyBoardType.TWO_ROW, true);
        }
        linksMap.remove(linkType);
        departmentService.updateDepartment(department);
        ContextUtils.resetLocationToStep(context, Constants.Processors.LINKS_DASH);
        List<LString> messageLines = new ArrayList<>();
        messageLines.add(LString.builder().title(Constants.Messages.LINK_DELETE_SUCCESS)
                .placeholders(Map.of("media", linkType)).build());
        messageLines.add(LString.builder().title(Constants.Messages.SELECT_ACTION).build());
        return MessageUtils.buildCustomKeyboardHolders("", Constants.ADMIN_APPOINTMENT_BUTTONS,
                KeyBoardType.TWO_ROW, messageLines, true);
    }
}
