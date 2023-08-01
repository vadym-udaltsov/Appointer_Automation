package com.bot.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.bot.dagger.DaggerBotLambdaComponent;
import com.bot.util.WarmUpUtils;
import com.commons.model.ProxyResponse;
import com.commons.utils.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.Getter;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Getter
public class BotLambda implements RequestStreamHandler {

    TelegramBot botExecutor;

    static {
        WarmUpUtils.warmUp();
    }

    public BotLambda() {
        DaggerBotLambdaComponent.create().inject(this);
    }

    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
        Map<String, Object> mapInput = JsonUtils.parseInputStreamToObject(input, new TypeReference<>() {
        });
        Map<String, Object> pathParameters = (Map) mapInput.get("pathParameters");
        String departmentId = (String) pathParameters.get("id");
        String body = (String) mapInput.get("body");
        Update update = JsonUtils.parseStringToObject(body, Update.class);
        botExecutor.processBotMessage(departmentId, update);

        ProxyResponse response = ProxyResponse.builder().body("OK").statusCode(200).build();
        output.write(JsonUtils.convertObjectToString(response).getBytes(StandardCharsets.UTF_8));
    }

    @Inject
    public void setTelegramBot(TelegramBot telegramBot) {
        this.botExecutor = telegramBot;
    }
}
