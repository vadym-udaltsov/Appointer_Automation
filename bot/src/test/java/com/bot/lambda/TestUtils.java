package com.bot.lambda;

import com.bot.model.Context;
import com.bot.model.Language;
import com.bot.service.IContextService;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.objects.Update;
import software.amazon.awssdk.services.sqs.endpoints.internal.Value;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestUtils {

    public static final String UPD_TEMPLATE = "{\n" +
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
            "        \"text\": \"%s\"\n" +
            "    }\n" +
            "}";
    public static String getUpdateStr(String text) {
        return String.format(UPD_TEMPLATE, text);
    }
    public static List<String> CR_APP_1_NAV = new ArrayList<>(List.of(
            "askLang",
            "setLangAskContact",
            "setContactStartDash"));

    public static List<String> CR_APP_2_NAV = new ArrayList<>(List.of(
            "askLang",
            "setLangAskContact",
            "setContact",
            "startDash",
            "createApp1"));
    public static List<String> CR_APP_3_NAV = new ArrayList<>(List.of(
            "askLang",
            "setLangAskContact",
            "setContactStartDash",
            "startCreateApp",
            "setAppDateAskSpec"));

    public static List<String> CR_DAY_OFF_2_NAV = new ArrayList<>(List.of(
            "askLang",
            "setLangAskContact",
            "setContact",
            "startDash",
            "dayOffStart",
            "dayOffCreate1"));

    public static BotLambda getBotLambda(IContextService mockedContextService) {
        BotLambda botLambda = new BotLambda();
        TelegramBot botExecutor = botLambda.getBotExecutor();
        botExecutor.setContextService(mockedContextService);
        botExecutor.getFactory().setContextService(mockedContextService);
        return botLambda;
    }

    public static IContextService getMockedContextService(Context context) {
        IContextService contextService = Mockito.mock(IContextService.class);
        Mockito.when(contextService.getContext(Mockito.any(), Mockito.any())).thenReturn(context);
        Mockito.doNothing().when(contextService).updateLocation(Mockito.any(), Mockito.any());
        Mockito.doNothing().when(contextService).updateContext(Mockito.any());
        return contextService;
    }

    public static Context getContext(List<String> navigation, Map<String, Object> params) {
        Context context = new Context();
        context.setUserId(538025182);
        context.setLanguage(Language.RU);
        context.setPhoneNumber("+380505746182");
        context.setNavigation(navigation);
        context.setDepartmentId("52c59292");
        context.setParams(params);
        return context;
    }

    public static Map<String, Object> getParams(String update) {
        Map<String, Object> params = new HashMap<>();
        Map<String, Object> pathParameters = Map.of("id", "52c59292");
        params.put("pathParameters", pathParameters);
        params.put("body", update);
        return params;
    }
}
