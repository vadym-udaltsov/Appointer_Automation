package com.bot.controller;

import com.bot.model.CreateServiceRequest;
import com.commons.request.UpdateServiceRequest;
import com.commons.model.SimpleResponse;
import com.commons.service.IDepartmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("admin/service")
@Slf4j
public class ServiceController {

    @Autowired
    private IDepartmentService departmentService;

    @PostMapping()
    public ResponseEntity<SimpleResponse> createService(@RequestBody CreateServiceRequest request) {
        log.info("Got request for creation new service");
        departmentService.addCustomerService(request.getCustomer(), request.getDepartment(), request.getService());
        return new ResponseEntity<>(SimpleResponse.builder().body("Created").build(), HttpStatus.OK);
    }

    @PutMapping()
    public ResponseEntity<SimpleResponse> updateService(@RequestBody UpdateServiceRequest request) {
        log.info("Got request for updating service");
        departmentService.updateCustomerService(request);
        return new ResponseEntity<>(SimpleResponse.builder().body("Update").build(), HttpStatus.OK);
    }

}
