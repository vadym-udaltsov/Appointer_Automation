package com.bot.service.aws.impl;

import com.bot.model.AuthData;
import com.bot.service.aws.ICognitoService;
import com.bot.service.aws.ISsmService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminInitiateAuthRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminInitiateAuthResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AuthFlowType;

import java.util.Map;

@Service
@Slf4j
public class CognitoService implements ICognitoService {

    private final String poolIdParamName;
    private final String clientIdParamName;
    private final ISsmService ssmService;
    private final CognitoIdentityProviderClient client;

    @Autowired
    public CognitoService(@Value("${userpool.id.name}") String poolIdParamName,
                          @Value("${userpool.client.id.name}") String clientIdParamName,
                          CognitoIdentityProviderClient client,
                          ISsmService ssmService) {
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
        AdminInitiateAuthRequest request = AdminInitiateAuthRequest.builder()
                .authFlow(AuthFlowType.ADMIN_NO_SRP_AUTH)
                .userPoolId(poolId)
                .clientId(clientId)
                .authParameters(Map.of("USERNAME", email, "PASSWORD", authData.getPassword()))
                .build();
        AdminInitiateAuthResponse response = client.adminInitiateAuth(request);
        String idToken = response.authenticationResult().idToken();
        log.info("Successfully got token from cognito: {}", idToken);
        return idToken;
    }
}
