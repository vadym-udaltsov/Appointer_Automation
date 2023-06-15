package com.bot.dao.impl;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.commons.dao.impl.DepartmentDao;
import com.commons.dao.impl.DynamoDbFactory;
import com.commons.model.CustomerService;
import com.commons.model.Department;
import com.commons.request.service.UpdateServiceRequest;

import java.util.List;

public class DepartmentDaoTest {

    private static AmazonDynamoDB dynamoDb = AmazonDynamoDBClientBuilder.standard().build();
    private static DynamoDbFactory dynamoDbFactory = new DynamoDbFactory(dynamoDb);
    private static DepartmentDao departmentDao = new DepartmentDao(dynamoDbFactory);

    //    @Test
    public void shouldUpdateDepartment() {
        Department department = new Department();
        department.setId("fc871929");
        department.setCustomer("sergii.udaltsov@gmail.com");
        department.setName("Default");
        department.setStartWork(12);
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
