package com.commons.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractDao<T> {

    @Autowired
    private DynamoDBMapper dynamoDBMapper;
    private String tableName;

    protected AbstractDao(String tableName) {
        this.tableName = tableName;
    }

    public void createItem(T item) {
        dynamoDBMapper.save(item);
    }

}
