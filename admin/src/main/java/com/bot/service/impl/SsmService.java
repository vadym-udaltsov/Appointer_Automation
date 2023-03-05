package com.bot.service.impl;

import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterRequest;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterResult;
import com.bot.service.ISsmService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SsmService implements ISsmService {

    @Autowired
    private AWSSimpleSystemsManagement ssmClient;

    @Override
    public String getParameterValue(String parameterName) {
        log.info("Got request for parameter: {}", parameterName);
        GetParameterRequest request = new GetParameterRequest()
                .withName(parameterName)
                .withWithDecryption(true);
        GetParameterResult parameter = ssmClient.getParameter(request);
        String value = parameter.getParameter().getValue();
        log.info("Successfully got parameter with name: {}", parameterName);
        return value;
    }
}
