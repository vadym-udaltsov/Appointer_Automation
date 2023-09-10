package com.bot.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthData {
    private String email;
    private String password;
}
