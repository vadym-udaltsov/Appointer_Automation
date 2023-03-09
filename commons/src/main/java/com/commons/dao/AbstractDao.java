package com.commons.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.commons.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public abstract class AbstractDao<T> {

    private DynamoDBMapper dynamoDBMapper;
    private Class<T> tClass;
    private String tableName;

    public AbstractDao(DynamoDBMapper dynamoDBMapper, Class<T> tClass, String tableName) {
        this.dynamoDBMapper = dynamoDBMapper;
        this.tClass = tClass;
        this.tableName = tableName;
    }

    public void createItem(T item) {
        log.info("Creating item: {}", JsonUtils.convertObjectToString(item));
        dynamoDBMapper.save(item);
        log.info("Successfully saved item to {} table", tableName);
    }

    public T getItemByHashKeyString(String hashKey) {
        log.info("Getting object from table: {} with hashKey: {}", tableName, hashKey);
        T item = dynamoDBMapper.load(tClass, hashKey);
        log.info("Successfully got item from table: {}, hash key: {}", tableName, hashKey);
        return item;
    }

}
