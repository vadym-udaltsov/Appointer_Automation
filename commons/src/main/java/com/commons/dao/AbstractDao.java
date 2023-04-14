package com.commons.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
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
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import com.commons.dao.impl.DynamoDbFactory;
import com.commons.model.DynamoDbEntity;
import com.commons.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import software.amazon.awssdk.services.sqs.endpoints.internal.Value;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public abstract class AbstractDao<T extends DynamoDbEntity> {

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
        Item item = getItemsFromQueryResult(result).get(0);
        return JsonUtils.parseStringToObject(item.toJSON(), tClass);
    }

    public T getItemByHashKey(Object hashKey) {
        log.info("Getting object from table: {} with hashKey: {}", tableName, hashKey);
        T item = getMapper().load(tClass, hashKey);
        log.info("Successfully got item from table: {}, hash key: {}", tableName, hashKey);
        return item;
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
