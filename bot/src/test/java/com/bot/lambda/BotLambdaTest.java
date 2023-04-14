package com.bot.lambda;

import com.commons.utils.JsonUtils;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;

import static org.junit.jupiter.api.Assertions.*;

class BotLambdaTest {

//    @Test
//    public void testLambda() throws IOException {
//        String up = "{\n" +
//                "    \"update_id\": 170516072,\n" +
//                "    \"message\": {\n" +
//                "        \"message_id\": 17,\n" +
//                "        \"from\": {\n" +
//                "            \"id\": 538025182,\n" +
//                "            \"first_name\": \"Sergey\",\n" +
//                "            \"is_bot\": false,\n" +
//                "            \"last_name\": \"Udaltsov\",\n" +
//                "            \"username\": \"Sergudal\",\n" +
//                "            \"language_code\": \"ru\"\n" +
//                "        },\n" +
//                "        \"date\": 1681053924,\n" +
//                "        \"chat\": {\n" +
//                "            \"id\": 538025182,\n" +
//                "            \"type\": \"private\",\n" +
//                "            \"first_name\": \"Sergey\",\n" +
//                "            \"last_name\": \"Udaltsov\",\n" +
//                "            \"username\": \"Sergudal\"\n" +
//                "        },\n" +
//                "        \"text\": \"Оор\"\n" +
//                "    }\n" +
//                "}";
//
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        ObjectOutputStream oos = new ObjectOutputStream(baos);
//
//        Update update = JsonUtils.parseStringToObject(up, Update.class);
//        oos.writeObject(update);
//
//        oos.flush();
//        oos.close();
//
//        InputStream is = new ByteArrayInputStream(baos.toByteArray());
//        BotLambda botLambda = new BotLambda();
//        botLambda.testMethod(update);
////        botLambda.handleRequest(is, null, null);
//        System.out.println();
//    }

}