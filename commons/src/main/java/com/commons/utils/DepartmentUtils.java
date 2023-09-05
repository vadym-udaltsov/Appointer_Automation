package com.commons.utils;

import com.commons.model.Appointment;
import com.commons.model.Department;
import com.commons.model.Specialist;

public class DepartmentUtils {

    private DepartmentUtils() {
        throw new RuntimeException("Utility class");
    }

    public static Specialist getSelectedSpecialist(Department department, String specName) {
        return department.getAvailableSpecialists().stream()
                .filter(s -> specName.equals(s.getName()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Specialist with name " + specName + " not found"));
    }

    public static String getSpecialistName(Department department, Appointment appointment) {
        String id = appointment.getId();
        String specialistId = id.split("::")[0];
        Specialist specialist = department.getAvailableSpecialists().stream()
                .filter(s -> specialistId.equals(s.getId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Specialist with id " + specialistId + " not found"));
        return specialist.getName();
    }
}
