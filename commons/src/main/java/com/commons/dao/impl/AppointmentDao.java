package com.commons.dao.impl;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.NameMap;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.BatchWriteItemRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteRequest;
import com.amazonaws.services.dynamodbv2.model.PutRequest;
import com.amazonaws.services.dynamodbv2.model.WriteRequest;
import com.commons.dao.AbstractDao;
import com.commons.dao.IAppointmentDao;
import com.commons.model.Appointment;
import com.commons.model.Department;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Repository
public class AppointmentDao extends AbstractDao<Appointment> implements IAppointmentDao {

    public AppointmentDao(DynamoDbFactory dynamoDbFactory) {
        super(dynamoDbFactory, Appointment.class, Appointment.TABLE_NAME);
    }

    @Override
    public void deleteSpecialistAppointments(String specialistId, String departmentId, long endDate) {
        String appId = specialistId + "::" + departmentId;
        List<Appointment> appointments = getAppointmentsBySpecialist(appId, 0, endDate);
        List<WriteRequest> writeRequests = buildDeleteRequests(appointments);

        executeBatchWriteRequests(writeRequests);
    }

    @Override
    public void createAppointments(List<Appointment> appointments) {
        List<WriteRequest> writeRequests = buildWriteRequests(appointments);
        executeBatchWriteRequests(writeRequests);
    }

    @Override
    public void deleteClientAppointments(long userId) {
        List<Appointment> clientAppointments = getClientAppointments(userId);
        List<WriteRequest> deleteRequests = buildDeleteRequests(clientAppointments);
        executeBatchWriteRequests(deleteRequests);
    }

    @Override
    public void deleteItems(List<Appointment> appointments) {
        List<WriteRequest> deleteRequests = buildDeleteRequests(appointments);
        executeBatchWriteRequests(deleteRequests);
    }

    @Override
    public List<Appointment> getAppointmentsByDepartment(Department department, long startDate, long finishDate) {
        String departmentId = department.getId();
        QuerySpec spec = new QuerySpec()
                .withKeyConditionExpression("#hash = :id AND #range BETWEEN :start AND :end")
                .withNameMap(new NameMap()
                        .with("#hash", "did")
                        .with("#range", "d"))
                .withValueMap(new ValueMap()
                        .withString(":id", departmentId)
                        .withNumber(":start", startDate)
                        .withNumber(":end", finishDate));
        return getItemsByIndexQuery(spec, Appointment.DEP_INDEX_NAME);
    }

    @Override
    public List<Appointment> getAppointmentsByUserId(long userId, long startDate, long finishDate) {
        QuerySpec spec = new QuerySpec()
                .withKeyConditionExpression("#hash = :id AND #range BETWEEN :start AND :end")
                .withNameMap(new NameMap()
                        .with("#hash", "uid")
                        .with("#range", "d"))
                .withValueMap(new ValueMap()
                        .withNumber(":id", userId)
                        .withNumber(":start", startDate)
                        .withNumber(":end", finishDate));
        return getItemsByIndexQuery(spec, Appointment.USER_INDEX_NAME);
    }

    @Override
    public List<Appointment> getAppointmentsBySpecialist(String appId, long startDate, long finishDate) {
        QuerySpec spec = new QuerySpec()
                .withKeyConditionExpression("#hash = :id AND #range BETWEEN :start AND :end")
                .withNameMap(new NameMap()
                        .with("#hash", "s")
                        .with("#range", "d"))
                .withValueMap(new ValueMap()
                        .withString(":id", appId)
                        .withNumber(":start", startDate)
                        .withNumber(":end", finishDate));
        return findAllByQuery(spec);
    }

    private List<WriteRequest> buildWriteRequests(List<Appointment> appointments) {
        List<WriteRequest> writeRequests = new ArrayList<>();

        for (Appointment item : appointments) {
            PutRequest putRequest = new PutRequest().withItem(toItemMap(item));
            writeRequests.add(new WriteRequest().withPutRequest(putRequest));
        }
        return writeRequests;
    }

    private Map<String, AttributeValue> toItemMap(Appointment appointment) {
        Map<String, AttributeValue> item = Map.of(
                Appointment.HASH_KEY, new AttributeValue(appointment.getId()),
                Appointment.RANGE_KEY, new AttributeValue().withN(String.valueOf(appointment.getDate())),
                "uid", new AttributeValue().withN(String.valueOf(appointment.getUserId())),
                "dur", new AttributeValue().withN(String.valueOf(appointment.getDuration())),
                "did", new AttributeValue(appointment.getDepartmentId()),
                "serv", new AttributeValue(appointment.getService()),
                "po", new AttributeValue().withBOOL(appointment.isPhoneOrder()));
        return item;
    }

    private Map<String, AttributeValue> buildKeyMap(Appointment appointment) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("s", new AttributeValue(appointment.getId()));
        key.put("d", new AttributeValue().withN(String.valueOf(appointment.getDate())));
        return key;
    }

    private List<WriteRequest> buildDeleteRequests(List<Appointment> appointments) {
        List<WriteRequest> writeRequests = new ArrayList<>();

        for (Appointment item : appointments) {
            Map<String, AttributeValue> key = buildKeyMap(item);
            DeleteRequest deleteRequest = new DeleteRequest().withKey(key);

            writeRequests.add(new WriteRequest().withDeleteRequest(deleteRequest));
        }
        return writeRequests;
    }

    private List<Appointment> getClientAppointments(long userId) {
        QuerySpec spec = new QuerySpec()
                .withKeyConditionExpression("#hash = :id")
                .withNameMap(new NameMap()
                        .with("#hash", "uid"))
                .withValueMap(new ValueMap()
                        .withNumber(":id", userId));
        return getItemsByIndexQuery(spec, Appointment.USER_INDEX_NAME);
    }
}
