package com.bot.controller;

import com.bot.model.DepartmentData;
import com.bot.request.CreateDepartmentRequest;
import com.commons.service.ISqsService;
import com.commons.model.Customer;
import com.commons.model.Department;
import com.commons.model.DepartmentType;
import com.commons.model.SimpleResponse;
import com.commons.model.TimeZone;
import com.commons.service.ICustomerService;
import com.commons.service.IDepartmentService;
import com.commons.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("admin/department")
@Slf4j
public class DepartmentController {

    private final IDepartmentService departmentService;
    private final ICustomerService customerService;
    private final ISqsService sqsService;

    @Autowired
    public DepartmentController(IDepartmentService departmentService, ICustomerService customerService,
                                ISqsService sqsService) {
        this.departmentService = departmentService;
        this.customerService = customerService;
        this.sqsService = sqsService;
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
            data.setRegistered(customer != null && customer.isRegistered());
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

    @PostMapping("create")
    public ResponseEntity<SimpleResponse> createDepartment(@RequestBody CreateDepartmentRequest request) {
        String name = request.getName();
        String email = request.getEmail();
        Customer customer = customerService.getCustomerByEmail(email);
        if (customer == null || !customer.isRegistered()) {
            Map<String, String> messageData = new HashMap<>();
            messageData.put("email", email);
            messageData.put("bot_name", name);
            messageData.put("phone_number", "test");
            sqsService.sendMessage(JsonUtils.convertObjectToString(messageData));
            customerService.registerCustomer(email);
            return new ResponseEntity<>(SimpleResponse.builder().body("Created").build(), HttpStatus.OK);
        }
        return new ResponseEntity<>(SimpleResponse.builder().body("You have already requested department").build(),
                HttpStatus.BAD_REQUEST);
    }
}
