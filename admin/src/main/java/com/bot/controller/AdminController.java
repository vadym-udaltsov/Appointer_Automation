package com.bot.controller;

import com.bot.model.AuthData;
import com.bot.model.SimpleResponse;
import com.bot.service.ICognitoService;
import com.bot.service.ISesService;
import com.bot.service.impl.SesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.NotAuthorizedException;

@RestController
@RequestMapping("admin")
@Slf4j
public class AdminController {

    private final ICognitoService cognitoService;
    private final ISesService sesService;

    @Autowired
    public AdminController(ICognitoService cognitoService, SesService sesService) {
        this.cognitoService = cognitoService;
        this.sesService = sesService;
    }

    @PostMapping("auth")
    public ResponseEntity<SimpleResponse> getToken(@RequestBody AuthData authData) {
        String email = authData.getEmail();
        try {
            String customerToken = cognitoService.getCustomerToken(authData);
            return new ResponseEntity<>(SimpleResponse.builder().body(customerToken).build(), HttpStatus.OK);
        } catch (NotAuthorizedException e) {
            log.warn("User: {} is not authorized", email);
            return new ResponseEntity<>(SimpleResponse.builder()
                    .body(String.format("User: %s is not authorized", email))
                    .build(), HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("email-verify")
    public ResponseEntity<SimpleResponse> verifyCustomerEmail(@RequestParam("email") String email) {
        sesService.verifyCustomerEmail(email);
        return new ResponseEntity<>(SimpleResponse.builder().body("Verified").build(), HttpStatus.OK);
    }

    @GetMapping("company")
    public ResponseEntity<SimpleResponse> testAuth() {
        log.info("Got authorization test request");
        return new ResponseEntity<>(SimpleResponse.builder().body("Authorized").build(), HttpStatus.OK);
    }
}

