package com.commons.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Data
@DynamoDBTable(tableName="customer")
@Builder
@NoArgsConstructor()
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Customer extends DynamoDbEntity{

    @DynamoDBHashKey(attributeName="email")
    private String email;
    private String phone;

    @Override
    public PrimaryKey getPrimaryKey() {
        return null;
    }

    @Override
    public Item toItem() {
        return new Item()
                .with("email", email)
                .with("phone", phone);
    }

    @Override
    public String getCondition() {
        return "attribute_not_exists(email)";

    }
}
