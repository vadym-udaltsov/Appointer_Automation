package com.bot.service;

import com.bot.model.AuthData;
import com.bot.model.ChangePasswordData;

public interface ICognitoService {

    String getCustomerToken(AuthData authData);

    void confirmPassword(ChangePasswordData data);

    void resetPasswordInit(String email);

}
