package com.bot.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateDepartmentRequest {

    private String name;
    private String email;
    private String type;
    private String zone;
    private String botToken;
}
