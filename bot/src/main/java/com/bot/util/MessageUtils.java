package com.bot.util;

import com.commons.model.Appointment;
import com.bot.model.BuildKeyboardRequest;
import com.bot.model.Button;
import com.bot.model.ButtonsType;
import com.bot.model.Context;
import com.bot.model.KeyBoardType;
import com.bot.model.LString;
import com.bot.model.Language;
import com.bot.model.MessageHolder;
import com.bot.model.MessageTemplate;
import com.commons.model.Department;
import com.commons.utils.DateUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import software.amazon.awssdk.utils.StringUtils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class MessageUtils {

    public static MessageHolder getLanguageMessageHolder() {
        return getLanguageMessageHolder("Select language");
    }

    public static MessageHolder getLanguageMessageHolder(String message) {
        BuildKeyboardRequest request = BuildKeyboardRequest.builder()
                .type(KeyBoardType.VERTICAL)
                .buttonsMap(buildButtons(commonButtons(getFlags()), false))
                .build();
        return holder(message, ButtonsType.INLINE, request);
    }

    public static MessageHolder getBotNotReadyMessageHolder() {
        BuildKeyboardRequest request = BuildKeyboardRequest.builder()
                .type(KeyBoardType.VERTICAL)
                .buttonsMap(buildButtons(commonButtons(List.of("Try bot")), false))
                .build();
        return holder("Bot is not ready, try later", ButtonsType.KEYBOARD, request);
    }

    public static MessageHolder getClientBlockedMessageHolder() {
        BuildKeyboardRequest request = BuildKeyboardRequest.builder()
                .type(KeyBoardType.VERTICAL)
                .buttonsMap(buildButtons(commonButtons(List.of("Try bot")), false))
                .build();
        return holder("You are blocked", ButtonsType.KEYBOARD, request);
    }

    public static List<MessageHolder> buildProfileDashboard(String message) {
        return buildCustomKeyboardHolders(message, Constants.PROFILE_BUTTONS,
                KeyBoardType.TWO_ROW, true);
    }

    public static List<MessageHolder> buildAdminAppointmentsDashboard() {
        return buildCustomKeyboardHolders("Select action", Constants.ADMIN_APPOINTMENT_BUTTONS,
                KeyBoardType.TWO_ROW, true);
    }

    public static List<MessageHolder> buildStartBlockDashboard() {
        return buildCustomKeyboardHolders("Select action", Constants.START_BLOCK_BUTTONS,
                KeyBoardType.TWO_ROW, true);
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

    public static List<MessageHolder> buildDatePicker(Set<String> stringSet, String message, boolean isNextMonth,
                                                      Department department) {
        BuildKeyboardRequest datePickerRequest = BuildKeyboardRequest.builder()
                .params(Map.of(
                        Constants.IS_NEXT_MONTH, isNextMonth,
                        Constants.USER_APPOINTMENTS, stringSet))
                .build();
        Month month = DateUtils.nowZoneDateTime(department).getMonth();
//        Month month = LocalDate.now().getMonth();
        MessageHolder datePicker = MessageUtils.holder(month.name(), ButtonsType.DATE_PICKER_MY_APP, datePickerRequest);
        BuildKeyboardRequest commonsRequest = BuildKeyboardRequest.builder()
                .type(KeyBoardType.TWO_ROW)
                .buttonsMap(MessageUtils.buildButtons(List.of(), true))
                .build();
        MessageHolder commonButtonsHolder = MessageUtils.holder(message, ButtonsType.KEYBOARD, commonsRequest);
        return List.of(commonButtonsHolder, datePicker);
    }

    public static MessageHolder buildDashboardHolder(String message, List<LString> messageLines, String strategyKey) {
        BuildKeyboardRequest request = BuildKeyboardRequest.builder()
                .type(KeyBoardType.TWO_ROW)
                .buttonsMap(buildButtons(commonButtons(Constants.DASHBOARD_BUTTONS.get(strategyKey)), false))
                .build();
        return holder(message, ButtonsType.KEYBOARD, request, messageLines);
    }

    public static List<MessageHolder> buildCustomKeyboardHolders(String message, List<String> buttonTitles, KeyBoardType keyBoardType,
                                                         boolean withCommon) {
        ArrayList<MessageHolder> response = new ArrayList<>();
        BuildKeyboardRequest holderRequest = BuildKeyboardRequest.builder()
                .type(keyBoardType)
                .buttonsMap(MessageUtils.buildButtons(MessageUtils.commonButtons(buttonTitles), withCommon))
                .build();
        response.add(MessageUtils.holder(message, ButtonsType.KEYBOARD, holderRequest));
        return response;
    }

    public static List<MessageHolder> buildCustomKeyboardHolders(String message, List<String> buttonTitles,
                                                                 KeyBoardType keyBoardType, List<LString> messageLines,
                                                                 boolean withCommon) {
        ArrayList<MessageHolder> response = new ArrayList<>();
        BuildKeyboardRequest holderRequest = BuildKeyboardRequest.builder()
                .type(keyBoardType)
                .buttonsMap(MessageUtils.buildButtons(MessageUtils.commonButtons(buttonTitles), withCommon))
                .build();
        response.add(MessageUtils.holder(message, ButtonsType.KEYBOARD, holderRequest, messageLines));
        return response;
    }

    public static MessageHolder holder(String message, ButtonsType buttonsType,
                                       BuildKeyboardRequest request) {
        return MessageHolder.builder()
                .message(message)
                .buttonsType(buttonsType)
                .keyboardRequest(request)
                .build();
    }

    public static MessageHolder holder(String message) {
        return MessageHolder.builder()
                .message(message)
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

    public static BuildKeyboardRequest buildVerticalHolderRequestWithCommon(List<String> titles) {
        List<Button> buttons = titles.stream()
                .map(s -> Button.builder()
                        .value(s)
                        .build())
                .collect(Collectors.toList());
        return BuildKeyboardRequest.builder()
                .type(KeyBoardType.VERTICAL)
                .buttonsMap(MessageUtils.buildButtons(buttons, true))
                .build();
    }

    public static void fillMessagesToLocalize(List<LString> messagesToLocalize, Appointment appointment,
                                              Context userContext, MessageTemplate template) {
        String specialist = appointment.getSpecialist();
        String service = appointment.getService();
        int duration = appointment.getDuration();
        long date = appointment.getDate();
        String dateTitle = DateUtils.getDateTitle(date);
        LocalDateTime startDate = LocalDateTime.ofInstant(Instant.ofEpochSecond(date), ZoneId.systemDefault());
        LocalDateTime endDate = startDate.plus(duration, ChronoUnit.MINUTES);
        String startTime = startDate.format(DateTimeFormatter.ofPattern("HH:mm"));
        String endTime = endDate.format(DateTimeFormatter.ofPattern("HH:mm"));
        Map<String, LString> messagesMap = new HashMap<>();
        messagesMap.put("date", LString.builder().title("Date: ${date}").placeholders(Map.of("date", dateTitle.split(",")[0])).build());
        messagesMap.put("time", LString.builder().title("Time from ${timeFrom} to ${timeTo}").placeholders(Map.of("timeFrom", startTime, "timeTo", endTime)).build());
        messagesMap.put("service", LString.builder().title("Service: ${service}").placeholders(Map.of("service", service)).build());
        messagesMap.put("specialist", LString.builder().title("Specialist: ${specialist}").placeholders(Map.of("specialist", specialist)).build());
        if (userContext != null) {
            messagesMap.put("client", LString.builder().title("Client: ${client}").placeholders(Map.of("client", userContext.getName())).build());
            if (!"n/a".equals(userContext.getPhoneNumber())) {
                messagesMap.put("clientPhone", LString.builder().title("Phone Number: ${phone}").placeholders(Map.of("phone", userContext.getPhoneNumber())).build());
                Map<String, String> clientPhonePlaceholder = Map.of("client", userContext.getName(), "phone", userContext.getPhoneNumber());
                messagesMap.put("clientWithPhone", LString.builder().title(Constants.Messages.APP_CLIENT_INFO).placeholders(clientPhonePlaceholder).build());
            } else {
                messagesMap.put("clientWithPhone", LString.builder().title(Constants.Messages.CLIENT_INFO).placeholders(Map.of("client", userContext.getName())).build());
            }
        }
        template.buildMessages(messagesToLocalize, messagesMap);
    }

    public static List<LString> buildNotificationForAdmins(List<LString> messagesToLocalize, Context context,
                                                           Department department) {
        List<LString> adminMessages = new ArrayList<>();
        adminMessages.add(LString.builder().title(Constants.STAR_SIGN).build());
        adminMessages.addAll(messagesToLocalize);
        adminMessages.add(LString.builder().title("Client: ${client}").placeholders(Map.of("client", context.getName())).build());
        if (!"n/a".equalsIgnoreCase(context.getPhoneNumber())) {
            adminMessages.add(LString.builder().title("Phone Number: ${phone}").placeholders(Map.of("phone", context.getPhoneNumber())).build());
        }
        if (!"default".equalsIgnoreCase(department.getName())) {
            adminMessages.add(LString.builder().title("Department: ${department}").placeholders(Map.of("department", department.getName())).build());
        }
        return adminMessages;
    }

    public static MessageHolder buildKeyboardHolder(String message, List<LString> messageLines, List<String> buttons) {
        BuildKeyboardRequest request = BuildKeyboardRequest.builder()
                .type(KeyBoardType.THREE_ROW)
                .buttonsMap(MessageUtils.buildButtons(MessageUtils.commonButtons(buttons), true))
                .build();
        return MessageUtils.holder(message, ButtonsType.KEYBOARD, request, messageLines);
    }
}
