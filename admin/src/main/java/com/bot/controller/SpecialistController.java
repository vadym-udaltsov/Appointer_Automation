package com.bot.controller;

import com.commons.DbItemUpdateException;
import com.commons.model.SimpleResponse;
import com.commons.request.specialist.CreateSpecialistRequest;
import com.commons.request.specialist.DeleteSpecialistRequest;
import com.commons.request.specialist.UpdateSpecialistRequest;
import com.commons.service.IDepartmentService;
import com.commons.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("admin/specialist")
@Slf4j
public class SpecialistController {

    @Autowired
    private IDepartmentService departmentService;

    @PostMapping()
    public ResponseEntity<SimpleResponse> createSpecialist(@RequestBody CreateSpecialistRequest request) {
        log.info("Got request for creation new specialist: {}", JsonUtils.convertObjectToString(request));
        try {
            departmentService.addSpecialist(request);
        } catch (DbItemUpdateException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(SimpleResponse.builder().body(e.getMessage()).build(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(SimpleResponse.builder().body("Created").build(), HttpStatus.OK);
    }

    @PutMapping()
    public ResponseEntity<SimpleResponse> updateSpecialist(@RequestBody UpdateSpecialistRequest request) {
        log.info("Got request for updating specialist: {}", JsonUtils.convertObjectToString(request));
        try {
            departmentService.updateSpecialist(request);
        } catch (DbItemUpdateException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(SimpleResponse.builder().body(e.getMessage()).build(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(SimpleResponse.builder().body("Updated").build(), HttpStatus.OK);
    }

    @DeleteMapping()
    public ResponseEntity<SimpleResponse> deleteSpecialist(@RequestBody DeleteSpecialistRequest request) {
        log.info("Got request for deleting specialist: {}", JsonUtils.convertObjectToString(request));
        try {
            departmentService.deleteSpecialist(request);
        } catch (DbItemUpdateException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(SimpleResponse.builder().body(e.getMessage()).build(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(SimpleResponse.builder().body("Deleted").build(), HttpStatus.OK);
    }
}
