package com.bot.converter;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemUtils;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.commons.utils.JsonUtils;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Serhii_Udaltsov on 5/1/2021
 */
public class MapObjectConverter implements DynamoDBTypeConverter<Map<String, AttributeValue>, Map<String, Object>> {

    @Override
    public Map<String, Object> unconvert(Map<String, AttributeValue> attributeValueMap) {
        return attributeValueMap.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> ItemUtils.toSimpleValue(e.getValue())));
    }

    @Override
    public Map<String, AttributeValue> convert(Map<String, Object> map) {
        Item item = new Item().withJSON("document", JsonUtils.convertObjectToString(map));
        Map<String, AttributeValue> attributes = ItemUtils.toAttributeValues(item);
        return attributes.get("document").getM();
    }
}
