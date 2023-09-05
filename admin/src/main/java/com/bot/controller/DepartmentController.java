package com.bot.controller;

import com.bot.model.DepartmentData;
import com.bot.request.CreateDepartmentRequest;
import com.bot.request.UpdateWebhookRequest;
import com.bot.service.IBotService;
import com.commons.model.Customer;
import com.commons.model.Department;
import com.commons.model.DepartmentType;
import com.commons.model.SetWebHookResult;
import com.commons.model.SimpleResponse;
import com.commons.model.Specialist;
import com.commons.model.TimeZone;
import com.commons.service.ICustomerService;
import com.commons.service.IDepartmentService;
import com.commons.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("admin/department")
@Slf4j
public class DepartmentController {

    private final IDepartmentService departmentService;
    private final ICustomerService customerService;
    private final IBotService botService;

    @Autowired
    public DepartmentController(IDepartmentService departmentService, ICustomerService customerService,
                                IBotService botService) {
        this.departmentService = departmentService;
        this.customerService = customerService;
        this.botService = botService;
    }

    @GetMapping("data/{customer}")
    public ResponseEntity<DepartmentData> getDepartmentData(@PathVariable("customer") String customerEmail) {
        List<Department> customerDepartments = departmentService.getCustomerDepartments(customerEmail);
        DepartmentData data = DepartmentData.builder()
                .customerDepartments(customerDepartments)
                .availableTypes(List.of(DepartmentType.values()))
                .availableZones(TimeZone.buildDtos())
                .build();
        if (customerDepartments.size() == 0) {
            Customer customer = customerService.getCustomerByEmail(customerEmail);
            data.setRegistered(customer != null && customer.isBotRegistered());
        }
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<SimpleResponse> updateDepartment(@RequestBody Department department) {
        log.info("Got request for department update. Department: {}", JsonUtils.convertObjectToString(department));
        departmentService.updateDepartment(department);
        log.info("Department updated successfully");
        return new ResponseEntity<>(SimpleResponse.builder().body("Updated").build(), HttpStatus.OK);
    }

    @PutMapping()
    public ResponseEntity<SimpleResponse> updateWebhook(@RequestBody UpdateWebhookRequest request) {
        log.info("Got request for webhook update. Request: {}", JsonUtils.convertObjectToString(request));
        String departmentName = request.getDepartmentName();
        String customer = request.getEmail();
        String botToken = request.getBotToken();
        SetWebHookResult result = botService.registerNewWebHook(botToken, request.getDepartmentId());
        if (!result.isOk()) {
            return new ResponseEntity<>(SimpleResponse.builder().body("Failed to create webhook").build(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        departmentService.updateToken(departmentName, customer, botToken);
        return new ResponseEntity<>(SimpleResponse.builder().body("Webhook updated").build(),
                HttpStatus.OK);
    }

    @PostMapping("create")
    public ResponseEntity<SimpleResponse> createDepartment(@RequestBody CreateDepartmentRequest request) {
        String name = request.getName();
        String email = request.getEmail();
        String departmentTypeName = request.getType();
        Customer customer = customerService.getCustomerByEmail(email);
        if (customer == null) {
            return new ResponseEntity<>(SimpleResponse.builder().body("Failed to find customer").build(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        String zone = request.getZone();
        TimeZone.validateZoneTitle(zone);

        String departmentId = RandomStringUtils.randomAlphabetic(8);
        DepartmentType departmentType = DepartmentType.fromName(departmentTypeName);

        Specialist specialist = new Specialist();
        specialist.setId(RandomStringUtils.randomAlphabetic(4));
        specialist.setName("owner");
        specialist.setPhoneNumber(customer.getPhone());

        String botToken = request.getBotToken();
        SetWebHookResult result = botService.registerNewWebHook(botToken, departmentId);
        if (!result.isOk()) {
            return new ResponseEntity<>(SimpleResponse.builder().body("Failed to create webhook").build(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Department department = new Department();
        department.setName(name);
        department.setZone(zone);
        department.setType(departmentType);
        department.setId(departmentId);
        department.setCustomer(email);
        department.setStartWork(9);
        department.setEndWork(19);
        department.setNonWorkingDays(List.of(7));
        department.setServices(List.of());
        department.setToken(botToken);
        department.setAdmins(List.of(customer.getPhone()));
        department.setAvailableSpecialists(List.of(specialist));

        departmentService.createDepartment(department);

        customerService.registerCustomer(email);
        return new ResponseEntity<>(SimpleResponse.builder().body("Created").build(),
                HttpStatus.OK);
    }
}
