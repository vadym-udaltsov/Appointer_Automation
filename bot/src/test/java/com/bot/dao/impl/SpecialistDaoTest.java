package com.bot.dao.impl;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.commons.dao.impl.DepartmentDao;
import com.commons.dao.impl.DynamoDbFactory;
import com.commons.request.specialist.DeleteSpecialistRequest;
import org.junit.jupiter.api.Test;

public class SpecialistDaoTest {

    private static AmazonDynamoDB dynamoDb = AmazonDynamoDBClientBuilder.standard().build();
    private static DynamoDbFactory dynamoDbFactory = new DynamoDbFactory(dynamoDb);
    private static DepartmentDao departmentDao = new DepartmentDao(dynamoDbFactory);

//    @Test
    public void shouldDeleteSpecialist() {
        DeleteSpecialistRequest request = new DeleteSpecialistRequest();
        request.setDepartmentId("1eaaf36e");
        request.setSpecialistName("Test");
        departmentDao.deleteSpecialist(request);
    }
}
