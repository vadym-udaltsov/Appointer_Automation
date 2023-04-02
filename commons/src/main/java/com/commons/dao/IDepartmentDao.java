package com.commons.dao;

import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.commons.model.CustomerService;
import com.commons.model.Department;

import java.util.List;

public interface IDepartmentDao {

    boolean createItem(Department department);

    List<Department> findAllByQuery(QuerySpec querySpec);

    void addNewService(String email, String departmentName, CustomerService service);
}
