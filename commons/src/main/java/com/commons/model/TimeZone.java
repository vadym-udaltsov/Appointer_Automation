package com.commons.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Getter
public enum TimeZone {
    UTC("Europe/London", 1, 0),
    UTC_PLUS_1("Europe/Berlin", 2, 1),
    UTC_PLUS_2("Europe/Kiev", 3, 2);
    ////    PLUS_3,
////    PLUS_4,
////    PLUS_5,
////    MINUS_1,
////    MINUS_2,
////    MINUS_3,
////    MINUS_4,
////    MINUS_5
//
    private final String id;
    private final int summerOffset;
    private final int winterOffset;

    public static List<TimeZoneDto> buildDtos() {
        List<TimeZoneDto> dtos = new ArrayList<>();
        for (TimeZone value : values()) {
            dtos.add(TimeZoneDto.builder()
                    .title(value.id + " - " + LocalDateTime.now(ZoneId.of(value.id)).format(DateTimeFormatter.ofPattern("HH:mm")))
                    .zone(value)
                    .build());
        }
        return dtos;
    }

}
