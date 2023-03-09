package com.bot.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.CognitoUserPoolPostConfirmationEvent;
import com.bot.lambda.dagger.DaggerLambdaComponent;
import com.commons.model.Customer;
import com.commons.service.ICustomerService;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import java.util.Map;

@Slf4j
public class RegistrationLambda implements RequestHandler<CognitoUserPoolPostConfirmationEvent, CognitoUserPoolPostConfirmationEvent> {

    @Inject
    ICustomerService customerService;

    public RegistrationLambda() {
        DaggerLambdaComponent.create().inject(this);
    }

    @Override
    public CognitoUserPoolPostConfirmationEvent handleRequest(CognitoUserPoolPostConfirmationEvent event, Context context) {
        Map<String, String> userAttributes = event.getRequest().getUserAttributes();
        log.info("Got request: {}", userAttributes);

        String email = userAttributes.get("email");
        String phoneNumber = userAttributes.get("phone_number");
        customerService.createCustomer(Customer.builder()
                .email(email)
                .phone(phoneNumber)
                .build());
        log.info("Successfully created new customer with email: {} and phone number: {}", email, phoneNumber);
        return event;
    }
}
