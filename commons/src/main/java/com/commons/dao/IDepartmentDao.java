package com.commons.dao;

import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.commons.model.CustomerService;
import com.commons.model.Department;
import com.commons.request.UpdateServiceRequest;

import java.util.List;

public interface IDepartmentDao {

    boolean createItem(Department department);

    boolean updateDepartment(Department department);

    Department getDepartmentById(String departmentId);

    List<Department> findAllByQuery(QuerySpec querySpec);

    void addNewService(String email, String departmentName, CustomerService service);

    void updateService(UpdateServiceRequest request);
}
