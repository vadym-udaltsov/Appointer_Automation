package com.bot.dao.impl;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.bot.converter.MapObjectConverter;
import com.bot.model.Appointment;
import com.bot.model.Context;
import com.bot.model.FreeSlot;
import com.bot.model.Strategy;
import com.bot.util.ContextUtils;
import com.bot.util.StrategyProvider;
import com.commons.dao.impl.DynamoDbFactory;
import com.commons.utils.JsonUtils;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

class ContextDaoTest {

    private AmazonDynamoDB dynamoDb = AmazonDynamoDBClientBuilder.standard().build();
    private DynamoDbFactory dynamoDbFactory = new DynamoDbFactory(dynamoDb);
    private ContextDao contextDao = new ContextDao(dynamoDbFactory);

    //    @Test
    void getContext() {
        Context context = contextDao.getContext(538025182, "bbad4b51");
        List<FreeSlot> slots = ContextUtils.getSpecialistSlotsConverted(context, "9b91bc16");
        System.out.println();
    }

    //    @Test
    public void shouldGetItemByIndex() {
        Context context = contextDao.getAdminContext("+380505746182", "52c59292");
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

//    @Test
    void testContextList() {
        List<Appointment> appointments = new ArrayList<>();
        for (int i = 1; i < 120; i++) {
            StringBuilder builder = new StringBuilder();
            for (int s = 1; s < 80; s++) {
                builder.append(s);
            }
            Appointment appointment = new Appointment("", 0L, i, builder.toString(), "", "", 0);
            appointments.add(appointment);
        }
        Appointment appointment1 = new Appointment("", 0L, 2006806288L, "790b0de6", "", "", 0);
        Appointment appointment2 = new Appointment("", 0L, 263609752L, "790b0de6", "", "", 0);
        Appointment appointment3 = new Appointment("", 0L, 641450521L, "790b0de6", "", "", 0);
        Appointment appointment4 = new Appointment("", 0L, 641450521L, "790b0de6", "", "", 0);
        appointments.add(appointment1);
        appointments.add(appointment2);
        appointments.add(appointment3);
        appointments.add(appointment4);
        List<Context> list = contextDao.getContextListByAppointments(appointments);
        System.out.println();
    }
}