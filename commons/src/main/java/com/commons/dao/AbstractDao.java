package com.commons.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.AttributeUpdate;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import com.commons.dao.impl.DynamoDbFactory;
import com.commons.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public abstract class AbstractDao<T> {

    private DynamoDbFactory dynamoDbFactory;
    private Class<T> tClass;
    private String tableName;

    public AbstractDao(DynamoDbFactory dynamoDbFactory, Class<T> tClass, String tableName) {
        this.dynamoDbFactory = dynamoDbFactory;
        this.tClass = tClass;
        this.tableName = tableName;
    }

    public void updateItem(PrimaryKey primaryKey, AttributeUpdate... attributeUpdates) {
        log.info("Updating item with primary key: {}", JsonUtils.convertObjectToString(primaryKey));
        getDynamoDb().getTable(tableName).updateItem(primaryKey, attributeUpdates);
        log.info("Successfully updated item in the table: {}", tableName);
    }

    public void updateItem(UpdateItemRequest request) {
//        log.info("Updating item with primary key: {}", JsonUtils.convertObjectToString(primaryKey));
        dynamoDbFactory.getAmazonDynamoDB().updateItem(request);
        log.info("Successfully updated item in the table: {}", tableName);
    }

    public void updateItem(UpdateItemSpec updateItemSpec) {
//        log.info("Updating item with primary key: {}", JsonUtils.convertObjectToString(primaryKey));
        dynamoDbFactory.getDynamoDB().getTable(tableName).updateItem(updateItemSpec);
        log.info("Successfully updated item in the table: {}", tableName);
    }

    public void createItem(T item) {
        log.info("Creating item: {}", JsonUtils.convertObjectToString(item));
        getMapper().save(item);
        log.info("Successfully saved item to {} table", tableName);
    }

    public T getItemByHashKeyString(String hashKey) {
        log.info("Getting object from table: {} with hashKey: {}", tableName, hashKey);
        T item = getMapper().load(tClass, hashKey);
        log.info("Successfully got item from table: {}, hash key: {}", tableName, hashKey);
        return item;
    }

    private DynamoDBMapper getMapper() {
        return dynamoDbFactory.getMapper();
    }

    private DynamoDB getDynamoDb() {
        return dynamoDbFactory.getDynamoDB();
    }
}
