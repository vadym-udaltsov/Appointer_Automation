package com.bot.service.admin.impl;

import com.commons.dao.ICustomerDao;
import com.commons.model.Customer;
import com.bot.service.admin.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService implements ICustomerService {

    @Autowired
    private ICustomerDao customerDao;

    @Override
    public void createCustomer(Customer customer) {
        customerDao.createItem(customer);
    }

}
