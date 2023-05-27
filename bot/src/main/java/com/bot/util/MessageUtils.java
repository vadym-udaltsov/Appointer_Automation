package com.bot.util;

import com.bot.model.BuildKeyboardRequest;
import com.bot.model.Button;
import com.bot.model.ButtonsType;
import com.bot.model.KeyBoardType;
import com.bot.model.Language;
import com.bot.model.MessageHolder;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import software.amazon.awssdk.utils.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MessageUtils {

    private static final List<String> DASHBOARD = Arrays.asList("Create appointment", "My appointments");

    public static MessageHolder getLanguageMessageHolder() {
        BuildKeyboardRequest request = BuildKeyboardRequest.builder()
                .type(KeyBoardType.VERTICAL)
                .buttonsMap(buildButtons(commonButtons(getFlags()), false))
                .build();
        return holder("Select language", ButtonsType.INLINE, request);
    }

    public static List<String> getFlags() {
        return Arrays.stream(Language.values())
                .map(Language::getValue)
                .collect(Collectors.toList());
    }

    public static MessageHolder getContactsMessageHolder() {
        BuildKeyboardRequest request = BuildKeyboardRequest.builder()
                .type(KeyBoardType.VERTICAL)
                .buttonsMap(buildButtons(commonButtons(Collections.singletonList(Constants.Messages.SHARE_CONTACT)), false))
                .build();
        return holder(Constants.Messages.SHARE_CONTACT, ButtonsType.CONTACTS, request);
    }

    public static long getUserIdFromUpdate(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        if (callbackQuery != null) {
            return callbackQuery.getFrom().getId();
        }
        Message editedMessage = update.getEditedMessage();
        if (editedMessage != null) {
            return editedMessage.getChat().getId();
        }
        return update.getMessage().getFrom().getId();
    }

    public static Contact getPhoneNumberFromUpdate(Update update) {
        Message message = update.getMessage();
        if (message == null) {
            return null;
        }
        return message.getContact();
    }

    public static MessageHolder buildDashboardHolder() {
        return buildDashboardHolder("");
    }

    public static MessageHolder buildDashboardHolder(String message) {
        String result = "Select action";
        if (!message.isBlank()) {
            result = message + result;
        }
        BuildKeyboardRequest request = BuildKeyboardRequest.builder()
                .type(KeyBoardType.TWO_ROW)
                .buttonsMap(buildButtons(commonButtons(DASHBOARD), false))
                .build();
        return holder(result, ButtonsType.KEYBOARD, request);
    }

    public static MessageHolder holder(String message, ButtonsType buttonsType,
                                       BuildKeyboardRequest request) {
        return MessageHolder.builder()
                .message(message)
                .buttonsType(buttonsType)
                .keyboardRequest(request)
                .build();
    }

    public static List<Button> commonButtons(List<String> titles) {
        return titles.stream()
                .map(t -> Button.builder()
                        .value(t)
                        .build())
                .collect(Collectors.toList());
    }

    public static SendMessage buildMessage(MessageHolder holder, long operatorId) {
        SendMessage sendMessage = new SendMessage(String.valueOf(operatorId), holder.getMessage());
        ReplyKeyboard keyboard = holder.getButtonsType()
                .getButtonsFunction()
                .apply(holder.getKeyboardRequest());
        sendMessage.setReplyMarkup(keyboard);
        return sendMessage;
    }

    public static Map<String, String> buildButtons(List<Button> buttons, boolean withCommon) {
        Map<String, String> resultMap = new LinkedHashMap<>();
        for (Button button : buttons) {
            resultMap.put(button.getValue(), StringUtils.isBlank(button.getCallback())
                    ? button.getValue()
                    : button.getCallback());
        }
        if (withCommon) {
            resultMap.putAll(commonButtonsMap());
        }
        return resultMap;
    }

    public static String getTextFromUpdate(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        if (callbackQuery != null) {
            return callbackQuery.getData();
        }
        return update.getMessage().getText();
    }

    public static void setTextToUpdate(Update update, String text) {
        update.setCallbackQuery(null);
        Message message = new Message();
        message.setText(text);
        update.setMessage(message);
    }

    public static Map<String, String> commonButtonsMap() {
        Map<String, String> buttons = new HashMap<>();
        buttons.put("Back", "back");
        buttons.put("Home", "home");
        return buttons;
    }

    public static BuildKeyboardRequest buildVerticalHolderRequestWithCommon(List<String> availableSpecialists) {
        List<Button> buttons = availableSpecialists.stream()
                .map(s -> Button.builder()
                        .value(s)
                        .build())
                .collect(Collectors.toList());
        return BuildKeyboardRequest.builder()
                .type(KeyBoardType.VERTICAL)
                .buttonsMap(MessageUtils.buildButtons(buttons, true))
                .build();

    }
}
