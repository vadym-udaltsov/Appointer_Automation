package com.bot.util;

import com.bot.model.BuildKeyboardRequest;
import com.bot.model.Context;
import com.bot.model.KeyBoardType;
import com.commons.model.Department;
import lombok.extern.slf4j.Slf4j;
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
import java.util.Set;
import java.util.stream.IntStream;

/**
 * @author Serhii_Udaltsov on 4/8/2021
 */
@Slf4j
public class KeyBoardUtils {

    public static InlineKeyboardMarkup buildDatePickerMyAppointments(BuildKeyboardRequest request) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        Map<String, Object> params = request.getParams();
        Set<String> appointments = (Set<String>) params.get(Constants.USER_APPOINTMENTS);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd");
        List<InlineKeyboardButton> dayTitlesRow = new ArrayList<>();
        for (String dayTitle : Constants.DAY_TITLES) {
            InlineKeyboardButton titleButton = new InlineKeyboardButton();
            titleButton.setText(dayTitle);
            titleButton.setCallbackData(Constants.IGNORE);
            dayTitlesRow.add(titleButton);
        }
        keyboard.add(dayTitlesRow);

        LocalDate currentDate = LocalDate.now();
        int currentMonth = currentDate.getMonthValue();
        int currentYear = currentDate.getYear();
        boolean isNextMonth = (boolean) params.get(Constants.IS_NEXT_MONTH);
        if (isNextMonth) {
            currentMonth++;
        }
        Month month = Month.of(currentMonth);
        LocalDate date = LocalDate.of(currentYear, currentMonth, 1);
        int daysInMonth = month.length(currentDate.isLeapYear());
        Map<String, String> numbers = Constants.Numbers.SPEC_NUMBERS;
        for (int i = 1; i <= daysInMonth; i++) {
            if (i == 1 || date.getDayOfWeek().getValue() == 1) {
                List<InlineKeyboardButton> row = new ArrayList<>();
                keyboard.add(row);
            }
            String buttonText = date.format(formatter);
            String buttonTitle = null;
            if (appointments.contains(buttonText)) {
                buttonTitle = numbers.get(buttonText);
            }
            InlineKeyboardButton dateButton = new InlineKeyboardButton();
            dateButton.setText(buttonTitle == null ? buttonText : buttonTitle);
            dateButton.setCallbackData(buttonText);
            int previousMonthDays = date.getDayOfWeek().getValue() - i;
            if (i == 1 && previousMonthDays != 0) {
                InlineKeyboardButton emptyButton = new InlineKeyboardButton();
                emptyButton.setText(Constants.EMPTY_DATE);
                emptyButton.setCallbackData(Constants.IGNORE);
                IntStream.range(0, previousMonthDays).forEach(n -> keyboard.get(keyboard.size() - 1).add(emptyButton));
            }
            keyboard.get(keyboard.size() - 1).add(dateButton);
            date = date.plusDays(1);
        }
        List<InlineKeyboardButton> lastDaysRow = keyboard.get(keyboard.size() - 1);
        int lastDaysRowSize = lastDaysRow.size();
        if (lastDaysRowSize < Constants.DAYS_IN_WEEK) {
            InlineKeyboardButton emptyButton = new InlineKeyboardButton();
            emptyButton.setText(Constants.EMPTY_DATE);
            emptyButton.setCallbackData(Constants.IGNORE);
            IntStream.range(lastDaysRowSize, Constants.DAYS_IN_WEEK).forEach(n -> lastDaysRow.add(emptyButton));
        }
        Month nextMoth = month.plus(isNextMonth ? -1 : 1);
        List<InlineKeyboardButton> lastRow = new ArrayList<>();
        InlineKeyboardButton nextMonthButton = new InlineKeyboardButton();
        nextMonthButton.setText(nextMoth.name());
        nextMonthButton.setCallbackData(isNextMonth ? Constants.CURRENT_MONTH : Constants.NEXT_MONTH);
        lastRow.add(nextMonthButton);
        keyboard.add(lastRow);
        keyboard.removeIf(r -> r.stream().allMatch(b -> Constants.UNAVAILABLE_DATE.equals(b.getText())));
        inlineKeyboardMarkup.setKeyboard(keyboard);
//        context.getParams().put(Constants.AVAILABLE_DATES, availableDates);
        return inlineKeyboardMarkup;
    }

    public static InlineKeyboardMarkup buildDatePickerCreateAppointment(BuildKeyboardRequest request) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        Map<String, Object> params = request.getParams();
        Department department = (Department) params.get(Constants.DEPARTMENT);
        boolean isNextMonth = (boolean) params.get(Constants.IS_NEXT_MONTH);
        Context context = (Context) params.get(Constants.CONTEXT);
        List<Integer> nonWorkingDays = department.getNonWorkingDays();

        LocalDate currentDate = LocalDate.now();
        int currentMonth = currentDate.getMonthValue();
        int currentYear = currentDate.getYear();
        int today = currentDate.getDayOfMonth();

        long endOfWorkingDay = DateUtils.getPointOfDay(currentMonth, today, department.getEndWork());
        long now = DateUtils.now(department);

        boolean todayIsFinished = now > endOfWorkingDay;
        Set<String> busyDays = request.getButtonsMap().keySet();

        if (isNextMonth) {
            currentMonth++;
            today = 1;
            todayIsFinished = false;
        } else {
            int todayInWeek = DateUtils.getDayOfWeek(currentYear, currentMonth, today);
            int tomorrowInWeek = DateUtils.getDayOfWeek(currentYear, currentMonth, today + 1);
            List<InlineKeyboardButton> firstRow = new ArrayList<>();
            firstRow.add(buildButton(busyDays, today, todayIsFinished || nonWorkingDays.contains(todayInWeek)
                    ? Constants.UNAVAILABLE_DATE
                    : Constants.TODAY));
            firstRow.add(buildButton(busyDays, today + 1, nonWorkingDays.contains(tomorrowInWeek)
                    ? Constants.UNAVAILABLE_DATE
                    : Constants.TOMORROW));
            keyboard.add(firstRow);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd");
        List<InlineKeyboardButton> dayTitlesRow = new ArrayList<>();
        for (String dayTitle : Constants.DAY_TITLES) {
            InlineKeyboardButton titleButton = new InlineKeyboardButton();
            titleButton.setText(dayTitle);
            titleButton.setCallbackData(Constants.IGNORE);
            dayTitlesRow.add(titleButton);
        }
        keyboard.add(dayTitlesRow);

        Month month = Month.of(currentMonth);
        LocalDate date = LocalDate.of(currentYear, currentMonth, 1);
        int daysInMonth = month.length(currentDate.isLeapYear());
        List<String> availableDates = new ArrayList<>();
        for (int i = 1; i <= daysInMonth; i++) {
            if (i == 1 || date.getDayOfWeek().getValue() == 1) {
                List<InlineKeyboardButton> row = new ArrayList<>();
                keyboard.add(row);
            }
            String buttonText;
            int dayOfWeek = DateUtils.getDayOfWeek(currentYear, currentMonth, i);
            if (i < today
                    || (i == today && todayIsFinished)
                    || busyDays.contains(String.valueOf(i))
                    || nonWorkingDays.contains(dayOfWeek)) {
                buttonText = Constants.UNAVAILABLE_DATE;
            } else {
                buttonText = date.format(formatter);
                availableDates.add(buttonText);
            }
            InlineKeyboardButton dateButton = new InlineKeyboardButton();
            dateButton.setText(buttonText);
            dateButton.setCallbackData(buttonText.equals(Constants.UNAVAILABLE_DATE) ? Constants.IGNORE : buttonText);
            int previousMonthDays = date.getDayOfWeek().getValue() - i;
            if (i == 1 && previousMonthDays != 0) {
                InlineKeyboardButton emptyButton = new InlineKeyboardButton();
                emptyButton.setText(Constants.EMPTY_DATE);
                emptyButton.setCallbackData(Constants.IGNORE);
                IntStream.range(0, previousMonthDays).forEach(n -> keyboard.get(keyboard.size() - 1).add(emptyButton));
            }
            keyboard.get(keyboard.size() - 1).add(dateButton);
            date = date.plusDays(1);
        }
        List<InlineKeyboardButton> lastDaysRow = keyboard.get(keyboard.size() - 1);
        int lastDaysRowSize = lastDaysRow.size();
        if (lastDaysRowSize < Constants.DAYS_IN_WEEK) {
            InlineKeyboardButton emptyButton = new InlineKeyboardButton();
            emptyButton.setText(Constants.EMPTY_DATE);
            emptyButton.setCallbackData(Constants.IGNORE);
            IntStream.range(lastDaysRowSize, Constants.DAYS_IN_WEEK).forEach(n -> lastDaysRow.add(emptyButton));
        }
        Month nextMoth = month.plus(isNextMonth ? -1 : 1);
        List<InlineKeyboardButton> lastRow = new ArrayList<>();
        InlineKeyboardButton nextMonthButton = new InlineKeyboardButton();
        nextMonthButton.setText(nextMoth.name());
        nextMonthButton.setCallbackData(isNextMonth ? Constants.CURRENT_MONTH : Constants.NEXT_MONTH);
        lastRow.add(nextMonthButton);
        keyboard.add(lastRow);
        keyboard.removeIf(r -> r.stream().allMatch(b -> Constants.UNAVAILABLE_DATE.equals(b.getText())));
        inlineKeyboardMarkup.setKeyboard(keyboard);
        context.getParams().put(Constants.AVAILABLE_DATES, availableDates);
        return inlineKeyboardMarkup;
    }

    public static InlineKeyboardMarkup buildInlineKeyboard(BuildKeyboardRequest request) {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        for (Map.Entry<String, String> entry : request.getButtonsMap().entrySet()) {
            if (rowInline.size() == request.getType().getElementsInRow()) {
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

    public static ReplyKeyboardMarkup buildReplyKeyboard(BuildKeyboardRequest request) {
        Map<String, String> buttonsMap = request.getButtonsMap();
        KeyBoardType type = request.getType();
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

    public static ReplyKeyboardMarkup buildContactsKeyboard(BuildKeyboardRequest request) {
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

    private static InlineKeyboardButton buildButton(Set<String> busyDays, int currentDay, String value) {
        String currentDayValue = String.valueOf(currentDay);
        String todayTitle = busyDays.contains(currentDayValue) ? Constants.UNAVAILABLE_DATE : value;
        String todayCallBack = Constants.UNAVAILABLE_DATE.equals(todayTitle) ? Constants.IGNORE : currentDayValue;
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(todayTitle);
        button.setCallbackData(todayCallBack);
        return button;
    }
}
