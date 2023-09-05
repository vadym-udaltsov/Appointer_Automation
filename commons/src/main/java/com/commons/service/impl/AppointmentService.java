package com.commons.service.impl;


import com.commons.dao.IAppointmentDao;
import com.commons.model.Appointment;
import com.commons.model.Department;
import com.commons.model.FreeSlot;
import com.commons.model.Specialist;
import com.commons.service.IAppointmentService;
import com.commons.utils.DateUtils;
import com.commons.utils.DepartmentUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class AppointmentService implements IAppointmentService {

    private final IAppointmentDao appointmentDao;

    @Override
    public void save(Appointment appointment) {
        appointmentDao.createItem(appointment);
    }

    @Override
    public void createAppointments(List<Appointment> appointments) {
        appointmentDao.createAppointments(appointments);
    }

    @Override
    public void delete(Appointment appointment) {
        appointmentDao.deleteItem(appointment);
    }

    @Override
    public void deleteAppointmentsByClientId(long userId) {
        appointmentDao.deleteClientAppointments(userId);
    }

    @Override
    public void delete(List<Appointment> appointments) {
        appointmentDao.deleteItems(appointments);
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
    public List<Appointment> getAppointmentsBySpecialist(Department department, String specialistName, long startDate,
                                                         long finishDate) {
        Specialist specialist = DepartmentUtils.getSelectedSpecialist(department, specialistName);

        String specId = specialist.getId() + "::" + department.getId();
        return appointmentDao.getAppointmentsBySpecialist(specId, startDate, finishDate);
    }

    @Override
    public List<FreeSlot> getFreeSlotsBySpecialist(Department department, String specialistName, int year, int month,
                                                   int dayNumber) {
        long startDate = DateUtils.getPointOfDayWithYear(year, month, dayNumber, department.getStartWork(), department);
        long finishDate = DateUtils.getPointOfDayWithYear(year, month, dayNumber, department.getEndWork(), department);
        long now = DateUtils.nowZone(department);
        int todayDayNumber = DateUtils.getNumberOfCurrentDay(department);
        Specialist specialist = DepartmentUtils.getSelectedSpecialist(department, specialistName);
        String specId = specialist.getId() + "::" + department.getId();
        List<Appointment> specAppointments = appointmentDao.getAppointmentsBySpecialist(specId, startDate, finishDate);
        List<FreeSlot> slots = getSlots(specAppointments, startDate, dayNumber, todayDayNumber, specialistName, finishDate, now);
        if (specAppointments.isEmpty()) {
            if (dayNumber == todayDayNumber && now > startDate) {
                startDate = now;
            }
            long wholeDay = finishDate - startDate;
            return new ArrayList<>(List.of(FreeSlot.builder()
                    .startPoint(startDate)
                    .durationSec(wholeDay)
                    .specialist(specialistName)
                    .build()));
        } else {
            return slots;
        }
    }

    @Override
    public Map<String, List<FreeSlot>> getFreeSlotsByDepartment(Department department, int year, int month, int dayNumber) {
        long startDate = DateUtils.getPointOfDayWithYear(year, month, dayNumber, department.getStartWork(), department);
        long finishDate = DateUtils.getPointOfDayWithYear(year, month, dayNumber, department.getEndWork(), department);
        long now = DateUtils.nowZone(department);
        int todayDayNumber = DateUtils.getNumberOfCurrentDay(department);
        List<Specialist> departmentSpecialists = department.getAvailableSpecialists();
        List<String> specialistNames = departmentSpecialists.stream().map(Specialist::getName).collect(Collectors.toList());

        List<Appointment> appointments = appointmentDao.getAppointmentsByDepartment(department, startDate, finishDate)
                .stream()
                .filter(a -> specialistNames.contains(DepartmentUtils.getSpecialistName(department, a)))
                .collect(Collectors.toList());
        Map<String, List<Appointment>> appointmentsBySpecialists = appointments.stream()
                .collect(Collectors.groupingBy(a -> DepartmentUtils.getSpecialistName(department, a)));

        Map<String, List<FreeSlot>> result = new HashMap<>();
        for (Map.Entry<String, List<Appointment>> entry : appointmentsBySpecialists.entrySet()) {
            String specialist = entry.getKey();
            List<FreeSlot> slots = getSlots(entry.getValue(), startDate, dayNumber, todayDayNumber, entry.getKey(),
                    finishDate, now);
            result.put(specialist, slots);
        }

        for (Specialist specialist : departmentSpecialists) {
            String specName = specialist.getName();
            List<Appointment> specApp = appointmentsBySpecialists.get(specName);
            if (specApp == null || specApp.isEmpty()) {
                if (dayNumber == todayDayNumber && now > startDate) {
                    startDate = now;
                }
                long wholeDay = finishDate - startDate;
                result.put(specName, List.of(FreeSlot.builder()
                        .startPoint(startDate)
                        .durationSec(wholeDay)
                        .specialist(specName)
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
