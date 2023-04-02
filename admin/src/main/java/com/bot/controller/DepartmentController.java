package com.bot.controller;

import com.bot.model.DepartmentData;
import com.commons.model.Department;
import com.commons.model.DepartmentType;
import com.commons.model.SimpleResponse;
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

import javax.annotation.PostConstruct;
import java.util.List;

@RestController
@RequestMapping("admin/department")
@Slf4j
public class DepartmentController {

    @Autowired
    private IDepartmentService departmentService;

    @GetMapping("data/{customer}")
    public ResponseEntity<DepartmentData> getCustomer(@PathVariable("customer") String customer) {
        List<Department> customerDepartments = departmentService.getCustomerDepartments(customer);
        DepartmentData data = DepartmentData.builder()
                .customerDepartments(customerDepartments)
                .availableTypes(List.of(DepartmentType.values()))
                .build();
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

//    @PostMapping()
//    public ResponseEntity<SimpleResponse> createDepartment(@RequestBody Department department) {//should be checked if needed
//        log.info("Got request for creation new department------");
//        if (departmentService.createDepartment(department)) {
//
//        }
//        log.info("Name: {}", department.getName());
//        log.info("Customer: {}", department.getCustomer());
//        log.info("Type: {}", department.getType());
//        return new ResponseEntity<>(SimpleResponse.builder().body("Created").build(), HttpStatus.OK);
//    }

}
