package com.commons.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Slf4j
public class JsonUtils {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static String convertObjectToString(Object object) {
        try {
            return MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("Error during convert object {}", object.getClass());
            throw new RuntimeException("Error during convert object: " + e.getMessage(), e);
        }
    }

    public static <T> T parseStringToObject(String objectStr, TypeReference<T> type) {
        try {
            return MAPPER.readValue(objectStr, type);
        } catch (JsonProcessingException e) {
            log.error("Error during parsing string {}", objectStr);
            throw new RuntimeException("Error during parsing string: " + e.getMessage(), e);
        }
    }

    public static <T> T parseStringToObject(String objectStr, Class<T> type) {
        try {
            return MAPPER.readValue(objectStr, type);
        } catch (JsonProcessingException e) {
            log.error("Error during parsing string {}", objectStr);
            throw new RuntimeException("Error during parsing string: " + e.getMessage(), e);
        }
    }

    public static <T> T parseInputStreamToObject(InputStream json, TypeReference<T> type) {
        try {
            return MAPPER.readValue(json, type);
        } catch (IOException e) {
            log.error("Error during paring inputStream");
            throw new RuntimeException("Error during parsing inputStream", e);
        }
    }

    public static Map<String, Object> parseObjectToMap(Object object) {
        try {
            String sVal = MAPPER.writeValueAsString(object);
            return MAPPER.readValue(sVal, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error during parsing object to map", e);
        }
    }

    public static <T> T parseMapToObject(Map<String, Object> map, Class<T> tClass) {
        try {
            return MAPPER.readValue(convertObjectToString(map), tClass);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error during parsing map to object", e);
        }
    }

    public static <T> T parseMapToObject(Map<String, Object> map) {
        try {
            return MAPPER.readValue(convertObjectToString(map), new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error during parsing map to object", e);
        }
    }

    public static JsonNode parseStreamToJsonNode(InputStream stream) {
        try {
            return MAPPER.readTree(stream);
        } catch (IOException e) {
            log.error("Error during parsing inputStream");
            throw new RuntimeException("Error during paring inputStream", e);
        }
    }
}
