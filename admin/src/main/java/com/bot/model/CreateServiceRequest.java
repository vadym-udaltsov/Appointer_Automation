package com.bot.model;

import com.commons.request.ServiceRequest;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateServiceRequest extends ServiceRequest {

    private String department;
    private String customer;
}
