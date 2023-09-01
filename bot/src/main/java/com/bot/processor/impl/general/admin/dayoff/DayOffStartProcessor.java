package com.bot.processor.impl.general.admin.dayoff;

import com.bot.model.KeyBoardType;
import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.bot.util.Constants;
import com.bot.util.MessageUtils;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

public class DayOffStartProcessor implements IProcessor {

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        return MessageUtils.buildCustomKeyboardHolders("Select action", Constants.START_DAYOFF_BUTTONS,
                KeyBoardType.TWO_ROW, true);
    }
}
