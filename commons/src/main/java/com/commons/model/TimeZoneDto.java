package com.commons.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TimeZoneDto {
    private TimeZone zone;
    private String title;
}
