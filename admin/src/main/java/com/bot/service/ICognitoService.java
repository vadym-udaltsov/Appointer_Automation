package com.bot.service;

import com.bot.model.AuthData;

public interface ICognitoService {

    String getCustomerToken(AuthData authData);

}
