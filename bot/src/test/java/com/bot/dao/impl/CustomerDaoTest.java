package com.bot.dao.impl;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.commons.dao.impl.CustomerDao;
import com.commons.dao.impl.DynamoDbFactory;
import com.commons.model.Customer;
import org.junit.jupiter.api.Test;

public class CustomerDaoTest {
    private AmazonDynamoDB dynamoDb = AmazonDynamoDBClientBuilder.standard().build();
    private DynamoDbFactory dynamoDbFactory = new DynamoDbFactory(dynamoDb);
    private CustomerDao customerDao = new CustomerDao(dynamoDbFactory);

//    @Test
    void getContext() {
        customerDao.registerCustomer("sergii.udaltsov@gmail.com");
        System.out.println();
    }

//    @Test
    void getCustomerByEmail() {
        Customer itemByHashKey = customerDao.getItemByHashKey("sergii.udaltsov@gmail.com");
        System.out.println();
    }
}
