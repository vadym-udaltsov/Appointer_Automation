package com.bot.dao.impl;

import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.NameMap;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ExecuteStatementResult;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.bot.dao.IAppointmentDao;
import com.bot.model.Appointment;
import com.commons.dao.AbstractDao;
import com.commons.dao.impl.DynamoDbFactory;
import com.commons.model.Department;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AppointmentDao extends AbstractDao<Appointment> implements IAppointmentDao {

    public AppointmentDao(DynamoDbFactory dynamoDbFactory) {
        super(dynamoDbFactory, Appointment.class, Appointment.TABLE_NAME);
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

        return getItemsByIndexQuery(spec, Appointment.INDEX_NAME);
    }

    @Override
    public List<Appointment> getAppointmentsByUserId(long userId, long startDate, long finishDate) {
        ExecuteStatementResult result = getItemsByQuery("SELECT * FROM appointment WHERE uid=" + userId
                + " AND d BETWEEN " + startDate + " AND " + finishDate);
        return parseDbResponse(result.getItems());
    }

    private List<Appointment> parseDbResponse(List<Map<String, AttributeValue>> items) {
        List<Appointment> appointments = new ArrayList<>();
        for (Map<String, AttributeValue> item : items) {
            Appointment appointment = new Appointment();
            appointment.setSpecialist(item.get("s").getS());
            appointment.setDate(Integer.parseInt(item.get("d").getN()));
            appointment.setService(item.get("serv").getS());
            appointment.setUserId(Long.parseLong(item.get("uid").getN()));
            appointment.setDepartmentId(item.get("did").getS());
            appointments.add(appointment);
        }
        return appointments;
    }
}
