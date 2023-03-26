package com.commons.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedEnum;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@DynamoDBTable(tableName = "department")
@Builder
@NoArgsConstructor()
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Department extends DynamoDbEntity {
    private static final String HASH_KEY = "c";
    private static final String RANGE_KEY = "n";

    @DynamoDBHashKey(attributeName = HASH_KEY)
    private String customer;
    @DynamoDBRangeKey(attributeName = RANGE_KEY)
    private String name;

    private String id;

    @DynamoDBTypeConvertedEnum
    private DepartmentType type;
    private String token;

    private List<String> availableSpecialists = new ArrayList<>();

    @Override
    public PrimaryKey getPrimaryKey() {
        return new PrimaryKey(HASH_KEY, customer, RANGE_KEY, name);
    }

    @Override
    public Item toItem() {
        return new Item()
                .with(HASH_KEY, customer)
                .with(RANGE_KEY, name)
                .with("id", id)
                .with("type", type.name())
                .withList("availableSpecialists", availableSpecialists);
    }

    @Override
    public String getCondition() {
        return String.format("attribute_not_exists(%s)", RANGE_KEY);
    }
}
