package com.bot.processor.impl.general.user.salon;

import com.bot.model.KeyBoardType;
import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.bot.util.Constants;
import com.bot.util.MessageUtils;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

public class SalonDashStepProcessor implements IProcessor {

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        return MessageUtils.buildCustomKeyboardHolders(Constants.Messages.SELECT_ACTION, Constants.SALON_INFO_BUTTONS,
                KeyBoardType.TWO_ROW, true);
    }
}
