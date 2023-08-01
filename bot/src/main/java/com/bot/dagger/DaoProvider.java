package com.bot.dagger;

import com.bot.dao.IAppointmentDao;
import com.bot.dao.IContextDao;
import com.bot.dao.impl.AppointmentDao;
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
    public static IAppointmentDao appointmentDao(DynamoDbFactory factory) {
        return new AppointmentDao(factory);
    }

    @Provides
    @Singleton
    public static IContextDao contextDao(DynamoDbFactory factory) {
        return new ContextDao(factory);
    }

    @Provides
    @Singleton
    public static IDepartmentDao departmentDao(DynamoDbFactory factory) {
        return new DepartmentDao(factory);
    }
}
