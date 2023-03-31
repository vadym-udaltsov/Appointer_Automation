package com.commons.service.impl;

import com.commons.service.ISqsService;
import lombok.AllArgsConstructor;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@AllArgsConstructor
public class SqsService implements ISqsService {

    private SqsClient client;

    @Override
    public void sendMessage(String message) {
        SendMessageRequest request = SendMessageRequest.builder()
                .messageBody(message)
                .queueUrl("https://sqs.eu-central-1.amazonaws.com/773974733061/BotQueue")
                .build();
        client.sendMessage(request);
    }
}
