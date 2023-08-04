package com.bot.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.commons.model.Appointment;
import com.commons.model.DynamoDbEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@DynamoDBTable(tableName = "schedule")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Schedule extends DynamoDbEntity {

    public static final String TABLE_NAME = "schedule";
    public static final String HASH_KEY = "s";
    private static final String RANGE_KEY = "d";

    @DynamoDBHashKey(attributeName = HASH_KEY)
    @JsonProperty(HASH_KEY)
    private String specialist;

    @DynamoDBRangeKey(attributeName = RANGE_KEY)
    @JsonProperty(RANGE_KEY)
    private String date;

    private List<Appointment> slots;

    @Override
    public PrimaryKey getPrimaryKey() {
        return null;
    }

    @Override
    public Item toItem() {
        return null;
    }

    @Override
    public String getCondition() {
        return null;
    }
}
