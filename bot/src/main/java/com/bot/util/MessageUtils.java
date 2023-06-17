package com.bot.util;

import com.bot.model.Appointment;
import com.bot.model.BuildKeyboardRequest;
import com.bot.model.Button;
import com.bot.model.ButtonsType;
import com.bot.model.KeyBoardType;
import com.bot.model.LString;
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

    public static final List<String> DASHBOARD = Arrays.asList("Create appointment", "My appointments");

    public static MessageHolder getLanguageMessageHolder() {
        BuildKeyboardRequest request = BuildKeyboardRequest.builder()
                .type(KeyBoardType.VERTICAL)
                .buttonsMap(buildButtons(commonButtons(getFlags()), false))
                .build();
        return holder("Select language", ButtonsType.INLINE, request);
    }

    public static MessageHolder getBotNotReadyMessageHolder() {
        BuildKeyboardRequest request = BuildKeyboardRequest.builder()
                .type(KeyBoardType.VERTICAL)
                .buttonsMap(buildButtons(commonButtons(List.of("Try bot")), false))
                .build();
        return holder("Bot is not ready, try later", ButtonsType.KEYBOARD, request);
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
        return buildDashboardHolder("", List.of());
    }

    public static MessageHolder buildDashboardHolder(List<LString> messageLines) {
        return buildDashboardHolder("", messageLines);
    }

    public static MessageHolder buildDashboardHolderByKey(String strategyKey) {
        BuildKeyboardRequest request = BuildKeyboardRequest.builder()
                .type(KeyBoardType.TWO_ROW)
                .buttonsMap(buildButtons(commonButtons(Constants.DASHBOARD_BUTTONS.get(strategyKey)), false))
                .build();
        return holder("Select action", ButtonsType.KEYBOARD, request);
    }

    public static MessageHolder buildDashboardHolder(String message) {
        return buildDashboardHolder(message, List.of());
    }

    public static MessageHolder buildDashboardHolder(String message, List<LString> messageLines) {
        String result = "Select action";
        if (!message.isBlank()) {
            result = message + result;
        }
        BuildKeyboardRequest request = BuildKeyboardRequest.builder()
                .type(KeyBoardType.TWO_ROW)
                .buttonsMap(buildButtons(commonButtons(DASHBOARD), false))
                .build();
        return holder(result, ButtonsType.KEYBOARD, request, messageLines);
    }

    public static MessageHolder holder(String message, ButtonsType buttonsType,
                                       BuildKeyboardRequest request) {
        return MessageHolder.builder()
                .message(message)
                .buttonsType(buttonsType)
                .keyboardRequest(request)
                .build();
    }

    public static MessageHolder holder(String message, ButtonsType buttonsType, BuildKeyboardRequest request,
                                       List<LString> messageLines) {
        return MessageHolder.builder()
                .message(message)
                .buttonsType(buttonsType)
                .messagesToLocalize(messageLines)
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

    public static void fillMessagesToLocalize(List<LString> messagesToLocalize, Appointment appointment) {
        String specialist = appointment.getSpecialist();
        String service = appointment.getService();
        int duration = appointment.getDuration();
        long date = appointment.getDate();
        String dateTitle = DateUtils.getDateTitle(date);
        messagesToLocalize.add(LString.builder().title("Service: ${service}").placeholders(Map.of("service", service)).build());
        if (!"owner".equals(specialist)) {
            messagesToLocalize.add(LString.builder().title("Specialist: ${specialist}").placeholders(Map.of("specialist", specialist)).build());
        }
        messagesToLocalize.add(LString.builder().title("Date: ${date}").placeholders(Map.of("date", dateTitle.split(",")[0])).build());
        messagesToLocalize.add(LString.builder().title("Time: ${time}").placeholders(Map.of("time", dateTitle.split(",")[1])).build());
        messagesToLocalize.add(LString.builder().title("Duration: ${duration} min").placeholders(Map.of("duration", String.valueOf(duration))).build());
    }
}
