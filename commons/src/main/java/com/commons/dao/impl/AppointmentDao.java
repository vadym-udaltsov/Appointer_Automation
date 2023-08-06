package com.commons.dao.impl;

import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.NameMap;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.DeleteRequest;
import com.amazonaws.services.dynamodbv2.model.WriteRequest;
import com.commons.dao.AbstractDao;
import com.commons.dao.IAppointmentDao;
import com.commons.model.Appointment;
import com.commons.model.Department;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class AppointmentDao extends AbstractDao<Appointment> implements IAppointmentDao {

    public AppointmentDao(DynamoDbFactory dynamoDbFactory) {
        super(dynamoDbFactory, Appointment.class, Appointment.TABLE_NAME);
    }

    @Override
    public void deleteSpecialistAppointments(String specialist, String departmentId, long endDate) {
        String specId = specialist + "::" + departmentId;
        List<Appointment> appointments = getAppointmentsBySpecialist(specId, 0, endDate);
        List<WriteRequest> writeRequests = new ArrayList<>();

        for (Appointment item : appointments) {
            Map<String, AttributeValue> key = new HashMap<>();
            key.put("s", new AttributeValue(item.getId()));
            key.put("d", new AttributeValue().withN(String.valueOf(item.getDate())));
            DeleteRequest deleteRequest = new DeleteRequest().withKey(key);

            writeRequests.add(new WriteRequest().withDeleteRequest(deleteRequest));
        }

        batchDeleteItems(writeRequests);
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
    public List<Appointment> getAppointmentsBySpecialist(String specId, long startDate, long finishDate) {
        QuerySpec spec = new QuerySpec()
                .withKeyConditionExpression("#hash = :id AND #range BETWEEN :start AND :end")
                .withNameMap(new NameMap()
                        .with("#hash", "s")
                        .with("#range", "d"))
                .withValueMap(new ValueMap()
                        .withString(":id", specId)
                        .withNumber(":start", startDate)
                        .withNumber(":end", finishDate));
        return findAllByQuery(spec);
    }
}
