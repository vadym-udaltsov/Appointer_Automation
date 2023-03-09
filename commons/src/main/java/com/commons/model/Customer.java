package com.commons.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@DynamoDBTable(tableName="customer")
@Builder
public class Customer {

    @DynamoDBHashKey(attributeName="email")
    private String email;
    private String phone;

    private List<Department> departments;
    private List<Specialist> specialists;
    private List<CustomerService> services;

}
