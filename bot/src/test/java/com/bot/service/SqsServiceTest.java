package com.bot.service;

import com.commons.service.ISqsService;
import com.commons.service.impl.SqsService;
import com.commons.utils.JsonUtils;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.sqs.SqsClient;

import java.util.HashMap;
import java.util.Map;

public class SqsServiceTest {
    private final SqsClient sqsClient = SqsClient.builder().build();
    private final ISqsService sqsService = new SqsService(sqsClient, "https://sqs.eu-central-1.amazonaws.com/773974733061/botQueue");

//    @Test
    public void testSendMessage() {
        Map<String, String> messageData = new HashMap<>();
        messageData.put("email", "test.sergii@gmail.com");
        messageData.put("bot_name", "Sergii Bot");
        messageData.put("phone_number", "+380672413127");

        sqsService.sendMessage(JsonUtils.convertObjectToString(messageData));
    }

}
