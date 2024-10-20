package com.commons.service.impl;

import com.commons.dao.ICustomerDao;
import com.commons.model.Customer;
import com.commons.model.Department;
import com.commons.service.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService implements ICustomerService {

    private ICustomerDao customerDao;

    @Autowired
    public CustomerService(ICustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    @Override
    public void createCustomer(Customer customer) {
        customerDao.createItem(customer);
    }

    @Override
    public void registerCustomer(String email) {
        customerDao.registerCustomer(email);
    }

    @Override
    public Customer getCustomerByEmail(String email) {
        return customerDao.getItemByHashKey(email);
    }

    @Override
    public void addCustomerDepartment(String email, Department department) {
        customerDao.addCustomerDepartment(email, department);
    }
}
