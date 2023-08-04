package com.commons.dao;

import com.commons.model.Appointment;
import com.commons.model.Department;

import java.util.List;

public interface IAppointmentDao {

    boolean createItem(Appointment appointment);

    void deleteItem(Appointment appointment);

    void deleteSpecialistAppointments(String specialist, String departmentId, long endDate);

    List<Appointment> getAppointmentsByUserId(long userId, long startDate, long finishDate);

    List<Appointment> getAppointmentsBySpecialist(String specId, long startDate, long finishDate);

    List<Appointment> getAppointmentsByDepartment(Department department, long startDate, long finishDate);

}
