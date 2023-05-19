package com.bot.dao;

import com.bot.model.Appointment;

import java.util.List;

public interface IAppointmentDao {

    boolean createItem(Appointment appointment);

    List<Appointment> getAppointmentsBySpecialist(String specialistId, long startDate, long finishDate);

    List<Appointment> getAppointmentsBySpecialists(List<String> specialistIds, long startDate, long finishDate);

}
