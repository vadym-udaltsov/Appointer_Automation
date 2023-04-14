package com.bot.dao.impl;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.AttributeValueUpdate;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import com.bot.dao.IContextDao;
import com.bot.model.Context;
import com.bot.model.Language;
import com.bot.util.Constants;
import com.commons.dao.AbstractDao;
import com.commons.dao.impl.DynamoDbFactory;

import java.util.List;
import java.util.Map;

public class ContextDao extends AbstractDao<Context> implements IContextDao {

    public ContextDao(DynamoDbFactory dynamoDbFactory) {
        super(dynamoDbFactory, Context.class, Context.TABLE_NAME);
    }

    @Override
    public void saveContext(Context context) {
        createItem(context);
    }

    @Override
    public Context getContext(long userId) {
        return getItemByHashKey(userId);
    }

    @Override
    public void resetLocationToDashboard(Context context) {
        UpdateItemRequest request = new UpdateItemRequest()
                .withTableName(Context.TABLE_NAME)
                .withKey(Map.of(Context.HASH_KEY, new AttributeValue().withN(String.valueOf(context.getUserId()))))
                .withUpdateExpression("SET n = :newLocation")
                .withExpressionAttributeValues(Map.of(":newLocation",
                        new AttributeValue().withL(
                                List.of(new AttributeValue(Constants.Processors.ASK_LANG),
                                        new AttributeValue(Constants.Processors.SET_LANG_ASK_CONT),
                                        new AttributeValue(Constants.Processors.SET_CONT_START_DASH)))));
        updateItem(request);
    }

    @Override
    public void setPhoneNumber(Context context, String number) {
        UpdateItemRequest request = new UpdateItemRequest()
                .withTableName(Context.TABLE_NAME)
                .withKey(Map.of(Context.HASH_KEY, new AttributeValue().withN(String.valueOf(context.getUserId()))))
                .withUpdateExpression("SET pn = :phoneNumber")
                .withExpressionAttributeValues(Map.of(":phoneNumber", new AttributeValue(number)));
        updateItem(request);
    }

    @Override
    public void updateLocale(long id, Language language) {
        UpdateItemRequest request = new UpdateItemRequest()
                .withTableName(Context.TABLE_NAME)
                .withKey(Map.of(Context.HASH_KEY, new AttributeValue().withN(String.valueOf(id))))
                .addAttributeUpdatesEntry(Context.LOCALE_FIELD,
                        new AttributeValueUpdate().withValue(new AttributeValue(language.name())));
        updateItem(request);
    }

    @Override
    public void updateLocation(Context context, String location) {
        List<String> navigation = context.getNavigation();

        UpdateItemRequest request = new UpdateItemRequest()
                .withTableName(Context.TABLE_NAME)
                .withKey(Map.of(Context.HASH_KEY, new AttributeValue().withN(String.valueOf(context.getUserId()))))
                .withUpdateExpression("SET n[" + (navigation.size()) + "] = :newLocation")
                .withConditionExpression("NOT contains(n, :newLocation)")
                .withExpressionAttributeValues(Map.of(":newLocation", new AttributeValue(location)));
        updateItem(request);
    }

    @Override
    public void removeLastLocation(Context context) {
        List<String> navigation = context.getNavigation();

        UpdateItemRequest request = new UpdateItemRequest()
                .withTableName(Context.TABLE_NAME)
                .withKey(Map.of(Context.HASH_KEY, new AttributeValue().withN(String.valueOf(context.getUserId()))))
                .withUpdateExpression("REMOVE n[" + (navigation.size() - 1) + "]");
        updateItem(request);
    }
}
