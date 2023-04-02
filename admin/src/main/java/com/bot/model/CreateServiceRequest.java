package com.bot.model;

import com.commons.model.CustomerService;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateServiceRequest {

    private String department;
    private String customer;
    private CustomerService service;
}
