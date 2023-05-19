package com.commons.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Specialist {

    private String id;
    private String name;
    @JsonProperty("pn")
    private String phoneNumber;
}
