package com.bot.converter;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.commons.utils.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.Map;

/**
 * @author Serhii_Udaltsov on 5/1/2021
 */
public class MapObjectConverter implements DynamoDBTypeConverter<String, Map<String, Object>> {

    @Override
    public String convert(Map<String, Object> map) {
        return JsonUtils.convertObjectToString(map);
    }

    @Override
    public Map<String, Object> unconvert(String mapJson) {
        return JsonUtils.parseStringToObject(mapJson, new TypeReference<>(){});
    }
}
