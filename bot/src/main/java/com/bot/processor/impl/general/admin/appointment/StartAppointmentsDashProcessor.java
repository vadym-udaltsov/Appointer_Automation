package com.bot.processor.impl.general.admin.appointment;

import com.bot.model.BuildKeyboardRequest;
import com.bot.model.ButtonsType;
import com.bot.model.KeyBoardType;
import com.bot.model.LString;
import com.bot.model.MessageHolder;
import com.bot.model.ProcessRequest;
import com.bot.processor.IProcessor;
import com.bot.util.Constants;
import com.bot.util.MessageUtils;
import com.bot.util.StringUtils;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

public class StartAppointmentsDashProcessor implements IProcessor {
    @Override
    public List<MessageHolder> processRequest(ProcessRequest request) throws TelegramApiException {
        Update update = request.getUpdate();
        List<String> buttons = List.of("Today", "Tomorrow", "By Date");
        String textFromUpdate = MessageUtils.getTextFromUpdate(update);
        String message = Constants.Messages.INCORRECT_ACTION;
        if (!StringUtils.isBlank(textFromUpdate) && (textFromUpdate.equals(Constants.BACK) || textFromUpdate.equals("Appointments"))) {
            message = Constants.Messages.SELECT_ACTION;
        }
        return List.of(buildKeyboardHolder(message, List.of(), buttons));
    }

    private MessageHolder buildKeyboardHolder(String message, List<LString> messageLines, List<String> buttons) {
        BuildKeyboardRequest request = BuildKeyboardRequest.builder()
                .type(KeyBoardType.THREE_ROW)
                .buttonsMap(MessageUtils.buildButtons(MessageUtils.commonButtons(buttons), true))
                .build();
        return MessageUtils.holder(message, ButtonsType.KEYBOARD, request, messageLines);
    }
}
