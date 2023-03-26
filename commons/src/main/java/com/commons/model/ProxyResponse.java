package com.commons.model;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class ProxyResponse {
    private int statusCode;
    private Map<String, String> headers;
    private String body;
}
