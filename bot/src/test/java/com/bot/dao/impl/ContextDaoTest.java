package com.bot.dao.impl;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.bot.converter.MapObjectConverter;
import com.bot.model.Context;
import com.bot.model.FreeSlot;
import com.bot.model.Strategy;
import com.bot.util.ContextUtils;
import com.bot.util.StrategyProvider;
import com.commons.dao.impl.DynamoDbFactory;
import com.commons.utils.JsonUtils;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

class ContextDaoTest {

    //    @Test
    void getContext() {
        AmazonDynamoDB dynamoDb = AmazonDynamoDBClientBuilder.standard().build();
        DynamoDbFactory dynamoDbFactory = new DynamoDbFactory(dynamoDb);
        ContextDao contextDao = new ContextDao(dynamoDbFactory);
        Context context = contextDao.getContext(538025182, "bbad4b51");
        List<FreeSlot> slots = ContextUtils.getSpecialistSlotsConverted(context, "9b91bc16");
        System.out.println();
    }

    //    @Test
    public void mapperTest() {
        MapObjectConverter mapper = new MapObjectConverter();
        Map<String, Object> test = Map.of("test", List.of("first", "second"));
        Map<String, AttributeValue> convert = mapper.convert(test);
        System.out.println();
    }

    //    @Test
    public void testStrategy() {
        List<String> nav = List.of("askLang",
                "setLangAskContact",
                "setContactStartDash",
                "startCreateApp",
                "setAppDateAskSpec");
        String strategyKey = "GENERAL::USER";
        Strategy any = StrategyProvider.getStrategyByLocationAndKey(nav, "any", strategyKey);
        System.out.println();
    }

    //    @Test
    public void testContext() {
        AmazonDynamoDB dynamoDb = AmazonDynamoDBClientBuilder.standard().build();
        DynamoDbFactory dynamoDbFactory = new DynamoDbFactory(dynamoDb);
        ContextDao contextDao = new ContextDao(dynamoDbFactory);
        Context context = new Context();
        context.setUserId(538025182);
        context.setDepartmentId("bbad4b51");
        FreeSlot slot = FreeSlot.builder()
                .durationSec(252525)
                .specialist("jnuunu")
                .startPoint(1524555)
                .build();
        Map<String, Object> map = JsonUtils.parseObjectToMap(slot);
        Map<String, Object> params = Map.of("test", List.of(map
        ));
        context.setParams(params);
        contextDao.overwriteItem(context);
//        Item item = context.toItem();
        System.out.println();
    }
}