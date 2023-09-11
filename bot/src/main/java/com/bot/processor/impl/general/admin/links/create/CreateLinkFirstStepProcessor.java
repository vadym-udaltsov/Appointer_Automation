package com.bot.processor.impl.general.admin.links.create;

import com.bot.model.KeyBoardType;
import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.bot.util.Constants;
import com.bot.util.MessageUtils;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

public class CreateLinkFirstStepProcessor implements IProcessor {

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        String message = "Select type of link to create";
        return MessageUtils.buildCustomKeyboardHolders(message, Constants.LINKS_BUTTONS,
                KeyBoardType.TWO_ROW, true);
    }
}
