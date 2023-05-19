package com.bot.lambda;

import com.bot.model.Context;
import com.bot.model.Language;
import com.bot.service.IContextService;
import com.commons.utils.JsonUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class BotLambdaTest {

//    @Test
    public void testLambda() throws IOException {
        String up = "{\n" +
                "    \"update_id\": 170516072,\n" +
                "    \"message\": {\n" +
                "        \"message_id\": 17,\n" +
                "        \"from\": {\n" +
                "            \"id\": 538025182,\n" +
                "            \"first_name\": \"Sergey\",\n" +
                "            \"is_bot\": false,\n" +
                "            \"last_name\": \"Udaltsov\",\n" +
                "            \"username\": \"Sergudal\",\n" +
                "            \"language_code\": \"ru\"\n" +
                "        },\n" +
                "        \"date\": 1681053924,\n" +
                "        \"chat\": {\n" +
                "            \"id\": 538025182,\n" +
                "            \"type\": \"private\",\n" +
                "            \"first_name\": \"Sergey\",\n" +
                "            \"last_name\": \"Udaltsov\",\n" +
                "            \"username\": \"Sergudal\"\n" +
                "        },\n" +
                "        \"text\": \"Записаться\"\n" +
                "    }\n" +
                "}";

        Map<String, Object> params = new HashMap<>();
        Map<String, Object> pathParameters = Map.of("id", "bbad4b51");
        params.put("pathParameters", pathParameters);
        params.put("body", up);
        Context context = new Context();
        context.setUserId(538025182);
        context.setLanguage(Language.RU);
        context.setPhoneNumber("380505746182");
        context.setNavigation(new ArrayList<>(List.of(
                "askLang",
                "setLangAskContact",
                "setContactStartDash")));
        IContextService contextService = Mockito.mock(IContextService.class);
        Mockito.when(contextService.getContext(Mockito.any(), Mockito.any())).thenReturn(context);
        Mockito.doNothing().when(contextService).updateLocation(Mockito.any(), Mockito.any());
        InputStream is = new ByteArrayInputStream(JsonUtils.convertObjectToString(params).getBytes(StandardCharsets.UTF_8));
        BotLambda botLambda = new BotLambda();
        TelegramBot botExecutor = botLambda.getBotExecutor();
        botExecutor.setContextService(contextService);
        botExecutor.getFactory().setContextService(contextService);
        botLambda.handleRequest(is, null, null);
        System.out.println();
    }

}