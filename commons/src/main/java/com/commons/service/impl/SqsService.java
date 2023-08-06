package com.commons.service.impl;

import com.commons.service.ISqsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Component
public class SqsService implements ISqsService {

    private final SqsClient sqsClient;
    private final String queueUrl;

    @Autowired
    public SqsService(SqsClient sqsClient, @Value("${queue.url}") String queueUrl) {
        this.sqsClient = sqsClient;
        this.queueUrl = queueUrl;
    }

    @Override
    public void sendMessage(String message) {
        SendMessageRequest sendMessageRequest = SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(message)
                .build();

        sqsClient.sendMessage(sendMessageRequest);
    }
}
