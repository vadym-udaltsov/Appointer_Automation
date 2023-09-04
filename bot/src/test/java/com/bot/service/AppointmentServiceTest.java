package com.bot.service;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.commons.dao.impl.AppointmentDao;
import com.bot.model.BuildKeyboardRequest;
import com.bot.model.Context;
import com.commons.model.FreeSlot;
import com.bot.model.KeyBoardType;
import com.bot.processor.impl.general.user.appointment.create.AbstractGetCalendarProcessor;
import com.commons.service.impl.AppointmentService;
import com.bot.util.Constants;
import com.bot.util.KeyBoardUtils;
import com.bot.util.MessageUtils;
import com.commons.dao.impl.DepartmentDao;
import com.commons.dao.impl.DynamoDbFactory;
import com.commons.model.Department;
import com.commons.service.IAppointmentService;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppointmentServiceTest {
    private AmazonDynamoDB dynamoDb = AmazonDynamoDBClientBuilder.standard().build();
    private DynamoDbFactory dynamoDbFactory = new DynamoDbFactory(dynamoDb);
    private AppointmentDao appointmentDao = new AppointmentDao(dynamoDbFactory);
    private IAppointmentService appointmentService = new AppointmentService(appointmentDao);
    private DepartmentDao departmentDao = new DepartmentDao(dynamoDbFactory);

//    @Test
    public void getFreeSlotsBySpecialist() {
//        Department department = new Department();
//        department.setId("52c59292");
//        department.setStartWork(10);
//        department.setEndWork(19);
//        department.setZone("Europe/Berlin");
//        List<FreeSlot> slots = appointmentService.getFreeSlotsBySpecialist(department, "Tatiana", 8, 2);
//        System.out.println();
    }

//    @Test
    public void getBusyDayTitles() {
//        Department department = new Department();
//        department.setId("52c59292");
//        department.setStartWork(10);
//        department.setEndWork(19);
//        CustomerService service = new CustomerService();
//        service.setName("test");
//        service.setDuration(60);
//        department.setServices(List.of(service));
//        Specialist specialist = new Specialist();
//        specialist.setName("testName");
//        department.setAvailableSpecialists(List.of(specialist));
//        department.setZone("Europe/Berlin");
        Department department = departmentDao.getDepartmentById("52c59292");
        AbstractGetCalendarProcessor processor = new AbstractGetCalendarProcessor(appointmentService);
        List<String> titles = processor.defineBusyDayTitles(new ArrayList<>(), department, 8, "test", 2023);
        System.out.println();
        Context context = new Context();
        context.setParams(new HashMap<>());

        BuildKeyboardRequest datePickerRequest = BuildKeyboardRequest.builder()
                .type(KeyBoardType.TWO_ROW)
                .buttonsMap(MessageUtils.buildButtons(MessageUtils.commonButtons(titles), false))
                .params(Map.of(
                        Constants.DEPARTMENT, department,
                        Constants.IS_NEXT_MONTH, false,
                        Constants.CONTEXT, context))
                .build();
        KeyBoardUtils.buildDatePickerCreateAppointment(datePickerRequest);
    }
}
