package com.bot.lambda.dagger;

import com.commons.dao.ICustomerDao;
import com.commons.dao.impl.CustomerDao;
import com.commons.dao.impl.DynamoDbFactory;
import com.commons.service.ICustomerService;
import com.commons.service.impl.CustomerService;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module(includes = AwsClientProvider.class)
public class ServiceProvider {

    @Provides
    @Singleton
    public ICustomerDao customerDao(DynamoDbFactory factory) {
        return new CustomerDao(factory);
    }

    @Provides
    @Singleton
    public ICustomerService customerService(ICustomerDao customerDao) {
        return new CustomerService(customerDao);
    }
}
