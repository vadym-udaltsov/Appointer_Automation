package com.bot.service;

import com.bot.model.Appointment;
import com.bot.model.FreeSlot;
import com.commons.model.Department;

import java.util.List;
import java.util.Map;

public interface IAppointmentService {

    void save(Appointment appointment);

    void delete(Appointment appointment);

    List<Appointment> getAppointmentsByUserId(long userId, long startDate, long finishDate);

    List<Appointment> getAppointmentsByDepartment(Department department, long startDate, long finishDate);

    Map<String, List<FreeSlot>> getFreeSlotsByDepartment(Department department, int month, int dayNumber);
}
