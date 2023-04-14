package com.bot.converter;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

import java.util.Map;

/**
 * @author Serhii_Udaltsov on 12/20/2021
 */
public class MapStringConverter implements DynamoDBTypeConverter<Map<String, String>, Map<String, String>> {

    @Override
    public Map<String, String> convert(Map<String, String> object) {
        return object;
    }

    @Override
    public Map<String, String> unconvert(Map<String, String> object) {
        return object;
    }
}
