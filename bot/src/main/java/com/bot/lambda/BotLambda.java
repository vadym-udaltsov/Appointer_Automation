package com.bot.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.bot.dagger.DaggerBotLambdaComponent;
import com.commons.model.ProxyResponse;
import com.commons.utils.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class BotLambda implements RequestStreamHandler {

    public BotLambda() {
        DaggerBotLambdaComponent.create().inject(this);
    }

    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
        System.out.println("Bot lambda handler ------");
        Map<String, Object> mapInput = JsonUtils.getObjectFromInputStreamByType(input, new TypeReference<>() {
        });
        System.out.println(mapInput);
        Map<String, String> pathParameters = (Map)mapInput.get("pathParameters");
        String id = pathParameters.get("id");
        System.out.println("Bot Id: " + id);
        String s = JsonUtils.convertObjectToString(mapInput);
        System.out.println(s);
        ProxyResponse response = ProxyResponse.builder().body("OK").statusCode(200).build();
        output.write(JsonUtils.convertObjectToString(response).getBytes(StandardCharsets.UTF_8));
    }
}
