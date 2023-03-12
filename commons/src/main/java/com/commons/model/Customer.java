package com.commons.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
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
public class Customer {

    @DynamoDBHashKey(attributeName="email")
    private String email;
    private String phone;

    private List<Department> departments;
    private List<Specialist> specialists;
    private List<CustomerService> services;

    public void addDepartment(Department department) {
        if (CollectionUtils.isEmpty(departments)) {
            departments = new ArrayList<>();
        }
        departments.add(department);
    }

}
