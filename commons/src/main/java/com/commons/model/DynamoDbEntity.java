package com.commons.model;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;

public abstract class DynamoDbEntity {

    public abstract PrimaryKey getPrimaryKey();

    public Item toItem() {
        return new Item();
    }

    public String getCondition() {
        return "";
    }
}
