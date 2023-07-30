package com.bot.processor.impl.general.admin.dayoff.cancel;

import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.bot.processor.impl.general.admin.dayoff.DayOffFirstStepProcessor;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

public class CancelDayOffFirstStepProcessor extends DayOffFirstStepProcessor implements IProcessor {

    public CancelDayOffFirstStepProcessor(IProcessor nextStepProcessor) {
        super(nextStepProcessor);
    }

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        return buildFirstStepDayOffResponse(request);
    }
}
