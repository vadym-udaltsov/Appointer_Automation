package com.bot.service.aws;

import com.bot.model.AuthData;

public interface ICognitoService {

    String getCustomerToken(AuthData authData);

}
