package com.commons.model;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;

public abstract class DynamoDbEntity {

    public abstract PrimaryKey getPrimaryKey();

    public abstract Item toItem();

    public abstract String getCondition();
}
