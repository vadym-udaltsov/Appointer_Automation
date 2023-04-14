package com.bot.dagger;

import com.bot.dao.IContextDao;
import com.bot.dao.impl.ContextDao;
import com.commons.dao.IDepartmentDao;
import com.commons.dao.impl.DepartmentDao;
import com.commons.dao.impl.DynamoDbFactory;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module(includes = AwsClientProvider.class)
public class DaoProvider {

    @Provides
    @Singleton
    IContextDao contextDao(DynamoDbFactory factory) {
        return new ContextDao(factory);
    }

    @Provides
    @Singleton
    IDepartmentDao departmentDao(DynamoDbFactory factory) {
        return new DepartmentDao(factory);
    }
}
