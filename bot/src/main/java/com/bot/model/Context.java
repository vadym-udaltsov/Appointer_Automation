package com.bot.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedEnum;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.bot.converter.MapObjectConverter;
import com.commons.model.DynamoDbEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
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
    public static final String RANGE_KEY = "did";
    public static final String PARAMS_FIELD = "p";
    public static final String LOCALE_FIELD = "l";
    public static final String NAVIGATION_FIELD = "n";
    public static final String PHONE_FIELD = "pn";
    public static final String BLOCKED_FIELD = "b";
    public static final String CUSTOM_FIELD = "c";
    public static final String COMMENTS_FIELD = "comm";
    public static final String EXPIRATION_FIELD = "exp";
    public static final String INDEX_NAME = "pn-did-index";
    public static final String DID_ID_INDEX = "did-id-index";

    @DynamoDBHashKey(attributeName = HASH_KEY)
    @DynamoDBIndexRangeKey(globalSecondaryIndexName = DID_ID_INDEX)
    @JsonProperty(HASH_KEY)
    private long userId;

    @DynamoDBRangeKey(attributeName = RANGE_KEY)
    @JsonProperty(RANGE_KEY)
    @DynamoDBIndexRangeKey(globalSecondaryIndexName = INDEX_NAME)
    @DynamoDBIndexHashKey(globalSecondaryIndexName = DID_ID_INDEX)
    private String departmentId;

    @JsonProperty(LOCALE_FIELD)
    @DynamoDBAttribute(attributeName = LOCALE_FIELD)
    @DynamoDBTypeConvertedEnum
    private Language language;

    @DynamoDBAttribute(attributeName = NAVIGATION_FIELD)
    @JsonProperty(NAVIGATION_FIELD)
    private List<String> navigation;

    @JsonProperty(PHONE_FIELD)
    @DynamoDBAttribute(attributeName = PHONE_FIELD)
    @DynamoDBIndexHashKey(globalSecondaryIndexName = INDEX_NAME)
    private String phoneNumber;

    @DynamoDBAttribute(attributeName = PARAMS_FIELD)
    @DynamoDBTypeConverted(converter = MapObjectConverter.class)
    @JsonProperty(PARAMS_FIELD)
    private Map<String, Object> params;

    @DynamoDBAttribute(attributeName = "name")
    private String name;

    @DynamoDBAttribute(attributeName = BLOCKED_FIELD)
    @JsonProperty(BLOCKED_FIELD)
    private boolean blocked;

    @DynamoDBAttribute(attributeName = CUSTOM_FIELD)
    @JsonProperty(CUSTOM_FIELD)
    private boolean custom;

    @DynamoDBAttribute(attributeName = EXPIRATION_FIELD)
    @JsonProperty(EXPIRATION_FIELD)
    private long expiration;

    @DynamoDBAttribute(attributeName = COMMENTS_FIELD)
    @JsonProperty(COMMENTS_FIELD)
    private List<String> comments;

    @Override
    @JsonIgnore
    public PrimaryKey getPrimaryKey() {
        return new PrimaryKey(HASH_KEY, userId, RANGE_KEY, departmentId);
    }

    @Override
    @JsonIgnore
    public Item toItem() {
        return new Item()
                .withNumber(HASH_KEY, userId)
                .withString(RANGE_KEY, departmentId)
                .with(NAVIGATION_FIELD, navigation)
                .with(LOCALE_FIELD, language == null ? Language.US.name() : language.name())
                .withString(PHONE_FIELD, StringUtils.isBlank(phoneNumber) ? "n/a" : phoneNumber)
                .withMap(PARAMS_FIELD, params == null ? new HashMap<>() : params)
                .withBoolean(BLOCKED_FIELD, blocked)
                .withBoolean(CUSTOM_FIELD, custom)
                .withNumber(EXPIRATION_FIELD, expiration)
                .withString("name", name == null ? "n/a" : name)
                .withList(COMMENTS_FIELD, comments == null ? new ArrayList<>() : comments)
                .withList(NAVIGATION_FIELD, navigation == null ? new ArrayList<>() : navigation);
    }

    @Override
    @JsonIgnore
    public String getCondition() {
        return String.format("attribute_not_exists(%s)", RANGE_KEY);
    }
}
