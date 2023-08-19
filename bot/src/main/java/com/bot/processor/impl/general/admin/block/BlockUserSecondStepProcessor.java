package com.bot.processor.impl.general.admin.block;

import com.bot.model.BuildKeyboardRequest;
import com.bot.model.ButtonsType;
import com.bot.model.Context;
import com.bot.model.KeyBoardType;
import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.bot.util.Constants;
import com.bot.util.ContextUtils;
import com.bot.util.MessageUtils;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

public class BlockUserSecondStepProcessor implements IProcessor {

    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        Update update = request.getUpdate();
        String selectedTitle = MessageUtils.getTextFromUpdate(update);
        if (Constants.BACK.equals(selectedTitle)) {

        }
        Context context = request.getContext();
        List<String> availableTitles = (List<String>) context.getParams().get(Constants.AVAILABLE_TITLES);
        if (!availableTitles.contains(selectedTitle)) {
            ContextUtils.setPreviousStep(context);
            return MessageUtils.buildCustomKeyboardHolders("Select client from proposed", availableTitles,
                    KeyBoardType.VERTICAL, true);
        }

        ContextUtils.setStringParameter(context, Constants.SELECTED_TITLE, selectedTitle);
        BuildKeyboardRequest holderRequest = MessageUtils.buildVerticalHolderRequestWithCommon(List.of(Constants.SUBMIT));
        return List.of(MessageUtils.holder("Submit client blocking", ButtonsType.KEYBOARD, holderRequest));
    }
}
