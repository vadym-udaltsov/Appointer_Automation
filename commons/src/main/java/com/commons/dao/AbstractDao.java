package com.commons.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractDao<T> {

    private DynamoDBMapper dynamoDBMapper;
    private String tableName;

    public AbstractDao(DynamoDBMapper dynamoDBMapper, String tableName) {
        this.dynamoDBMapper = dynamoDBMapper;
        this.tableName = tableName;
    }

    public void createItem(T item) {
        dynamoDBMapper.save(item);
    }

}
