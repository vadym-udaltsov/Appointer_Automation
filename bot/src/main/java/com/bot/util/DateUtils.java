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
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

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
        return LocalDateTime.now().toEpochSecond(ZoneOffset.ofHours(-getHourOffset(department)));
    }

    public static String getDayTitle(long date) {
        LocalDate localDate = LocalDate.ofInstant(Instant.ofEpochSecond(date), ZoneId.systemDefault());
        return localDate.format(DateTimeFormatter.ofPattern("dd"));
    }

    public static long getStartOfMonthDate(Department department, boolean isNextMonth) {
        LocalDateTime endDateTime = LocalDate.now()
                .plusMonths(isNextMonth ? 1 : 0)
                .atStartOfDay()
                .with(TemporalAdjusters.firstDayOfMonth());
        return endDateTime.toEpochSecond(ZoneOffset.ofHours(-getHourOffset(department)));
    }

    public static long getEndOfMonthDate(Department department, boolean isNextMonth) {
        LocalDateTime endDateTime = LocalDate.now()
                .plusMonths(isNextMonth ? 1 : 0)
                .atStartOfDay()
                .with(TemporalAdjusters.lastDayOfMonth())
                .plusDays(1);
        return endDateTime.toEpochSecond(ZoneOffset.ofHours(-getHourOffset(department)));
    }

    public static int getDayOfWeek(int year, int month, int day) {
        return LocalDate.of(year, month, day).getDayOfWeek().getValue();
    }

    public static int getNumberOfCurrentDay(Department department) {
        return LocalDateTime.now().plusHours(getHourOffset(department)).getDayOfMonth();
    }

    public static int getNumberOfCurrentMonth(Department department) {
        return LocalDateTime.now().plusHours(getHourOffset(department)).getMonth().getValue();
    }

    public static int getNumberOfCurrentYear(Department department) {
        return LocalDateTime.now().plusHours(getHourOffset(department)).getYear();
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
            int currentMinute = localDateTime.getMinute();
            if (currentMinute % intervalMin != 0) {
                startPoint += (getNextMinute(intervalMin, currentMinute) - currentMinute) * 60L;
            } else {
                startPoint += intervalMin * 60L;
            }
        }
        return titles;
    }

    private static int getNextMinute(int interval, int currentMinute) {
        int startMinute = 0;
        while (startMinute < 60) {
            startMinute += interval;
            if (currentMinute <= startMinute) {
                return startMinute;
            }
        }
        return startMinute;
    }

    public static int getHourOffset(Department department) {
        String zoneId = department.getZone();
        TimeZone timeZone = TimeZone.getTimeZone(zoneId);
        return timeZone.getOffset(Calendar.ZONE_OFFSET) / 3600 / 1000;
    }
}
