package com.commons.dao.impl;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DynamoDbFactory {

    private final AmazonDynamoDB amazonDynamoDB;
    private DynamoDBMapper mapper;
    private DynamoDB dynamoDB;

    @Autowired
    public DynamoDbFactory(AmazonDynamoDB amazonDynamoDB) {
        this.amazonDynamoDB = amazonDynamoDB;
    }

    public DynamoDBMapper getMapper() {
        if (mapper == null) {
            mapper = new DynamoDBMapper(amazonDynamoDB);
        }
        return mapper;
    }

    public DynamoDB getDynamoDB() {
        if (dynamoDB == null) {
            dynamoDB = new DynamoDB(amazonDynamoDB);
        }
        return dynamoDB;
    }

    public AmazonDynamoDB getAmazonDynamoDB() {
        return amazonDynamoDB;
    }
}
