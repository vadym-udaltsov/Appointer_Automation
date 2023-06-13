package com.bot.dao.impl;

import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.NameMap;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.bot.dao.IAppointmentDao;
import com.bot.model.Appointment;
import com.commons.dao.AbstractDao;
import com.commons.dao.impl.DynamoDbFactory;
import com.commons.model.Department;

import java.util.List;

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
}
