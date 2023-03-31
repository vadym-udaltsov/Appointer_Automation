package com.commons.service.impl;

import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.commons.dao.IDepartmentDao;
import com.commons.model.Department;
import com.commons.service.IDepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService implements IDepartmentService {

    @Autowired
    private IDepartmentDao departmentDao;

    @Override
    public List<Department> getCustomerDepartments(String customer) {
        QuerySpec querySpec = new QuerySpec().withHashKey("c", customer);
        return departmentDao.findAllByQuery(querySpec);
    }

    @Override
    public boolean createDepartment(Department department) {
        return departmentDao.createItem(department);
    }

}
