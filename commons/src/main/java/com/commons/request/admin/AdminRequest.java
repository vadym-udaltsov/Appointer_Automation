package com.commons.request.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdminRequest {

    @JsonProperty("cn")
    private String customerName;

    @JsonProperty("dn")
    private String departmentName;

    @JsonProperty("pn")
    private String phoneNumber;
}
