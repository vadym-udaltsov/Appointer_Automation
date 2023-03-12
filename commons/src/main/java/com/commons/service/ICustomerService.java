package com.commons.service;

import com.commons.model.Customer;
import com.commons.model.Department;

public interface ICustomerService {

    void createCustomer(Customer customer);

    Customer getCustomerByEmail(String email);

    void addCustomerDepartment(String email, Department department);
}
