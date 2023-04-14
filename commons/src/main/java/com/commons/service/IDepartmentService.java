package com.commons.service;

import com.commons.model.CustomerService;
import com.commons.model.Department;

import java.util.List;

public interface IDepartmentService {

    boolean createDepartment(Department department);

    List<Department> getCustomerDepartments(String customer);

    Department getDepartmentById(String departmentId);

    void addCustomerService(String email, String departmentName, CustomerService service);
}
