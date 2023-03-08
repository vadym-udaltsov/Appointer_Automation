package com.commons.dao.impl;

import com.commons.dao.AbstractDao;
import com.commons.dao.ICustomerDao;
import com.commons.model.Customer;

public class CustomerDao extends AbstractDao<Customer> implements ICustomerDao {

    public CustomerDao() {
        super("customer");
    }
}
