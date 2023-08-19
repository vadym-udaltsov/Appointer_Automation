package com.bot.dao.impl;

import com.amazonaws.services.dynamodbv2.document.TableKeysAndAttributes;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.NameMap;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.AttributeValueUpdate;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import com.bot.dao.IContextDao;
import com.bot.model.Context;
import com.bot.model.Language;
import com.bot.util.Constants;
import com.commons.dao.AbstractDao;
import com.commons.dao.impl.DynamoDbFactory;
import com.commons.model.Appointment;
import com.commons.model.Department;
import org.apache.commons.collections4.ListUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ContextDao extends AbstractDao<Context> implements IContextDao {

    public ContextDao(DynamoDbFactory dynamoDbFactory) {
        super(dynamoDbFactory, Context.class, Context.TABLE_NAME);
    }

    @Override
    public void saveContext(Context context) {
        createItem(context);
    }

    @Override
    public void updateContext(Context context) {
        overwriteItem(context);
    }

    @Override
    public List<Context> getUserContextsByDepartment(Department department) {
        QuerySpec spec = new QuerySpec()
                .withKeyConditionExpression("#hash = :id")
                .withNameMap(new NameMap()
                        .with("#hash", "did"))
                .withValueMap(new ValueMap()
                        .withString(":id", department.getId()));
        return getItemsByIndexQuery(spec, Context.DID_ID_INDEX);
    }

    @Override
    public Context getAdminContext(String phoneNumber, String departmentId) {
        QuerySpec spec = new QuerySpec()
                .withKeyConditionExpression("pn = :pn AND did = :did")
                .withValueMap(new ValueMap()
                        .withString(":pn", phoneNumber)
                        .withString(":did", departmentId));
        return getItemByIndexQuery(spec, Context.INDEX_NAME);
    }

    @Override
    public Context getContext(long userId, String departmentId) {
        Context context = new Context();
        context.setUserId(userId);
        context.setDepartmentId(departmentId);
        return getItem(context);
    }

    @Override
    public void resetLocationToDashboard(Context context) {
        UpdateItemRequest request = new UpdateItemRequest()
                .withTableName(Context.TABLE_NAME)
                .withKey(Map.of(Context.HASH_KEY, new AttributeValue().withN(String.valueOf(context.getUserId())),
                        Context.RANGE_KEY, new AttributeValue().withS(context.getDepartmentId())))
                .withUpdateExpression("SET n = :newLocation")
                .withExpressionAttributeValues(Map.of(":newLocation",
                        new AttributeValue().withL(
                                List.of(new AttributeValue(Constants.Processors.ASK_LANG),
                                        new AttributeValue(Constants.Processors.SET_LANG_ASK_CONT),
                                        new AttributeValue(Constants.Processors.START_DASH)))));
        updateItem(request);
    }

    @Override
    public void updateLocale(long id, String departmentId, Language language) {
        UpdateItemRequest request = new UpdateItemRequest()
                .withTableName(Context.TABLE_NAME)
                .withKey(Map.of(Context.HASH_KEY, new AttributeValue().withN(String.valueOf(id)),
                        Context.RANGE_KEY, new AttributeValue().withS(departmentId)))
                .addAttributeUpdatesEntry(Context.LOCALE_FIELD,
                        new AttributeValueUpdate().withValue(new AttributeValue(language.name())));
        updateItem(request);
    }

    @Override
    public void updateLocation(Context context, String location) {
        List<String> navigation = context.getNavigation();
        UpdateItemRequest request = new UpdateItemRequest()
                .withTableName(Context.TABLE_NAME)
                .withKey(Map.of(Context.HASH_KEY, new AttributeValue().withN(String.valueOf(context.getUserId())),
                        Context.RANGE_KEY, new AttributeValue().withS(context.getDepartmentId())))
                .withUpdateExpression("SET n[" + (navigation.size()) + "] = :newLocation")
                .withConditionExpression("NOT contains(n, :newLocation)")
                .withExpressionAttributeValues(Map.of(":newLocation", new AttributeValue(location)));
        updateItem(request);
    }

    @Override
    public List<Context> getContextListByAppointments(List<Appointment> appointments) {
        List<Context> resultList = new ArrayList<>();
        if (appointments.size() == 0) {
            return resultList;
        }
        String departmentId = appointments.get(0).getDepartmentId();
        List<Object> keyList = new ArrayList<>();
        appointments.stream()
                .collect(Collectors.toMap(Appointment::getUserId, a -> departmentId, (a, b) -> a))
                .forEach((k, v) -> {
                    keyList.add(k);
                    keyList.add(v);
                });
        List<List<Object>> partitions = ListUtils.partition(keyList, 200);
        for (List<Object> partition : partitions) {
            resultList.addAll(addBatchGetResult(partition));
        }
        return resultList;
    }

    private List<Context> addBatchGetResult(List<Object> keyList) {
        TableKeysAndAttributes tableKeysAndAttributes = new TableKeysAndAttributes(Context.TABLE_NAME);
        tableKeysAndAttributes.addHashAndRangePrimaryKeys(Context.HASH_KEY, Context.RANGE_KEY, keyList.toArray());
        return getItemsByKeyList(tableKeysAndAttributes);
    }
}
