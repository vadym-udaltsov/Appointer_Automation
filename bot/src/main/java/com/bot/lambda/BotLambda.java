package com.bot.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.bot.dagger.DaggerBotLambdaComponent;
import com.commons.model.ProxyResponse;
import com.commons.utils.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class BotLambda implements RequestStreamHandler {

    TelegramBot botExecutor;

    public BotLambda() {
        DaggerBotLambdaComponent.create().inject(this);
    }

    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
        System.out.println("Bot lambda handler ------");
        Map<String, Object> mapInput = JsonUtils.parseInputStreamToObject(input, new TypeReference<>() {
        });
        System.out.println(mapInput);
        Map<String, Object> pathParameters = (Map)mapInput.get("pathParameters");
        String departmentId = (String) pathParameters.get("id");
        String body = (String)mapInput.get("body");
        Update update = JsonUtils.parseStringToObject(body, Update.class);
        System.out.println("Update: " + JsonUtils.convertObjectToString(update));
        botExecutor.processBotMessage(departmentId, update);

        ProxyResponse response = ProxyResponse.builder().body("OK").statusCode(200).build();
        output.write(JsonUtils.convertObjectToString(response).getBytes(StandardCharsets.UTF_8));
    }

    public void testMethod(Update update) {
        botExecutor.processBotMessage("9de82f54", update);
    }

    @Inject
    public void setTelegramBot(TelegramBot telegramBot) {
        this.botExecutor = telegramBot;
    }
}
