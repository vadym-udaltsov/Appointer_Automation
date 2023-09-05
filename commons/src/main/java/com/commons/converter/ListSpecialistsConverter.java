package com.commons.converter;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.commons.model.Specialist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListSpecialistsConverter implements DynamoDBTypeConverter<List<Map<String, AttributeValue>>, List<Specialist>> {

    @Override
    public List<Map<String, AttributeValue>> convert(List<Specialist> specialists) {
        List<Map<String, AttributeValue>> values = new ArrayList<>();
        for (Specialist specialist : specialists) {
            Map<String, AttributeValue> value = new HashMap<>();
            value.put("id", new AttributeValue(specialist.getId()));
            value.put("name", new AttributeValue(specialist.getName()));
            value.put("pn", new AttributeValue(specialist.getPhoneNumber()));
            values.add(value);
        }
        return values;
    }

    @Override
    public List<Specialist> unconvert(List<Map<String, AttributeValue>> values) {
        List<Specialist> specialists = new ArrayList<>();
        for (Map<String, AttributeValue> value : values) {
            Specialist specialist = new Specialist();
            specialist.setName(value.get("name").getS());
            specialist.setId(value.get("id").getS());
            specialist.setPhoneNumber(value.get("pn").getS());
            specialists.add(specialist);
        }
        return specialists;
    }
}
