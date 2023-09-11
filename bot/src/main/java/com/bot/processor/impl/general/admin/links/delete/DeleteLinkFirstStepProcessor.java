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
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DeleteLinkFirstStepProcessor implements IProcessor {

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        Department department = request.getDepartment();
        Context context = request.getContext();
        Map<String, String> linksMap = department.getLinks();
        List<LString> messageLines = new ArrayList<>();
        if (linksMap == null || linksMap.size() == 0) {
            messageLines.add(LString.builder().title(Constants.Messages.LINKS_NOT_EXIST).build());
            ContextUtils.resetLocationToStep(context, Constants.Processors.LINKS_DASH);
            return MessageUtils.buildCustomKeyboardHolders(Constants.Messages.SELECT_ACTION, Constants.ADMIN_APPOINTMENT_BUTTONS,
                    KeyBoardType.TWO_ROW, messageLines, true);
        }
        String message = "Select type of link to delete";
        return MessageUtils.buildCustomKeyboardHolders(message, new ArrayList<>(linksMap.keySet()),
                KeyBoardType.TWO_ROW, true);
    }
}
