package com.bot.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedEnum;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.bot.converter.MapObjectConverter;
import com.commons.model.DynamoDbEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author Serhii_Udaltsov on 4/10/2021
 */
@Data
@NoArgsConstructor
@DynamoDBTable(tableName = "context")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Context extends DynamoDbEntity {
    public static final String TABLE_NAME = "context";
    public static final String HASH_KEY = "id";
    public static final String PARAMS_FIELD = "p";
    public static final String LOCALE_FIELD = "l";
    public static final String NAVIGATION_FIELD = "n";
    public static final String PHONE_FIELD = "pn";

    @DynamoDBHashKey(attributeName = HASH_KEY)
    private long userId;

    @DynamoDBAttribute(attributeName = LOCALE_FIELD)
    @DynamoDBTypeConvertedEnum
    private Language language;

    @DynamoDBAttribute(attributeName = NAVIGATION_FIELD)
    private List<String> navigation;

    @DynamoDBAttribute(attributeName = PHONE_FIELD)
    private String phoneNumber;

    @Override
    @JsonIgnore
    public PrimaryKey getPrimaryKey() {
        return new PrimaryKey(HASH_KEY, userId);
    }

    @Override
    @JsonIgnore
    public Item toItem() {
        return new Item()
                .withNumber(HASH_KEY, userId)
                .withList("n", navigation);
    }


    @Override
    @JsonIgnore
    public String getCondition() {
        return String.format("attribute_not_exists(%s)", HASH_KEY);
    }
}
