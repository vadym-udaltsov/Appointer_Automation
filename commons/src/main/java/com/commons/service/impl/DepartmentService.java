package com.commons.service.impl;

import com.commons.model.Customer;
import com.commons.model.Department;
import com.commons.service.ICustomerService;
import com.commons.service.IDepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DepartmentService implements IDepartmentService {

    @Autowired
    private ICustomerService customerService;

    @Override
    public void createDepartment(String customerEmail, Department department) {
        Customer customer = customerService.getCustomerByEmail(customerEmail);
        customer.addDepartment(department);
        customerService.addCustomerDepartment(customerEmail, department);
    }

}
