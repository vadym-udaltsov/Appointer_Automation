package com.bot.dao;

import com.bot.model.Appointment;

import java.util.List;

public interface IAppointmentDao {

    boolean createItem(Appointment appointment);

    List<Appointment> getAppointmentsByUserId(long userId, long startDate, long finishDate);

    List<Appointment> getAppointmentsBySpecialists(List<String> specialistIds, long startDate, long finishDate);

}
