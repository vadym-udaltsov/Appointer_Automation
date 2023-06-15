package com.commons.request.service;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateServiceRequest extends ServiceRequest {

    private String departmentId;
    private String serviceName;
}
