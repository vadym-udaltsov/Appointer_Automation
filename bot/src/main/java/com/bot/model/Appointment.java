package com.bot.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.commons.model.DynamoDbEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@DynamoDBTable(tableName = "appointment")
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Appointment extends DynamoDbEntity {
    private static final String HASH_KEY = "s";
    private static final String RANGE_KEY = "d";
    public static final String TABLE_NAME = "appointment";
    public static final String DEP_INDEX_NAME = "did-d-index";
    public static final String USER_INDEX_NAME = "uid-d-index";

    @DynamoDBHashKey(attributeName = HASH_KEY)
    @JsonProperty("s")
    private String specialist;

    @DynamoDBRangeKey(attributeName = RANGE_KEY)
    @DynamoDBIndexRangeKey(attributeName = RANGE_KEY, globalSecondaryIndexNames = {DEP_INDEX_NAME, USER_INDEX_NAME})
    @JsonProperty("d")
    private long date;

    @DynamoDBIndexHashKey(globalSecondaryIndexName = USER_INDEX_NAME)
    @JsonProperty("uid")
    private long userId;

    @DynamoDBIndexHashKey(globalSecondaryIndexName = DEP_INDEX_NAME)
    @JsonProperty("did")
    private String departmentId;

    @JsonProperty("serv")
    private String service;

    @JsonProperty("dur")
    private int duration;

    @Override
    @JsonIgnore
    public String getCondition() {
        return String.format("attribute_not_exists(%s)", RANGE_KEY);
    }

    @Override
    public Item toItem() {
        return new Item()
                .with(HASH_KEY, specialist)
                .withNumber(RANGE_KEY, date)
                .withNumber("uid", userId)
                .withNumber("dur", duration)
                .withString("did", departmentId)
                .withString("serv", service);
    }

    @Override
    @JsonIgnore
    public PrimaryKey getPrimaryKey() {
        return new PrimaryKey(HASH_KEY, userId, RANGE_KEY, date);
    }
}
