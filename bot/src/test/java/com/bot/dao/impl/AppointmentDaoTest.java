package com.bot.dao.impl;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.bot.model.Appointment;
import com.bot.util.DateUtils;
import com.commons.dao.impl.DynamoDbFactory;
import com.commons.model.Department;
import org.junit.jupiter.api.Test;

import java.util.List;

public class AppointmentDaoTest {

    private AmazonDynamoDB dynamoDb = AmazonDynamoDBClientBuilder.standard().build();
    private DynamoDbFactory dynamoDbFactory = new DynamoDbFactory(dynamoDb);
    private AppointmentDao appointmentDao = new AppointmentDao(dynamoDbFactory);

    //    @Test
    public void shouldUpdateDepartment() {
        Department department = new Department();
        department.setId("fc871929");
        List<Appointment> res = appointmentDao.getAppointmentsByDepartment(department, 1687528700, 1687528900);
        System.out.println();
    }

    @Test
    public void shouldGetAppointmentsByDay() {
        long startOfDay = DateUtils.getStartOrEndOfDay(Integer.parseInt("7"), Integer.parseInt("20"), false);
        long endOfDay = DateUtils.getStartOrEndOfDay(Integer.parseInt("7"), Integer.parseInt("20"), true);
        System.out.println(DateUtils.getDateTitle(startOfDay));
        System.out.println(DateUtils.getDateTitle(endOfDay));

//        List<Appointment> res = appointmentDao.getAppointmentsByUserId(538025182, startOfDay, endOfDay);
        System.out.println();
    }

}
