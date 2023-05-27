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
        ExecuteStatementResult result = getItemsByQuery("SELECT * FROM appointment WHERE s IN [" + specialistsStr
                + "] AND d BETWEEN " + startDate + " AND " + finishDate);
        return parseDbResponse(result.getItems());
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
            appointments.add(appointment);
        }
        return appointments;
    }
}
