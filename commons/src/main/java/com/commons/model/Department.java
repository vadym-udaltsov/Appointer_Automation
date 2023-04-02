package com.commons.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedEnum;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@Jacksonized
@DynamoDBTable(tableName = "department")
@Builder
@NoArgsConstructor()
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Department extends DynamoDbEntity {
    private static final String HASH_KEY = "c";
    private static final String RANGE_KEY = "n";

    @DynamoDBHashKey(attributeName = HASH_KEY)
    @JsonProperty("c")
    private String customer;

    @DynamoDBRangeKey(attributeName = RANGE_KEY)
    @JsonProperty("n")
    private String name;

    private String id;

    @DynamoDBTypeConvertedEnum
    @JsonProperty("tp")
    private DepartmentType type;

    @JsonProperty("tn")
    @JsonIgnore
    private String token;

    @JsonProperty("as")
    private List<String> availableSpecialists = new ArrayList<>();

    @JsonProperty("s")
    private List<CustomerService> services = new ArrayList<>();

    @Override
    @JsonIgnore
    public PrimaryKey getPrimaryKey() {
        return new PrimaryKey(HASH_KEY, customer, RANGE_KEY, name);
    }

    @Override
    public Item toItem() {
        return new Item()
                .with(HASH_KEY, customer)
                .with(RANGE_KEY, name)
                .with("id", id)
                .with("tp", type.name())
                .withList("as", availableSpecialists)
                .withList("s", services);
    }

    @Override
    @JsonIgnore
    public String getCondition() {
        return String.format("attribute_not_exists(%s)", RANGE_KEY);
    }
}
