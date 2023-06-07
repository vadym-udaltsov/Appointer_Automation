package com.bot.dao.impl;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.bot.model.Appointment;
import com.commons.dao.impl.DynamoDbFactory;
import com.commons.model.Department;
import org.junit.jupiter.api.Test;

import java.util.List;

public class AppointmentDaoTest {

    @Test
    public void shouldUpdateDepartment() {
        AmazonDynamoDB dynamoDb = AmazonDynamoDBClientBuilder.standard().build();
        DynamoDbFactory dynamoDbFactory = new DynamoDbFactory(dynamoDb);
        AppointmentDao appointmentDao = new AppointmentDao(dynamoDbFactory);
        Department department = new Department();
        department.setId("fc871929");
        List<Appointment> res = appointmentDao.getAppointmentsByDepartment(department, 1687528700, 1687528900);
        System.out.println();
    }
}
