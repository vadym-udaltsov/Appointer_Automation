package com.bot.dagger;

import com.bot.dao.IAppointmentDao;
import com.bot.dao.IContextDao;
import com.bot.lambda.TelegramBot;
import com.bot.localization.ILocalizer;
import com.bot.localization.Localizer;
import com.bot.processor.IProcessorFactory;
import com.bot.service.IAppointmentService;
import com.bot.service.IContextService;
import com.bot.service.IMessageSender;
import com.bot.service.ISendMessageService;
import com.bot.service.impl.AppointmentService;
import com.bot.service.impl.ContextService;
import com.bot.service.impl.MessageSender;
import com.bot.service.impl.SendMessageService;
import com.commons.dao.IDepartmentDao;
import com.commons.service.IDepartmentService;
import com.commons.service.impl.DepartmentService;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module(includes = {DaoProvider.class,
})
public class ServiceProvider {

    @Provides
    @Singleton
    public ISendMessageService sendMessageService(IMessageSender sender, IContextService contextService,
                                                  ILocalizer localizer) {
        return new SendMessageService(sender, contextService, localizer);
    }

    @Provides
    @Singleton
    public IMessageSender messageSender() {
        return new MessageSender();
    }

    @Provides
    @Singleton
    public TelegramBot botExecutor(IProcessorFactory factory, IContextService contextService,
                                   IDepartmentService departmentService, ILocalizer localizer) {
        return new TelegramBot(factory, contextService, departmentService, localizer);
    }

    @Provides
    @Singleton
    IAppointmentService appointmentService(IAppointmentDao appointmentDao) {
        return new AppointmentService(appointmentDao);
    }

    @Provides
    @Singleton
    IContextService contextService(IContextDao contextDao) {
        return new ContextService(contextDao);
    }

    @Provides
    @Singleton
    IDepartmentService departmentService(IDepartmentDao departmentDao) {
        return new DepartmentService(departmentDao);
    }

    @Provides
    @Singleton
    ILocalizer localizer() {
        return new Localizer();
    }

}
