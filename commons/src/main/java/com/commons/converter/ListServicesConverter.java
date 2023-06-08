package com.commons.converter;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.commons.model.CustomerService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListServicesConverter implements DynamoDBTypeConverter<List<Map<String, AttributeValue>>, List<CustomerService>> {

    @Override
    public List<Map<String, AttributeValue>> convert(List<CustomerService> services) {
        List<Map<String, AttributeValue>> values = new ArrayList<>();
        for (CustomerService service : services) {
            Map<String, AttributeValue> value = new HashMap<>();
            value.put("name", new AttributeValue(service.getName()));
            value.put("duration", new AttributeValue().withN(String.valueOf(service.getDuration())));
            value.put("price", new AttributeValue().withN(String.valueOf(service.getPrice())));
            values.add(value);
        }
        return values;
    }

    @Override
    public List<CustomerService> unconvert(List<Map<String, AttributeValue>> values) {
        List<CustomerService> services = new ArrayList<>();
        for (Map<String, AttributeValue> value : values) {
            CustomerService service = new CustomerService();
            service.setName(value.get("name").getS());
            service.setDuration(Integer.parseInt(value.get("duration").getN()));
            service.setPrice(Integer.parseInt(value.get("price").getN()));
            services.add(service);
        }
        return services;
    }
}
