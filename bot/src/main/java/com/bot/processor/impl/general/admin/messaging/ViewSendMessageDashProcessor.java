package com.bot.processor.impl.general.admin.messaging;

import com.bot.model.KeyBoardType;
import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.bot.util.Constants;
import com.bot.util.MessageUtils;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

public class ViewSendMessageDashProcessor implements IProcessor {

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        return MessageUtils.buildCustomKeyboardHolders(Constants.Messages.SELECT_ACTION, Constants.VIEW_ADMIN_MESSAGES_BUTTONS,
                KeyBoardType.TWO_ROW, true);
    }
}
