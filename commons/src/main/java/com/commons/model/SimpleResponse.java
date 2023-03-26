package com.commons.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SimpleResponse {
    private String body;
}
