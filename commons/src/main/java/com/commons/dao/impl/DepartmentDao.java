package com.commons.dao.impl;

import com.commons.dao.AbstractDao;
import com.commons.dao.IDepartmentDao;
import com.commons.model.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class DepartmentDao extends AbstractDao<Department> implements IDepartmentDao {

    @Autowired
    public DepartmentDao(DynamoDbFactory dynamoDbFactory) {
        super(dynamoDbFactory, Department.class, "department");
    }
}
