package com.bot.dao.impl;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.commons.dao.impl.DepartmentDao;
import com.commons.dao.impl.DynamoDbFactory;
import com.commons.model.CustomerService;
import com.commons.model.Department;
import com.commons.model.DepartmentType;
import com.commons.request.admin.AdminRequest;
import com.commons.request.service.UpdateServiceRequest;
import com.commons.utils.DateUtils;
import org.junit.jupiter.api.Test;

import java.util.List;

public class DepartmentDaoTest {

    private static AmazonDynamoDB dynamoDb = AmazonDynamoDBClientBuilder.standard().build();
    private static DynamoDbFactory dynamoDbFactory = new DynamoDbFactory(dynamoDb);
    private static DepartmentDao departmentDao = new DepartmentDao(dynamoDbFactory);

//    @Test
    void shouldCreateDepartment() {
        Department department = new Department();
        department.setId("RmKbeHxL555");
        department.setCustomer("sergii.udaltsov555@gmail.com");
        department.setName("Лучший массаж в Баре55");
        department.setStartWork(9);
        department.setEndWork(18);
        department.setAppointmentsLimit(2);
        department.setZone("Europe/Berlin");
        department.setType(DepartmentType.GENERAL);
        department.setToken("ljnjnj");
        department.setBotName("jknvjnfjvn");
        department.setBotUserName("kjjkrvjkrbv");
        department.setNonWorkingDays(List.of(6, 7));
        department.setCreationDate(DateUtils.nowZone(department));
        departmentDao.createItem(department);
    }

//    @Test
    void shouldGetDepartment() {
        Department departmentById = departmentDao.getDepartmentById("RmKbeHxL555");
        System.out.println();
    }

//    @Test
    void shouldDeleteDepartment() {
        Department department = new Department();
        department.setId("RmKbeHxL555");
        department.setName("Лучший массаж в Баре55");
        department.setCustomer("sergii.udaltsov555@gmail.com");
        departmentDao.deleteItem(department);
    }

//    @Test
    public void shouldDeleteAdmin() {
        AdminRequest request = new AdminRequest();
        request.setDepartmentName("Default");
        request.setCustomerName("sergii.udaltsov@gmail.com");
        request.setPhoneNumber("test");
        departmentDao.deleteAdmin(request);
    }

//    @Test
    void shouldUpdateToken() {
        departmentDao.updateToken("My massage in Bar", "udalcov@ukr.net", "test token");
    }

//    @Test
    public void shouldAddNewAdmin() {
        AdminRequest request = new AdminRequest();
        request.setDepartmentName("Default");
        request.setCustomerName("sergii.udaltsov@gmail.com");
        request.setPhoneNumber("test2");
        departmentDao.addAdmin(request);
    }
//        @Test
    public void shouldUpdateDepartment() {
        Department department = new Department();
        department.setId("RmKbeHxL");
        department.setCustomer("sergii.udaltsov@gmail.com");
        department.setName("Лучший массаж в Баре");
        department.setStartWork(9);
        department.setEndWork(18);
        department.setAppointmentsLimit(2);
        department.setZone("Europe/Berlin");
        department.setNonWorkingDays(List.of(6, 7));
        departmentDao.updateDepartment(department);
    }

    //    @Test
    public void shouldAddServiceToDepartment() {
        Department department = new Department();
        department.setId("fc871929");
        department.setCustomer("sergii.udaltsov@gmail.com");
        department.setName("Default");
        CustomerService service = new CustomerService();
        service.setName("testDao");
        service.setPrice(10);
        service.setDuration(100);
        departmentDao.addNewService("sergii.udaltsov@gmail.com", "Default", service);
    }

//    @Test
    public void shouldUpdateDepartmentService() {
        UpdateServiceRequest request = new UpdateServiceRequest();
        request.setServiceName("Test");
        request.setDepartmentId("fc871929");
        CustomerService service = new CustomerService();
        service.setDuration(100);
        service.setName("new name");
        service.setPrice(15);
        request.setService(service);
        departmentDao.updateService(request);
    }

}
