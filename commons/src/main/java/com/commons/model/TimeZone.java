package com.commons.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Getter
public enum TimeZone {
    UTC("Europe/London", "(GMT +1:00) London"),
    PLUS_1("Europe/Berlin", "(GMT +2:00) Stockholm, Berlin, Rome"),
    PLUS_2("Europe/Kiev", "(GMT +3:00) Helsinki, Kiev, Cairo"),
    PLUS_3("Europe/Moscow", "(GMT +3:00) Moscow, Istanbul");
    ////    PLUS_4,
////    PLUS_5,
////    MINUS_1,
////    MINUS_2,
////    MINUS_3,
////    MINUS_4,
////    MINUS_5
//
    private final String id;
    private final String title;

    public static List<TimeZoneDto> buildDtos() {
        List<TimeZoneDto> dtos = new ArrayList<>();
        for (TimeZone value : values()) {
            dtos.add(TimeZoneDto.builder()
                    .title(value.title)
                    .id(value.id)
                    .build());
        }
        return dtos;
    }

}
