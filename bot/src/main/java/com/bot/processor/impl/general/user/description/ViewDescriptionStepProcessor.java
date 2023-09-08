package com.bot.processor.impl.general.user.description;

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

public class ViewDescriptionStepProcessor implements IProcessor {

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        Context context = request.getContext();
        Department department = request.getDepartment();
        String message = "Description is not set yet";
        String description = department.getDescription();
        if (description != null && !"".equals(description)) {
            message = description;
        }
        ContextUtils.resetLocationToStep(context, "salonDash");
        return MessageUtils.buildCustomKeyboardHolders(message, Constants.SALON_INFO_BUTTONS,
                KeyBoardType.TWO_ROW, true);
    }
}
