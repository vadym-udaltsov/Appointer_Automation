package com.bot.model;

import com.commons.model.Department;
import com.commons.model.DepartmentType;
import com.commons.model.TimeZoneDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DepartmentData {

    private List<Department> customerDepartments;
    private List<DepartmentType> availableTypes;
    private List<TimeZoneDto> availableZones;
    private boolean isRegistered;
    private boolean admin;
}
