package com.bot.dao.impl;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ExecuteStatementResult;
import com.bot.dao.IAppointmentDao;
import com.bot.model.Appointment;
import com.commons.dao.AbstractDao;
import com.commons.dao.impl.DynamoDbFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AppointmentDao extends AbstractDao<Appointment> implements IAppointmentDao {

    public AppointmentDao(DynamoDbFactory dynamoDbFactory) {
        super(dynamoDbFactory, Appointment.class, Appointment.TABLE_NAME);
    }

    @Override
    public List<Appointment> getAppointmentsBySpecialists(List<String> specialistIds, long startDate, long finishDate) {
        String specialistsStr = specialistIds.stream().collect(Collectors.joining("', '", "'", "'"));
        ExecuteStatementResult result = getItemsByQuery("SELECT * FROM appointment WHERE s IN [" + specialistsStr + "] AND d BETWEEN " + startDate + " AND " + finishDate);
        List<Map<String, AttributeValue>> items = result.getItems();
        List<Appointment> appointments = new ArrayList<>();
        for (Map<String, AttributeValue> item : items) {
            Appointment appointment = new Appointment();
            appointment.setSpecialist(item.get("s").getS());
            appointment.setDate(Integer.parseInt(item.get("d").getN()));
            appointment.setService(item.get("serv").getS());
            appointment.setUserId(Long.parseLong(item.get("uid").getN()));
            appointments.add(appointment);
        }
        return appointments;
    }

    @Override
    public List<Appointment> getAppointmentsBySpecialist(String specialistId, long startDate, long finishDate) {
        return null;
    }


}
