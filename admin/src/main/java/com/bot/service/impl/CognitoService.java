package com.bot.service.impl;

import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.bot.model.AuthData;
import com.bot.model.ChangePasswordData;
import com.bot.service.ICognitoService;
import com.commons.service.ISsmService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminGetUserRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminGetUserResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminInitiateAuthRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminInitiateAuthResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AuthFlowType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ConfirmForgotPasswordRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ForgotPasswordRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ForgotPasswordResponse;

import java.util.Map;

@Service
@Slf4j
public class CognitoService implements ICognitoService {

    private final String poolIdParamName;
    private final String clientIdParamName;
    private final ISsmService ssmService;
    private final CognitoIdentityProviderClient client;

    @Autowired
    public CognitoService(@Value("${userpool.id.name}") String poolIdParamName, @Value("${userpool.client.id.name}") String clientIdParamName, CognitoIdentityProviderClient client, ISsmService ssmService) {
        this.poolIdParamName = poolIdParamName;
        this.clientIdParamName = clientIdParamName;
        this.client = client;
        this.ssmService = ssmService;
    }

    @Override
    public String getCustomerToken(AuthData authData) {
        String email = authData.getEmail();
        log.info("Got token request for user: {}", email);
        String poolId = ssmService.getParameterValue(poolIdParamName);
        String clientId = ssmService.getParameterValue(clientIdParamName);
        AdminInitiateAuthRequest request = AdminInitiateAuthRequest.builder().authFlow(AuthFlowType.ADMIN_NO_SRP_AUTH).userPoolId(poolId).clientId(clientId).authParameters(Map.of("USERNAME", email, "PASSWORD", authData.getPassword())).build();
        AdminInitiateAuthResponse response = client.adminInitiateAuth(request);
        String idToken = response.authenticationResult().idToken();
        log.info("Successfully got token from cognito: {}", idToken);
        return idToken;
    }

    @Override
    public void confirmPassword(ChangePasswordData data) {
        String poolId = ssmService.getParameterValue(poolIdParamName);
        String clientId = ssmService.getParameterValue(clientIdParamName);
        AdminGetUserRequest getUserRequest = AdminGetUserRequest.builder()
                .userPoolId(poolId)
                .username(data.getEmail())
                .build();

        AdminGetUserResponse res = client.adminGetUser(getUserRequest);
        String username = res.username();

        ConfirmForgotPasswordRequest req = ConfirmForgotPasswordRequest.builder()
                .clientId(clientId)
                .username(username)
                .confirmationCode(data.getCode())
                .password(data.getPassword())
                .build();
        try {
            client.confirmForgotPassword(req);
        } catch (ConditionalCheckFailedException e) {
            log.info("Password changed");
        }
    }

    @Override
    public void resetPasswordInit(String email) {
        String poolId = ssmService.getParameterValue(poolIdParamName);
        String clientId = ssmService.getParameterValue(clientIdParamName);
        AdminGetUserRequest getUserRequest = AdminGetUserRequest.builder().userPoolId(poolId).username(email).build();

        AdminGetUserResponse res = client.adminGetUser(getUserRequest);
        String username = res.username();

        ForgotPasswordRequest request = ForgotPasswordRequest.builder()
                .clientId(clientId)
                .username(username)
                .build();
        client.forgotPassword(request);
    }
}
