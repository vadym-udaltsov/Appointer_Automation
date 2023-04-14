package com.bot.util;

import com.bot.model.KeyBoardType;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Serhii_Udaltsov on 4/8/2021
 */
public class KeyBoardUtils {

    public static InlineKeyboardMarkup buildDatePicker(Map<String, String> buttonsMap, KeyBoardType type) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        LocalDate currentDate = LocalDate.now();
        int currentMonth = currentDate.getMonthValue();
        int currentYear = currentDate.getYear();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd");

        List<InlineKeyboardButton> firstRow = new ArrayList<>();
        InlineKeyboardButton todayButton = new InlineKeyboardButton();
        todayButton.setText("Today");
        todayButton.setCallbackData("Today");
        firstRow.add(todayButton);

        InlineKeyboardButton tomorrowButton = new InlineKeyboardButton();
        tomorrowButton.setText("Tomorrow");
        tomorrowButton.setCallbackData("Tomorrow");
        firstRow.add(tomorrowButton);
        keyboard.add(firstRow);

        List<InlineKeyboardButton> dayTitlesRow = new ArrayList<>();
        for (String dayTitle : Constants.DAY_TITLES) {
            InlineKeyboardButton titleButton = new InlineKeyboardButton();
            titleButton.setText(dayTitle);
            titleButton.setCallbackData("ignore");
            dayTitlesRow.add(titleButton);
        }
        keyboard.add(dayTitlesRow);

        Month month = Month.of(currentMonth);
        LocalDate date = LocalDate.of(currentYear, currentMonth, 1);
        int daysInMonth = month.length(currentDate.isLeapYear());
        int todaysDay = currentDate.getDayOfMonth();
        for (int i = 1; i <= daysInMonth; i++) {
            if (i == 1 || date.getDayOfWeek().getValue() == 1) {
                List<InlineKeyboardButton> row = new ArrayList<>();
                keyboard.add(row);
            }

            String buttonText;
            if (i < todaysDay) {
                buttonText = Constants.UNAVAILABLE_DATE;
            } else {
                buttonText = date.format(formatter);
            }
            InlineKeyboardButton dateButton = new InlineKeyboardButton();
            dateButton.setText(buttonText);
            dateButton.setCallbackData(buttonText.equals(Constants.UNAVAILABLE_DATE) ? "ignore" : buttonText);
            keyboard.get(keyboard.size() - 1).add(dateButton);

            date = date.plusDays(1);
        }
        Month nextMoth = month.plus(1);
        List<InlineKeyboardButton> lastRow = new ArrayList<>();
        InlineKeyboardButton nextMonthButton = new InlineKeyboardButton();
        nextMonthButton.setText(nextMoth.name());
        nextMonthButton.setCallbackData(nextMoth.name());
        lastRow.add(nextMonthButton);
        keyboard.add(lastRow);
        keyboard.removeIf(r -> r.stream().allMatch(b -> b.getText().equals(Constants.UNAVAILABLE_DATE)));
        inlineKeyboardMarkup.setKeyboard(keyboard);
        return inlineKeyboardMarkup;
    }

    public static InlineKeyboardMarkup buildInlineKeyboard(Map<String, String> buttonsMap, KeyBoardType type) {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        for (Map.Entry<String, String> entry : buttonsMap.entrySet()) {
            if (rowInline.size() == type.getElementsInRow()) {
                rowsInline.add(rowInline);
                rowInline = new ArrayList<>();
            }
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(entry.getKey());
            button.setCallbackData(entry.getValue());
            rowInline.add(button);
        }
        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }

    public static ReplyKeyboardMarkup buildReplyKeyboard(Map<String, String> buttonsMap, KeyBoardType type) {
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
        List<KeyboardRow> rows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        for (Map.Entry<String, String> entry : buttonsMap.entrySet()) {
            KeyboardButton button = new KeyboardButton();
            button.setText(entry.getKey());
            row.add(button);
            if (row.size() == type.getElementsInRow()) {
                rows.add(row);
                row = new KeyboardRow();
            }
        }
        if (row.size() < type.getElementsInRow()) {
            rows.add(row);
        }
        keyboard.setKeyboard(rows);
        keyboard.setOneTimeKeyboard(false);
        return keyboard;
    }

    public static ReplyKeyboardMarkup buildContactsKeyboard(Map<String, String> buttonsMap, KeyBoardType type) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow keyboardFirstRow = new KeyboardRow();
        KeyboardButton keyboardButton = new KeyboardButton();
        keyboardButton.setText("Share your number");
        keyboardButton.setRequestContact(true);
        keyboardFirstRow.add(keyboardButton);

        keyboard.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

}
