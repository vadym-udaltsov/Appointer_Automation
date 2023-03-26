package com.commons.service.impl;

import com.commons.dao.IDepartmentDao;
import com.commons.model.Department;
import com.commons.service.IDepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DepartmentService implements IDepartmentService {

    @Autowired
    private IDepartmentDao departmentDao;

    @Override
    public boolean createDepartment(Department department) {
        return departmentDao.createItem(department);
    }

}
