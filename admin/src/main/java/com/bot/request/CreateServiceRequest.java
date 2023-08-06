package com.bot.request;

import com.commons.request.service.ServiceRequest;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateServiceRequest extends ServiceRequest {

    private String department;
    private String customer;
}
