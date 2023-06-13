package com.bot.service.impl;

import com.bot.dao.IAppointmentDao;
import com.bot.model.Appointment;
import com.bot.model.FreeSlot;
import com.bot.service.IAppointmentService;
import com.bot.util.DateUtils;
import com.commons.model.CustomerService;
import com.commons.model.Department;
import com.commons.model.Specialist;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class AppointmentService implements IAppointmentService {

    private final IAppointmentDao appointmentDao;

    @Override
    public void save(Appointment appointment) {
        appointmentDao.createItem(appointment);
    }

    @Override
    public List<Appointment> getAppointmentsByDepartment(Department department, long startDate, long finishDate) {
        return appointmentDao.getAppointmentsByDepartment(department, startDate, finishDate);
    }

    @Override
    public List<Appointment> getAppointmentsByUserId(long userId, long startDate, long finishDate) {
        return appointmentDao.getAppointmentsByUserId(userId, startDate, finishDate);
    }

    @Override
    public Map<String, List<FreeSlot>> getFreeSlotsByDepartment(Department department, int month, int dayNumber) {
        long startDate = DateUtils.getPointOfDay(month, dayNumber, department.getStartWork());
        long finishDate = DateUtils.getPointOfDay(month, dayNumber, department.getEndWork());
        long now = DateUtils.now(department);
        int todayDayNumber = DateUtils.getNumberOfCurrentDay(department);

        List<Appointment> appointments = appointmentDao.getAppointmentsByDepartment(department, startDate, finishDate);
        Map<String, List<Appointment>> appointmentsBySpecialists = appointments.stream()
                .collect(Collectors.groupingBy(Appointment::getSpecialist));

        Map<String, Integer> durationsByServices = department.getServices().stream()
                .collect(Collectors.toMap(CustomerService::getName, CustomerService::getDuration));
        Map<String, List<FreeSlot>> result = new HashMap<>();
        for (Map.Entry<String, List<Appointment>> entry : appointmentsBySpecialists.entrySet()) {
            String specialist = entry.getKey();
            List<Appointment> specAppointments = entry.getValue();
            specAppointments.sort(Comparator.comparingLong(Appointment::getDate));
            long currentPoint = startDate;
            List<FreeSlot> slots = new ArrayList<>();
            for (Appointment appointment : specAppointments) {
                if (dayNumber == todayDayNumber && now > currentPoint) {
                    currentPoint = now;
                }
                long slotDuration = appointment.getDate() - currentPoint;
                if (slotDuration > 0) {
                    slots.add(FreeSlot.builder()
                            .specialist(specialist)
                            .startPoint(currentPoint)
                            .durationSec(slotDuration)
                            .build());
                }
                currentPoint = appointment.getDate() + appointment.getDuration() * 60L;
            }
            long restOfDay = finishDate - currentPoint;
            if (restOfDay > 0) {
                slots.add(FreeSlot.builder()
                        .specialist(specialist)
                        .startPoint(currentPoint)
                        .durationSec(restOfDay)
                        .build());
            }
            result.put(specialist, slots);
        }
        List<Specialist> departmentSpecialists = department.getAvailableSpecialists();

        for (Specialist specialist : departmentSpecialists) {
            String specId = specialist.getName();
            List<Appointment> specApp = appointmentsBySpecialists.get(specId);
            if (specApp == null || specApp.isEmpty()) {
                if (dayNumber == todayDayNumber && now > startDate) {
                    startDate = now;
                }
                long wholeDay = finishDate - startDate;
                result.put(specId, List.of(FreeSlot.builder()
                        .startPoint(startDate)
                        .durationSec(wholeDay)
                        .specialist(specId)
                        .build()));
            }
        }
        return result;
    }
}
