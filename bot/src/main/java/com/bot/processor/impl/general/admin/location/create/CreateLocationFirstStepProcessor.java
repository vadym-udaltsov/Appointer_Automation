package com.bot.processor.impl.general.admin.location.create;

import com.bot.model.KeyBoardType;
import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.bot.util.MessageUtils;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

public class CreateLocationFirstStepProcessor implements IProcessor {

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        return MessageUtils.buildCustomKeyboardHolders("Send your current location", List.of(),
                KeyBoardType.TWO_ROW, true);
    }
}
