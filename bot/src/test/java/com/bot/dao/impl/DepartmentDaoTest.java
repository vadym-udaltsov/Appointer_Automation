package com.bot.dao.impl;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.commons.dao.impl.DepartmentDao;
import com.commons.dao.impl.DynamoDbFactory;
import com.commons.model.Department;
import org.junit.jupiter.api.Test;

import java.util.List;

public class DepartmentDaoTest {

//    @Test
    public void shouldUpdateDepartment() {
        AmazonDynamoDB dynamoDb = AmazonDynamoDBClientBuilder.standard().build();
        DynamoDbFactory dynamoDbFactory = new DynamoDbFactory(dynamoDb);
        Department department = new Department();
        department.setId("fc871929");
        department.setCustomer("sergii.udaltsov@gmail.com");
        department.setName("Default");
        department.setStartWork(12);
        department.setNonWorkingDays(List.of(6, 7));
        DepartmentDao departmentDao = new DepartmentDao(dynamoDbFactory);
        departmentDao.updateDepartment(department);
    }
}
