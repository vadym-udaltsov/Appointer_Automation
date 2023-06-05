package com.commons.service.impl;

import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.commons.dao.IDepartmentDao;
import com.commons.model.CustomerService;
import com.commons.model.Department;
import com.commons.service.IDepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService implements IDepartmentService {

    private IDepartmentDao departmentDao;

    @Override
    public boolean updateDepartment(Department department) {
        return departmentDao.updateDepartment(department);
    }

    @Autowired
    public DepartmentService(IDepartmentDao departmentDao) {
        this.departmentDao = departmentDao;
    }

    @Override
    public Department getDepartmentById(String departmentId) {
        return departmentDao.getDepartmentById(departmentId);
    }

    @Override
    public void addCustomerService(String email, String departmentName, CustomerService service) {
        departmentDao.addNewService(email, departmentName, service);
    }

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
