package com.commons.dao.impl;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import com.commons.dao.AbstractDao;
import com.commons.dao.IDepartmentDao;
import com.commons.model.CustomerService;
import com.commons.model.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class DepartmentDao extends AbstractDao<Department> implements IDepartmentDao {

    @Autowired
    public DepartmentDao(DynamoDbFactory dynamoDbFactory) {
        super(dynamoDbFactory, Department.class, "department");
    }

    @Override
    public void addNewService(String email, String departmentName, CustomerService service) {
        String listAttributeName = "s";
        AttributeValue newService = new AttributeValue().withM(Map.of(
                "name", new AttributeValue(service.getName()),
                "duration", new AttributeValue().withN(String.valueOf(service.getDuration())),
                "price", new AttributeValue().withN(String.valueOf(service.getPrice()))));

        UpdateItemRequest request = new UpdateItemRequest()
                .withTableName("department")
                .withKey(Map.of(
                        "c", new AttributeValue(email),
                        "n", new AttributeValue(departmentName)))
                .withConditionExpression("NOT contains(s, :newService)")
                .withUpdateExpression("SET " + listAttributeName + " = list_append(if_not_exists(" + listAttributeName + ", :empty_list), :newServiceList)")
                .withExpressionAttributeValues(Map.of(
                        ":newServiceList", new AttributeValue().withL(newService),
                        ":empty_list", new AttributeValue().withL(),
                        ":newService", newService));

        updateItem(request);
    }
}
