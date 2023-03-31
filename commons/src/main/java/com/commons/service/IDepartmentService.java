package com.commons.service;

import com.commons.model.Department;

import java.util.List;

public interface IDepartmentService {

    boolean createDepartment(Department department);

    List<Department> getCustomerDepartments(String customer);
}
