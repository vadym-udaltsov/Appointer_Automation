package com.commons.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Getter
public enum TimeZone {
    PLUS_1("Europe/London", "London"),
    PLUS_2("Europe/Berlin", "Stockholm, Berlin, Rome"),
    PLUS_3("Europe/Kiev", "Helsinki, Kiev, Cairo"),
    PLUS_3_1("Europe/Moscow", "Istanbul, Moscow"),
    PLUS_4("Asia/Baku", "Baku, Yerevan, Tbilisi"),
    PLUS_5("Asia/Tashkent", "Ashkhabat, Tashkent"),
    MINUS_4("America/New_York", "New York, Detroit"),
    MINUS_5("America/Chicago", "Chicago"),
    MINUS_6("America/Denver", "Denver"),
    MINUS_7("America/Los_Angeles", "Los Angeles");

    private final String id;
    private final String title;

    public static void validateZoneTitle(String title) {
        for (TimeZone value : values()) {
            if (value.getId().equals(title)) {
                return;
            }
        }
        throw new RuntimeException("Time zone not found for title: " + title);
    }

    public static List<TimeZoneDto> buildDtos() {
        List<TimeZoneDto> dtos = new ArrayList<>();
        for (TimeZone value : values()) {
            ZoneId zoneId = ZoneId.of(value.getId());
            ZonedDateTime nowZdt = ZonedDateTime.now(zoneId);
            String timeStr = nowZdt.format(DateTimeFormatter.ofPattern("HH:mm"));
            dtos.add(TimeZoneDto.builder()
                    .title(String.format("(%s) %s", timeStr, value.getTitle()))
                    .id(value.id)
                    .build());
        }
        return dtos;
    }

}
