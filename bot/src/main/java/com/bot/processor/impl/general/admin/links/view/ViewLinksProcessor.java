package com.bot.processor.impl.general.admin.links.view;

import com.bot.model.Context;
import com.bot.model.KeyBoardType;
import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.bot.util.Constants;
import com.bot.util.ContextUtils;
import com.bot.util.MessageUtils;
import com.commons.model.Department;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Map;

public class ViewLinksProcessor implements IProcessor {

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        Context context = request.getContext();
        Department department = request.getDepartment();
        Map<String, String> linksMap = department.getLinks();
        ContextUtils.resetLocationToStep(context, Constants.Processors.LINKS_DASH);

        return MessageUtils.buildSocialLinksViewHolders(linksMap, "View social media", Constants.Messages.SELECT_ACTION,
                Constants.ADMIN_APPOINTMENT_BUTTONS, KeyBoardType.TWO_ROW, true);
    }
}