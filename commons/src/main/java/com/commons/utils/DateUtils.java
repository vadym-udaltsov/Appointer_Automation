package com.commons.utils;

import com.commons.model.Department;
import com.commons.model.FreeSlot;

import java.text.DateFormatSymbols;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DateUtils {

    public static long getPointOfDayWithYear(int year, int month, int dayOfMonth, int hourOfDay, Department department) {
        ZonedDateTime zdt = ZonedDateTime.of(year, month, dayOfMonth, hourOfDay, 0, 0, 0, ZoneId.of(department.getZone()));
        return zdt.toEpochSecond();
    }

    public static long getStartOrEndOfDayWithYear(int year, int month, int dayOfMonth, boolean endOfDay, Department department) {
        ZonedDateTime zdt = ZonedDateTime.of(year, month, dayOfMonth, 0, 1, 0, 0,
                ZoneId.of(department.getZone())).plusDays(endOfDay ? 1 : 0);
        return zdt.toEpochSecond();
    }

    public static int getYearOffset(int prevMonth, String newMonthName) {
        Month newMonth = Month.valueOf(newMonthName.toUpperCase());
        int value = newMonth.getValue();
        if ("January".equalsIgnoreCase(newMonthName) && prevMonth == 12) {
            return 1;
        }
        if ("December".equalsIgnoreCase(newMonthName) && prevMonth == 1) {
            return -1;
        }
        return 0;
    }

    public static String getPrevMonthValue(int current) {
        List<String> names = monthNames();
        if (current == 1) {
            return names.get(11);
        }
        return names.get(current - 2);
    }

    public static String getNextMonthValue(int current) {
        List<String> names = monthNames();
        if (current < 12) {
            return names.get(current);
        }
        if (current == 12) {
            return names.get(0);
        }
        throw new RuntimeException("Wrong month value: " + current);
    }

    public static List<String> monthNames() {
        DateFormatSymbols format = new DateFormatSymbols();
        String[] months = format.getMonths();
        return Arrays.stream(months).filter(m -> !"".equals(m)).map(String::toUpperCase).collect(Collectors.toList());
    }

    public static long nowZone(Department department) {
        ZoneId zone = ZoneId.of(department.getZone());
        ZonedDateTime now = ZonedDateTime.now(zone);
        return now.toEpochSecond();
    }

    public static ZonedDateTime nowZoneDateTime(Department department) {
        ZoneId zone = ZoneId.of(department.getZone());
        return ZonedDateTime.now(zone);
    }

    public static long getExpirationDate(Department department) {
        ZoneId zone = ZoneId.of(department.getZone());
        return ZonedDateTime.now(zone).plusMonths(6).toEpochSecond();
    }

    public static String getDayTitle(long date, Department department) {
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(Instant.ofEpochSecond(date), ZoneId.of(department.getZone()));
        return zonedDateTime.format(DateTimeFormatter.ofPattern("dd"));
    }

    public static String getMonthTitle(long date, Department department) {
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(Instant.ofEpochSecond(date), ZoneId.of(department.getZone()));
        return zonedDateTime.format(DateTimeFormatter.ofPattern("MM"));
    }

    public static String getDateTitle(long date, Department department) {
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(Instant.ofEpochSecond(date), ZoneId.of(department.getZone()));
        return zonedDateTime.format(DateTimeFormatter.ofPattern("MM/dd,HH:mm"));
    }

    public static long getStartOfMonthDate(Department department, boolean isNextMonth) {
        ZoneId zone = ZoneId.of(department.getZone());
        ZonedDateTime nowZdt = ZonedDateTime.now(zone).plusMonths(isNextMonth ? 1 : 0);
        ZonedDateTime zdt = ZonedDateTime.of(nowZdt.getYear(), nowZdt.getMonthValue(), 1, 0, 1,
                0, 0, zone);
        return zdt.toEpochSecond();
    }

    public static long getEndOfMonthDate(Department department, boolean isNextMonth) {
        ZoneId zone = ZoneId.of(department.getZone());
        ZonedDateTime nowZdt = ZonedDateTime.now(zone).plusMonths(isNextMonth ? 2 : 1);
        ZonedDateTime zdt = ZonedDateTime.of(nowZdt.getYear(), nowZdt.getMonthValue(), 1, 0, 1,
                0, 0, zone);
        return zdt.toEpochSecond();
    }

    public static ZonedDateTime getEndOfMonthZoneDateTime(Department department, boolean isNextMonth) {
        ZoneId zone = ZoneId.of(department.getZone());
        ZonedDateTime nowZdt = ZonedDateTime.now(zone).plusMonths(isNextMonth ? 2 : 1);
        return ZonedDateTime.of(nowZdt.getYear(), nowZdt.getMonthValue(), 1, 0, 1,
                0, 0, zone);
    }

    public static boolean isLastDayOfMonth(int year, int month, int day) {
        return LocalDate.of(year, month, day).plusDays(1).getMonth().getValue() > month;
    }

    public static int getDayOfWeek(int year, int month, int day) {
        return LocalDate.of(year, month, day).getDayOfWeek().getValue();
    }

    public static int getNextDayOfWeek(int year, int month, int day) {
        return LocalDate.of(year, month, day).plusDays(1).getDayOfWeek().getValue();
    }

    public static int getNumberOfCurrentDay(Department department) {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of(department.getZone()));
        return now.getDayOfMonth();
    }

    public static int getNumberOfCurrentMonth(Department department) {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of(department.getZone()));
        return now.getMonth().getValue();
    }

    public static int getNumberOfCurrentYear(Department department) {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of(department.getZone()));
        return now.getYear();
    }

    public static boolean isWholeDayAvailable(Department department, FreeSlot slot) {
        long startPoint = slot.getStartPoint();
        ZonedDateTime zdt = ZonedDateTime.ofInstant(Instant.ofEpochSecond(startPoint), ZoneId.of(department.getZone()));
        long wholeDay = getPointOfDayWithYear(zdt.getYear(), zdt.getMonth().getValue(), zdt.getDayOfMonth(),
                department.getEndWork(), department)
                - getPointOfDayWithYear(zdt.getYear(), zdt.getMonth().getValue(), zdt.getDayOfMonth(),
                department.getStartWork(), department);
        return slot.getDurationSec() == wholeDay;
    }

    public static List<String> getSlotTitles(FreeSlot slot, long serviceDuration, int intervalMin, Department department) {
        long startPoint = slot.getStartPoint();
        long endPoint = slot.getStartPoint() + slot.getDurationSec();
        List<String> titles = new ArrayList<>();
        while (startPoint + serviceDuration <= endPoint) {
            ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(Instant.ofEpochSecond(startPoint), ZoneId.of(department.getZone()));
            String title = zonedDateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
            titles.add(title);
            int currentMinute = zonedDateTime.getMinute();
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
}
