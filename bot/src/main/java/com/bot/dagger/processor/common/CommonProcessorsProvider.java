package com.bot.dagger.processor.common;

import com.bot.dagger.CommandKey;
import com.bot.model.CommandType;
import com.bot.processor.IProcessor;
import com.bot.processor.impl.common.ChangeLanguageFirstStepProcessor;
import com.bot.processor.impl.common.ChangeLanguageSecondStepProcessor;
import com.bot.processor.impl.general.admin.AdminProfileProcessor;
import com.bot.processor.impl.general.admin.location.LocationDashProcessor;
import com.bot.processor.impl.general.admin.location.create.CreateLocationFirstStepProcessor;
import com.bot.processor.impl.general.admin.location.create.CreateLocationSecondStepProcessor;
import com.bot.processor.impl.general.admin.location.delete.DeleteLocationFirstStepProcessor;
import com.bot.processor.impl.general.admin.location.delete.DeleteLocationSecondStepProcessor;
import com.bot.processor.impl.general.admin.location.view.ViewLocationFirstStepProcessor;
import com.bot.service.IDictionaryService;
import com.bot.service.ISendMessageService;
import com.commons.service.IDepartmentService;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;

import javax.inject.Singleton;

@Module
public class CommonProcessorsProvider {

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.ADMIN_PROFILE)
    public IProcessor profile() {
        return new AdminProfileProcessor();
    }

    // -------------------------------Location ---------------------------

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.LOCATION_DASH)
    public IProcessor locationDash() {
        return new LocationDashProcessor();
    }

    // ---------------------------Location create -------------------------

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.LOCATION_CREATE1)
    public IProcessor locationCreate1() {
        return new CreateLocationFirstStepProcessor();
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.LOCATION_CREATE2)
    public IProcessor locationCreate2(IDepartmentService departmentService) {
        return new CreateLocationSecondStepProcessor(departmentService);
    }

    // ----------------------------Location delete -----------------------

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.LOCATION_DELETE1)
    public IProcessor locationDelete1() {
        return new DeleteLocationFirstStepProcessor();
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.LOCATION_DELETE2)
    public IProcessor locationDelete2(IDepartmentService departmentService) {
        return new DeleteLocationSecondStepProcessor(departmentService);
    }

    // ----------------------------Location view ------------------------

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.LOCATION_VIEW1)
    public IProcessor locationView1(ISendMessageService sendMessageService) {
        return new ViewLocationFirstStepProcessor(sendMessageService);
    }

    // ----------------------------Change language------------------------

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.CHANGE_LANG1)
    public IProcessor changeLang1(IDictionaryService dictionaryService) {
        return new ChangeLanguageFirstStepProcessor(dictionaryService);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.CHANGE_LANG2)
    public IProcessor changeLang2(IDictionaryService dictionaryService) {
        return new ChangeLanguageSecondStepProcessor(dictionaryService);
    }
}
