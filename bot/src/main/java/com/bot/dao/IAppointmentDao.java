package com.bot.dao;

import com.bot.model.Appointment;
import com.commons.model.Department;

import java.util.List;

public interface IAppointmentDao {

    boolean createItem(Appointment appointment);

    List<Appointment> getAppointmentsByUserId(long userId, long startDate, long finishDate);

    List<Appointment> getAppointmentsByDepartment(Department department, long startDate, long finishDate);

}
