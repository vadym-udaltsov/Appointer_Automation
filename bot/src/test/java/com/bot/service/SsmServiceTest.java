package com.bot.service;

import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterRequest;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterResult;
import org.junit.jupiter.api.Test;

public class SsmServiceTest {

    private final AWSSimpleSystemsManagement client = AWSSimpleSystemsManagementClientBuilder.defaultClient();

//    @Test
    void testDecryption() {
        String paramName = "bot_url";
        GetParameterRequest request = new GetParameterRequest()
                .withName(paramName)
                .withWithDecryption(true);
        GetParameterResult parameter = client.getParameter(request);
        System.out.println();
    }
}
