package com.commons.dao;

import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.commons.model.CustomerService;
import com.commons.model.Department;
import com.commons.request.admin.AdminRequest;
import com.commons.request.service.UpdateServiceRequest;
import com.commons.request.specialist.CreateSpecialistRequest;
import com.commons.request.specialist.DeleteSpecialistRequest;
import com.commons.request.specialist.UpdateSpecialistRequest;

import java.util.List;

public interface IDepartmentDao {

    boolean createItem(Department department);

    boolean updateDepartment(Department department);

    void updateToken(String departmentName, String customer, String token);

    Department getDepartmentById(String departmentId);

    List<Department> findAllByQuery(QuerySpec querySpec);

    void addAdmin(AdminRequest request);

    void deleteAdmin(AdminRequest request);

    void addSpecialist(CreateSpecialistRequest request);

    void updateSpecialist(UpdateSpecialistRequest request);

    void deleteSpecialist(DeleteSpecialistRequest request);

    void addNewService(String email, String departmentName, CustomerService service);

    void updateService(UpdateServiceRequest request);

    void deleteCustomerService(UpdateServiceRequest request);
}
