package com.bot.service;

import com.bot.model.Appointment;
import com.bot.model.FreeSlot;
import com.commons.model.Department;

import java.util.List;
import java.util.Map;

public interface IAppointmentService {

    void save(Appointment appointment);

    List<Appointment> getAppointmentsByUserId(long userId, long startDate, long finishDate);

    List<Appointment> getAppointmentsBySpecialists(List<String> specialistIds, long startDate, long finishDate);

    Map<String, List<FreeSlot>> getFreeSlotsBySpecialists(List<String> specialistIds, Department department,
                                                          int month, int dayNumber);
}
