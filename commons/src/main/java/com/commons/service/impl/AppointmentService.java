package com.commons.service.impl;


import com.commons.dao.IAppointmentDao;
import com.commons.model.Appointment;
import com.commons.model.FreeSlot;
import com.commons.service.IAppointmentService;
import com.commons.model.Department;
import com.commons.model.Specialist;
import com.commons.utils.DateUtils;
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
    public void delete(Appointment appointment) {
        appointmentDao.deleteItem(appointment);
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
    public void deleteAppointmentsBySpecialist(String departmentId, String specialist, long endDate) {
        appointmentDao.deleteSpecialistAppointments(specialist, departmentId, endDate);
    }

    @Override
    public List<Appointment> getAppointmentsBySpecialist(Department department, String specialist, long startDate,
                                                         long finishDate) {
        String specId = specialist + "::" + department.getId();
        return appointmentDao.getAppointmentsBySpecialist(specId, startDate, finishDate);
    }

    @Override
    public List<FreeSlot> getFreeSlotsBySpecialist(Department department, String specialist, int month, int dayNumber) {
        long startDate = DateUtils.getPointOfDay(month, dayNumber, department.getStartWork());
        long finishDate = DateUtils.getPointOfDay(month, dayNumber, department.getEndWork());
        long now = DateUtils.now(department);
        int todayDayNumber = DateUtils.getNumberOfCurrentDay(department);
        String specId = specialist + "::" + department.getId();
        List<Appointment> specAppointments = appointmentDao.getAppointmentsBySpecialist(specId, startDate, finishDate);
        List<FreeSlot> slots = getSlots(specAppointments, startDate, dayNumber, todayDayNumber, specialist, finishDate, now);
        if (specAppointments.isEmpty()) {
            if (dayNumber == todayDayNumber && now > startDate) {
                startDate = now;
            }
            long wholeDay = finishDate - startDate;
            return new ArrayList<>(List.of(FreeSlot.builder()
                    .startPoint(startDate)
                    .durationSec(wholeDay)
                    .specialist(specialist)
                    .build()));
        } else {
            return slots;
        }
    }

    @Override
    public Map<String, List<FreeSlot>> getFreeSlotsByDepartment(Department department, int month, int dayNumber) {
        long startDate = DateUtils.getPointOfDay(month, dayNumber, department.getStartWork());
        long finishDate = DateUtils.getPointOfDay(month, dayNumber, department.getEndWork());
        long now = DateUtils.now(department);
        int todayDayNumber = DateUtils.getNumberOfCurrentDay(department);
        List<Specialist> departmentSpecialists = department.getAvailableSpecialists();
        List<String> specialistNames = departmentSpecialists.stream().map(Specialist::getName).collect(Collectors.toList());

        List<Appointment> appointments = appointmentDao.getAppointmentsByDepartment(department, startDate, finishDate)
                .stream()
                .filter(a -> specialistNames.contains(a.getSpecialist()))
                .collect(Collectors.toList());
        Map<String, List<Appointment>> appointmentsBySpecialists = appointments.stream()
                .collect(Collectors.groupingBy(Appointment::getSpecialist));

        Map<String, List<FreeSlot>> result = new HashMap<>();
        for (Map.Entry<String, List<Appointment>> entry : appointmentsBySpecialists.entrySet()) {
            String specialist = entry.getKey();
            List<FreeSlot> slots = getSlots(entry.getValue(), startDate, dayNumber, todayDayNumber, entry.getKey(),
                    finishDate, now);
            result.put(specialist, slots);
        }

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

    private List<FreeSlot> getSlots(List<Appointment> specAppointments, long startDate, int dayNumber, int todayDayNumber,
                                    String specialist, long finishDate, long now) {
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
        return slots;
    }
}
