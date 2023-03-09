package com.commons.dao.impl;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.commons.dao.AbstractDao;
import com.commons.dao.ICustomerDao;
import com.commons.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CustomerDao extends AbstractDao<Customer> implements ICustomerDao {

    @Autowired
    public CustomerDao(DynamoDBMapper dynamoDBMapper) {
        super(dynamoDBMapper, "customer");
    }
}
