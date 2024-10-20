package com.commons.dao.impl;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import com.commons.dao.AbstractDao;
import com.commons.dao.ICustomerDao;
import com.commons.model.Customer;
import com.commons.model.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class CustomerDao extends AbstractDao<Customer> implements ICustomerDao {

    @Autowired
    public CustomerDao(DynamoDbFactory dynamoDbFactory) {
        super(dynamoDbFactory, Customer.class, "customer");
    }

    @Override
    public void registerCustomer(String email) {
        UpdateItemRequest request = new UpdateItemRequest()
                .withTableName("customer")
                .withKey(Map.of("email", new AttributeValue(email)))
                .withUpdateExpression("SET isRegistered = :newValue")
                .withExpressionAttributeValues(Map.of(":newValue", new AttributeValue().withBOOL(true)));
        updateItem(request);
    }

    @Override
    public void addCustomerDepartment(String email, Department department) {
        String listAttributeName = "departments";
        UpdateItemRequest request = new UpdateItemRequest()
                .withTableName("customer")
                .withKey(Map.of("email", new AttributeValue(email)))
                .withConditionExpression("NOT contains(departments, :newObject1)")
                .withUpdateExpression("SET " + listAttributeName + " = list_append(if_not_exists(" + listAttributeName + ", :empty_list), :newObject)")
                .withExpressionAttributeValues(Map.of(":newObject", new AttributeValue().withL(new AttributeValue().withM(Map.of("name2", new AttributeValue("njfnj")))),
                        ":empty_list", new AttributeValue().withL(),
                        ":newObject1", new AttributeValue().withM(Map.of("name2", new AttributeValue("njfnj")))));

        updateItem(request);
    }
}
