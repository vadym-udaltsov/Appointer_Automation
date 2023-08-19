package com.bot.dao.impl;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.commons.dao.impl.AppointmentDao;
import com.commons.dao.impl.DynamoDbFactory;
import com.commons.model.Appointment;
import com.commons.model.Department;
import com.commons.utils.DateUtils;
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

//    @Test
    public void shouldGetClientAppointments() {
       appointmentDao.deleteClientAppointments(596819031);
        System.out.println();
    }

    //    @Test
    public void shouldDeleteAppointmentsBySpecialistId() {
        Department department = new Department();
        department.setId("fc871929");
        department.setZone("Europe/Berlin");
        long endDate = DateUtils.getEndOfMonthDate(department, true);
        appointmentDao.deleteSpecialistAppointments("Tatiana", "52c59292", endDate);
        System.out.println();
    }

    //    @Test
    public void shouldCreateAppointments() {
        long startDate = 152445552;
        for (int i = 0; i < 105; i++) {
            Appointment appointment = Appointment.builder()
                    .date(startDate)
                    .id("Tatiana::52c59292")
                    .departmentId("52c59292")
                    .service("TEST")
                    .specialist("Tatiana")
                    .duration(60)
                    .build();
            appointmentDao.createItem(appointment);
            startDate += 1000;
        }
//        appointmentDao.deleteSpecialistAppointments("Tatiana", "52c59292", endDate);
        System.out.println();
    }

    //    @Test
    public void shouldGetAppointmentsByDay() {
        long startOfDay = DateUtils.getStartOrEndOfDay(Integer.parseInt("7"), Integer.parseInt("20"), false);
        long endOfDay = DateUtils.getStartOrEndOfDay(Integer.parseInt("7"), Integer.parseInt("20"), true);
        System.out.println(DateUtils.getDateTitle(startOfDay));
        System.out.println(DateUtils.getDateTitle(endOfDay));

//        List<Appointment> res = appointmentDao.getAppointmentsByUserId(538025182, startOfDay, endOfDay);
        System.out.println();
    }

    //    @Test
    public void shouldGetAppointmentsBySpecialist() {
        long startOfDay = DateUtils.getStartOrEndOfDay(Integer.parseInt("7"), Integer.parseInt("25"), false);
        long endOfDay = DateUtils.getStartOrEndOfDay(Integer.parseInt("7"), Integer.parseInt("28"), true);
        System.out.println(DateUtils.getDateTitle(startOfDay));
        System.out.println(DateUtils.getDateTitle(endOfDay));
        List<Appointment> appointments = appointmentDao.getAppointmentsBySpecialist("Tatiana::52c59292", startOfDay, endOfDay);
//        List<Appointment> res = appointmentDao.getAppointmentsByUserId(538025182, startOfDay, endOfDay);
        System.out.println();
    }

}
