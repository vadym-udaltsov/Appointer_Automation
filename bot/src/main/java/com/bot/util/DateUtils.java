package com.bot.util;

import com.bot.model.FreeSlot;
import com.commons.model.Department;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;

public class DateUtils {

    public static long getPointOfDay(int month, int dayOfMonth, int hourOfDay) {
        LocalDateTime localDateTime = LocalDate.now()
                .atStartOfDay()
                .withMonth(month)
                .withDayOfMonth(dayOfMonth)
                .with(ChronoField.HOUR_OF_DAY, hourOfDay);
        return localDateTime.toEpochSecond(ZoneOffset.UTC);
    }

    public static long now(Department department) {
        return LocalDateTime.now().toEpochSecond(ZoneOffset.ofHours(-department.getZoneOffset()));
    }

    public static int getNumberOfCurrentDay(Department department) {
        return LocalDateTime.now().plusHours(department.getZoneOffset()).getDayOfMonth();
    }

    public static int getNumberOfCurrentMonth(Department department) {
        return LocalDateTime.now().plusHours(department.getZoneOffset()).getMonth().getValue();
    }

    public static int getNumberOfCurrentYear(Department department) {
        return LocalDateTime.now().plusHours(department.getZoneOffset()).getYear();
    }

    public static boolean isWholeDayAvailable(Department department, FreeSlot slot) {
        int currentDay = getNumberOfCurrentDay(department);
        long startPoint = slot.getStartPoint();
        LocalDate localDate = LocalDate.ofInstant(Instant.ofEpochSecond(startPoint), ZoneId.systemDefault());
        long wholeDay = getPointOfDay(localDate.getMonth().getValue(), currentDay, department.getEndWork())
                - getPointOfDay(localDate.getMonth().getValue(), currentDay, department.getStartWork());
        return slot.getDurationSec() == wholeDay;
    }

    public static List<String> getSlotTitles(FreeSlot slot, long serviceDuration, int intervalMin) {
        long startPoint = slot.getStartPoint();
        long endPoint = slot.getStartPoint() + slot.getDurationSec();
        List<String> titles = new ArrayList<>();
        while (startPoint + serviceDuration <= endPoint) {
            LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(startPoint), ZoneId.systemDefault());
            String title = localDateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
            titles.add(title);
            startPoint += intervalMin * 60L;
        }
        return titles;
    }
}
