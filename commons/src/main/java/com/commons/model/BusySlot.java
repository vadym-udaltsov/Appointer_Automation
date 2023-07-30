package com.commons.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusySlot {
    @JsonProperty("sp")
    private long startPoint;
    @JsonProperty("dur")
    private int durationSec;
    @JsonProperty("all")
    private boolean wholeDay;
}
