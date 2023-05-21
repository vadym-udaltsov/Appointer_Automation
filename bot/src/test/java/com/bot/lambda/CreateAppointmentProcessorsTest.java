package com.bot.lambda;

import com.bot.model.Context;
import com.bot.model.FreeSlot;
import com.bot.service.IContextService;
import com.bot.util.Constants;
import com.commons.utils.JsonUtils;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class CreateAppointmentProcessorsTest {

//        @Test
    public void testCreateAppFirstStep() throws IOException {
        Map<String, Object> params = TestUtils.getParams(TestUtils.getUpdateStr("Записаться"));
        Context context = TestUtils.getContext(TestUtils.CR_APP_1_NAV, new HashMap<>());
        IContextService contextService = TestUtils.getMockedContextService(context);
        InputStream is = new ByteArrayInputStream(JsonUtils.convertObjectToString(params).getBytes(StandardCharsets.UTF_8));
        BotLambda botLambda = TestUtils.getBotLambda(contextService);
        botLambda.handleRequest(is, null, null);
    }

    //    @Test
    public void testCreateAppSecondStep() throws IOException {
        Map<String, Object> params = TestUtils.getParams(TestUtils.getUpdateStr("30"));
        HashMap<String, Object> map = new HashMap<>();
        map.put(Constants.MONTH, "5");
        Context context = TestUtils.getContext(TestUtils.CR_APP_2_NAV, map);
        IContextService contextService = TestUtils.getMockedContextService(context);
        InputStream is = new ByteArrayInputStream(JsonUtils.convertObjectToString(params).getBytes(StandardCharsets.UTF_8));
        BotLambda botLambda = TestUtils.getBotLambda(contextService);
        botLambda.handleRequest(is, null, null);
    }

//    @Test
    public void testCreateAppSecondStepNextMonth() throws IOException {
        Map<String, Object> params = TestUtils.getParams(TestUtils.getUpdateStr("nextMonth"));
        HashMap<String, Object> map = new HashMap<>();
        map.put(Constants.MONTH, "5");
        Context context = TestUtils.getContext(TestUtils.CR_APP_2_NAV, map);
        IContextService contextService = TestUtils.getMockedContextService(context);
        InputStream is = new ByteArrayInputStream(JsonUtils.convertObjectToString(params).getBytes(StandardCharsets.UTF_8));
        BotLambda botLambda = TestUtils.getBotLambda(contextService);
        botLambda.handleRequest(is, null, null);
    }

    //    @Test
    public void testCreateAppThirdStep() throws IOException {
        Map<String, Object> params = TestUtils.getParams(TestUtils.getUpdateStr("9b91bc16"));
        HashMap<String, Object> contextParams = new HashMap<>();
        contextParams.put(Constants.MONTH, "5");
        contextParams.put(Constants.SELECTED_DAY, "30");
        contextParams.put("9b917777",
                Map.of("specialist", "9b917777", "durationSec", 32400L, "startPoint", 1685437200L)
        );
        contextParams.put("9b91bc16", List.of(
                Map.of("specialist", "9b91bc16", "durationSec", 900L, "startPoint", 1685439000L),
                Map.of("specialist", "9b91bc16", "durationSec", 900L, "startPoint", 1685450700L),
                Map.of("specialist", "9b91bc16", "durationSec", 1800L, "startPoint", 1685457000L)
        ));

        Context context = TestUtils.getContext(TestUtils.CR_APP_3_NAV, contextParams);
        IContextService contextService = TestUtils.getMockedContextService(context);
        InputStream is = new ByteArrayInputStream(JsonUtils.convertObjectToString(params).getBytes(StandardCharsets.UTF_8));
        BotLambda botLambda = TestUtils.getBotLambda(contextService);
        botLambda.handleRequest(is, null, null);
    }
}