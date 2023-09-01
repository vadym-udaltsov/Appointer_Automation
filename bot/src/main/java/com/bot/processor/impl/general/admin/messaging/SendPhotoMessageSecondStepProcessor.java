package com.bot.processor.impl.general.admin.messaging;

import com.bot.model.Context;
import com.bot.model.LString;
import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.bot.util.Constants;
import com.bot.util.ContextUtils;
import com.bot.util.MessageUtils;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class SendPhotoMessageSecondStepProcessor implements IProcessor {

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        Context context = request.getContext();
        Update update = request.getUpdate();
        List<String> buttons = List.of("Submit");

        if (!update.getMessage().hasPhoto()) {
            String message = Constants.Messages.INPUT_MESSAGE_IMAGE;
            ContextUtils.resetLocationToPreviousStep(context);
            return List.of(MessageUtils.buildKeyboardHolder(message, List.of(), List.of()));
        }

        List<LString> messageLines = new ArrayList<>();
        String photoFileId = update.getMessage().getPhoto().get(0).getFileId();
        context.getParams().put(Constants.PHOTO_ID_FOR_SUBMIT, photoFileId);
        messageLines.add(LString.builder().title(Constants.Messages.SUBMIT_MESSAGE_IMAGE).build());

        return List.of(MessageUtils.buildKeyboardHolder("", messageLines, buttons));
    }
}
