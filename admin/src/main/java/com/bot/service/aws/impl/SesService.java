package com.bot.service.aws.impl;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.VerifyEmailIdentityRequest;
import com.bot.service.aws.ISesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SesService implements ISesService {

    @Autowired
    private AmazonSimpleEmailService sesClient;

    @Override
    public void verifyCustomerEmail(String email) {
        log.info("Verifying email: {}", email);
        VerifyEmailIdentityRequest request = new VerifyEmailIdentityRequest().withEmailAddress(email);
        sesClient.verifyEmailIdentity(request);

        log.info("Successfully verified email: {}", email);
    }
}
