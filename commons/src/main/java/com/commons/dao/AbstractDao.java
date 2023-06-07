package com.commons.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.AttributeUpdate;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Index;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.Page;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.dynamodbv2.model.ExecuteStatementRequest;
import com.amazonaws.services.dynamodbv2.model.ExecuteStatementResult;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import com.commons.dao.impl.DynamoDbFactory;
import com.commons.model.DynamoDbEntity;
import com.commons.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public abstract class AbstractDao<T extends DynamoDbEntity> {

    private final DynamoDbFactory dynamoDbFactory;
    private final Class<T> tClass;
    private final String tableName;

    public AbstractDao(DynamoDbFactory dynamoDbFactory, Class<T> tClass, String tableName) {
        this.dynamoDbFactory = dynamoDbFactory;
        this.tClass = tClass;
        this.tableName = tableName;
    }

    public void updateItem(UpdateItemRequest request) {
        log.info("Updating item with primary key: {}", JsonUtils.convertObjectToString(request));
        dynamoDbFactory.getAmazonDynamoDB().updateItem(request);
        log.info("Successfully updated item in the table: {}", tableName);
    }

    public ExecuteStatementResult getItemsByQuery(String query) {
        ExecuteStatementRequest request = new ExecuteStatementRequest().withStatement(query);
        return dynamoDbFactory.getAmazonDynamoDB().executeStatement(request);
    }

    public boolean createItem(T item) {
        try {
            PutItemSpec putItemSpec = new PutItemSpec()
                    .withItem(item.toItem())
                    .withConditionExpression(item.getCondition());
            getDynamoDb().getTable(tableName).putItem(putItemSpec);
            log.info("Successfully saved item to {} table", tableName);
            return true;
        } catch (ConditionalCheckFailedException e) {
            log.warn("Condition: {} failed", item.getCondition());
            return false;
        }
    }

    public void overwriteItem(T item) {
        PutItemSpec putItemSpec = new PutItemSpec()
                .withItem(item.toItem());
        getDynamoDb().getTable(tableName).putItem(putItemSpec);
        log.info("Successfully saved item to {} table", tableName);
    }

    public List<T> findAllByQuery(QuerySpec querySpec) {
        ItemCollection<QueryOutcome> queryOutcome = getDynamoDb().getTable(tableName).query(querySpec);
        List<Item> items = getItemsFromQueryResult(queryOutcome);
        if (items.isEmpty()) {
            return Collections.emptyList();
        }
        return items.stream()
                .map(i -> JsonUtils.parseStringToObject(i.toJSON(), tClass))
                .collect(Collectors.toList());
    }

    public T getItemByIndexQuery(QuerySpec querySpec, String indexName) {
        Index index = dynamoDbFactory.getDynamoDB().getTable(tableName).getIndex(indexName);
        ItemCollection<QueryOutcome> result = index.query(querySpec);
        List<Item> itemsFromQueryResult = getItemsFromQueryResult(result);
        if (itemsFromQueryResult.size() == 0) {
            return null;
        }
        Item item = itemsFromQueryResult.get(0);
        return JsonUtils.parseStringToObject(item.toJSON(), tClass);
    }

    public List<T> getItemsByIndexQuery(QuerySpec querySpec, String indexName) {
        Index index = dynamoDbFactory.getDynamoDB().getTable(tableName).getIndex(indexName);
        ItemCollection<QueryOutcome> result = index.query(querySpec);
        List<Item> itemsFromQueryResult = getItemsFromQueryResult(result);
        if (itemsFromQueryResult.size() == 0) {
            return List.of();
        }
        return itemsFromQueryResult.stream()
                .map(i -> JsonUtils.parseStringToObject(i.toJSON(), tClass))
                .collect(Collectors.toList());
    }

    public T getItemByHashKey(Object hashKey) {
        log.info("Getting object from table: {} with hashKey: {}", tableName, hashKey);
        T item = getMapper().load(tClass, hashKey);
        log.info("Successfully got item from table: {}, hash key: {}", tableName, hashKey);
        return item;
    }

    public T getItem(T item) {
        log.info("Getting object from table: {}", tableName);
        T result = getMapper().load(item);
        log.info("Successfully got item from table: {}", tableName);
        return result;
    }

    private List<Item> getItemsFromQueryResult(ItemCollection<QueryOutcome> queryOutcome) {
        List<Item> queryResult = new ArrayList<>();
        for (Page<Item, QueryOutcome> page : queryOutcome.pages()) {
            for (Item item : page) {
                queryResult.add(item);
            }
        }
        return queryResult;
    }

    private DynamoDBMapper getMapper() {
        return dynamoDbFactory.getMapper();
    }

    private DynamoDB getDynamoDb() {
        return dynamoDbFactory.getDynamoDB();
    }
}
