package com.bot;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.CognitoUserPoolPostConfirmationEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Map;

@SpringBootApplication
@Slf4j
public class RegistrationLambda implements RequestHandler<CognitoUserPoolPostConfirmationEvent, CognitoUserPoolPostConfirmationEvent> {

    @Override
    public CognitoUserPoolPostConfirmationEvent handleRequest(CognitoUserPoolPostConfirmationEvent event, Context context) {
        Map<String, String> userAttributes = event.getRequest().getUserAttributes();
        log.info("Got request: {}", userAttributes);
        return event;
    }
}
