package com.bot.util;

import com.bot.model.Button;
import com.bot.model.ButtonsType;
import com.bot.model.KeyBoardType;
import com.bot.model.Language;
import com.bot.model.MessageHolder;
import org.apache.commons.lang3.StringUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

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
        return holder(MessageUtils.getFlags(), "Select language",
                KeyBoardType.VERTICAL, false, ButtonsType.INLINE);
    }

    public static List<String> getFlags() {
        return Arrays.stream(Language.values())
                .map(Language::getValue)
                .collect(Collectors.toList());
    }

    public static MessageHolder getContactsMessageHolder() {
        return holder(Collections.singletonList(Constants.Messages.SHARE_CONTACT), Constants.Messages.SHARE_CONTACT,
                KeyBoardType.VERTICAL, false, ButtonsType.CONTACTS);
    }

    public static long getUserIdFromUpdate(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        if (callbackQuery != null) {
            return callbackQuery.getFrom().getId();
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
        return holder(DASHBOARD, "Select action", KeyBoardType.TWO_ROW, false,
                ButtonsType.KEYBOARD);
    }

    public static MessageHolder holder(List<String> titles, String message, KeyBoardType type,
                                       boolean withCommonButtons, ButtonsType buttonsType) {
        return MessageHolder.builder()
                .withCommonButtons(withCommonButtons)
                .message(message)
                .buttons(commonButtons(titles))
                .buttonsType(buttonsType)
                .keyBoardType(type)
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
        Map<String, String> buttonsMap = buildButtons(holder.getButtons(), holder.isWithCommonButtons());
        ReplyKeyboard keyboard = holder.getButtonsType()
                .getButtonsFunction()
                .apply(buttonsMap, holder.getKeyBoardType());
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

    public static Map<String, String> commonButtonsMap() {
        Map<String, String> buttons = new HashMap<>();
        buttons.put("Back", "back");
        buttons.put("Home", "home");
        return buttons;
    }
}
