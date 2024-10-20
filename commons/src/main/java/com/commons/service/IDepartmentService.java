package com.commons.service;

import com.commons.model.CustomerService;
import com.commons.model.Department;
import com.commons.request.admin.AdminRequest;
import com.commons.request.service.UpdateServiceRequest;
import com.commons.request.specialist.CreateSpecialistRequest;
import com.commons.request.specialist.DeleteSpecialistRequest;
import com.commons.request.specialist.UpdateSpecialistRequest;

import java.util.List;

public interface IDepartmentService {

    boolean createDepartment(Department department);

    boolean updateDepartment(Department department);

    void updateToken(String departmentName, String customer, String token);

    List<Department> getCustomerDepartments(String customer);

    Department getDepartmentById(String departmentId);

    void addAdmin(AdminRequest request);

    void deleteAdmin(AdminRequest request);

    void addSpecialist(CreateSpecialistRequest request);

    void updateSpecialist(UpdateSpecialistRequest request);

    void deleteSpecialist(DeleteSpecialistRequest request);

    void addCustomerService(String email, String departmentName, CustomerService service);

    void updateCustomerService(UpdateServiceRequest request);

    void deleteCustomerService(UpdateServiceRequest request);
}
