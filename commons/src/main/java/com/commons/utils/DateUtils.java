package com.commons.utils;

import com.commons.model.FreeSlot;
import com.commons.model.Department;

import java.text.DateFormatSymbols;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.stream.Collectors;

public class DateUtils {

    public static long getPointOfDay(int month, int dayOfMonth, int hourOfDay, Department department) {
        LocalDateTime localDateTime = LocalDate.now()
                .atStartOfDay()
                .withMonth(month)
                .withDayOfMonth(dayOfMonth)
                .with(ChronoField.HOUR_OF_DAY, hourOfDay);
        return ZonedDateTime.of(localDateTime, ZoneId.of(department.getZone())).toEpochSecond();
    }

    public static long getPointOfDay(int month, int dayOfMonth, int hourOfDay, int minute, Department department) {
        LocalDateTime localDateTime = LocalDate.now()
                .atStartOfDay()
                .withMonth(month)
                .withDayOfMonth(dayOfMonth)
                .with(ChronoField.HOUR_OF_DAY, hourOfDay)
                .with(ChronoField.MINUTE_OF_HOUR, minute);
        return ZonedDateTime.of(localDateTime, ZoneId.of(department.getZone())).toEpochSecond();
    }

    public static long getStartOrEndOfDay(int month, int dayOfMonth, boolean endOfDay, Department department) {
        LocalDateTime localDateTime = LocalDate.now()
                .atStartOfDay()
                .withMonth(month)
                .withDayOfMonth(dayOfMonth)
                .plusDays(endOfDay ? 1 : 0);
        return ZonedDateTime.of(localDateTime, ZoneId.of(department.getZone())).toEpochSecond();
    }

    public static long getStartOrEndOfDay(int year, int month, int dayOfMonth, boolean endOfDay, Department department) {
        ZoneId zone = ZoneId.of(department.getZone());
        return LocalDate.of(year, month, dayOfMonth)
                .atStartOfDay(zone).plusDays(endOfDay ? 1 : 0)
                .toEpochSecond();
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
        return getPointOfDay(now.getMonthValue(), now.getDayOfMonth(), now.getHour(), now.getMinute(), department);
    }

    public static ZonedDateTime nowZoneDateTime(Department department) {
        ZoneId zone = ZoneId.of(department.getZone());
        return ZonedDateTime.now(zone);
    }

    public static String getDayTitle(long date, Department department) {
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(Instant.ofEpochSecond(date), ZoneId.of(department.getZone()));
//        LocalDate localDate = LocalDate.ofInstant(Instant.ofEpochSecond(date), ZoneId.systemDefault());
        return zonedDateTime.format(DateTimeFormatter.ofPattern("dd"));
    }

    public static String getMonthTitle(long date, Department department) {
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(Instant.ofEpochSecond(date), ZoneId.of(department.getZone()));
//        LocalDate localDate = LocalDate.ofInstant(Instant.ofEpochSecond(date), ZoneId.systemDefault());
        return zonedDateTime.format(DateTimeFormatter.ofPattern("MM"));
    }

//    public static String getDateTitle(long date) {
//        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(date), ZoneId.systemDefault());
//        return dateTime.format(DateTimeFormatter.ofPattern("MM/dd,HH:mm"));
//    }

    public static String getDateTitle(long date, Department department) {
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(Instant.ofEpochSecond(date), ZoneId.of(department.getZone()));
        return zonedDateTime.format(DateTimeFormatter.ofPattern("MM/dd,HH:mm"));
    }

    public static long getStartOfMonthDate(Department department, boolean isNextMonth) {
        LocalDateTime endDateTime = LocalDate.now()
                .plusMonths(isNextMonth ? 1 : 0)
                .atStartOfDay()
                .with(TemporalAdjusters.firstDayOfMonth());
        ZonedDateTime zdt = ZonedDateTime.of(endDateTime, ZoneId.of(department.getZone()));
        return zdt.toEpochSecond();
    }

    public static long getEndOfMonthDate(Department department, boolean isNextMonth) {
        LocalDateTime endDateTime = LocalDate.now()
                .plusMonths(isNextMonth ? 1 : 0)
                .atStartOfDay()
                .with(TemporalAdjusters.lastDayOfMonth())
                .plusDays(1);
        ZonedDateTime zdt = ZonedDateTime.of(endDateTime, ZoneId.of(department.getZone()));
        return zdt.toEpochSecond();
//        return endDateTime.toEpochSecond(ZoneOffset.ofHours(-getHourOffset(department)));
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
//        return LocalDateTime.now().plusHours(getHourOffset(department)).getDayOfMonth();
    }

    public static int getNumberOfCurrentMonth(Department department) {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of(department.getZone()));
        return now.getMonth().getValue();
//        return LocalDateTime.now().plusHours(getHourOffset(department)).getMonth().getValue();
    }

    public static int getNumberOfCurrentYear(Department department) {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of(department.getZone()));
        return now.getYear();
//        return LocalDateTime.now().plusHours(getHourOffset(department)).getYear();
    }

    public static boolean isWholeDayAvailable(Department department, FreeSlot slot) {
        long startPoint = slot.getStartPoint();
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(Instant.ofEpochSecond(startPoint), ZoneId.of(department.getZone()));
        long wholeDay = getPointOfDay(zonedDateTime.getMonth().getValue(), zonedDateTime.getDayOfMonth(), department.getEndWork(), department)
                - getPointOfDay(zonedDateTime.getMonth().getValue(), zonedDateTime.getDayOfMonth(), department.getStartWork(), department);
        return slot.getDurationSec() == wholeDay;
    }

    public static List<String> getSlotTitles(FreeSlot slot, long serviceDuration, int intervalMin, Department department) {
        long startPoint = slot.getStartPoint();
        long endPoint = slot.getStartPoint() + slot.getDurationSec();
        List<String> titles = new ArrayList<>();
        while (startPoint + serviceDuration <= endPoint) {
            ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(Instant.ofEpochSecond(startPoint), ZoneId.of(department.getZone()));
//            LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(startPoint), ZoneId.systemDefault());
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

    public static int getHourOffset(Department department) {
        String zoneId = department.getZone();
        TimeZone timeZone = TimeZone.getTimeZone(zoneId);
        return timeZone.getOffset(Calendar.ZONE_OFFSET) / 3600 / 1000;
    }
}
