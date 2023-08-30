package com.commons.service;

import com.commons.model.Appointment;
import com.commons.model.Department;
import com.commons.model.FreeSlot;

import java.util.List;
import java.util.Map;

public interface IAppointmentService {

    void save(Appointment appointment);

    void createAppointments(List<Appointment> appointments);

    void delete(Appointment appointment);

    void delete(List<Appointment> appointments);

    List<Appointment> getAppointmentsByUserId(long userId, long startDate, long finishDate);

    List<Appointment> getAppointmentsBySpecialist(Department department, String specialist, long startDate, long finishDate);

    void deleteAppointmentsBySpecialist(String departmentId, String specialist, long endDate);

    void deleteAppointmentsByClientId(long userId);

    List<Appointment> getAppointmentsByDepartment(Department department, long startDate, long finishDate);

    Map<String, List<FreeSlot>> getFreeSlotsByDepartment(Department department, int month, int dayNumber);

    List<FreeSlot> getFreeSlotsBySpecialist(Department department, String specialist, int month, int dayNumber);
}
