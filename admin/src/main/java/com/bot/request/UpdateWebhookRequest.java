package com.bot.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateWebhookRequest {

    private String email;
    private String departmentName;
    private String departmentId;
    private String botToken;
}
