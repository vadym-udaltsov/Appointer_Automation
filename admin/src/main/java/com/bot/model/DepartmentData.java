package com.bot.model;

import com.commons.model.Department;
import com.commons.model.DepartmentType;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DepartmentData {

    private List<Department> customerDepartments;
    private List<DepartmentType> availableTypes;
}
