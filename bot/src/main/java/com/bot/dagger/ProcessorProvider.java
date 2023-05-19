package com.bot.dagger;

import com.bot.model.CommandType;
import com.bot.processor.IProcessor;
import com.bot.processor.IProcessorFactory;
import com.bot.processor.impl.ProcessorFactory;
import com.bot.processor.impl.appointment.create.CreateAppointmentFifthStepProcessor;
import com.bot.processor.impl.appointment.create.CreateAppointmentSecondStepProcessor;
import com.bot.processor.impl.appointment.create.CreateAppointmentFirstStepProcessor;
import com.bot.processor.impl.appointment.create.CreateAppointmentFourthStepProcessor;
import com.bot.processor.impl.appointment.create.CreateAppointmentThirdStepProcessor;
import com.bot.processor.impl.appointment.my.MyAppointmentsProcessor;
import com.bot.processor.impl.start.AskLanguageProcessor;
import com.bot.processor.impl.start.SetContactStartDashboard;
import com.bot.processor.impl.start.SetLanguageAskContactsProcessor;
import com.bot.service.IAppointmentService;
import com.bot.service.IContextService;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Map;

@Module
public class ProcessorProvider {

    //----------------Appointment-----------------------
    //             ----------------My-----------------------
    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.MY_APPOINTMENTS)
    public IProcessor myApps(IContextService contextService) {
        return new MyAppointmentsProcessor();
    }

    //             ----------------Create-----------------------
    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.CREATE_APP_1)
    public IProcessor createAppFirst(IAppointmentService appointmentService, IContextService contextService) {
        return new CreateAppointmentFirstStepProcessor(appointmentService, contextService);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.CREATE_APP_2)
    public IProcessor createAppSecond(IAppointmentService appointmentService, IContextService contextService,
                                                      @Named("createAppThird") IProcessor processor) {
        return new CreateAppointmentSecondStepProcessor(appointmentService, contextService, processor);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.CREATE_APP_3)
    public IProcessor createAppThird(IContextService contextService, @Named("createAppFourth") IProcessor processor) {
        return new CreateAppointmentThirdStepProcessor(contextService, processor);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.CREATE_APP_4)
    public IProcessor createAppFourth(IContextService contextService) {
        return new CreateAppointmentFourthStepProcessor(contextService);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.CREATE_APP_5)
    public IProcessor createAppFifth(IContextService contextService, IAppointmentService appointmentService) {
        return new CreateAppointmentFifthStepProcessor(contextService, appointmentService);
    }

    //next step processor beans

    @Provides
    @Singleton
    @Named("createAppThird")
    public IProcessor crAppThird(IContextService contextService, @Named("createAppFourth") IProcessor processor) {
        return new CreateAppointmentThirdStepProcessor(contextService, processor);
    }

    @Provides
    @Singleton
    @Named("createAppFourth")
    public IProcessor crAppFourth(IContextService contextService) {
        return new CreateAppointmentFourthStepProcessor(contextService);
    }

    //----------------Start-----------------------
    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.SET_CONT_START_DASH)
    public IProcessor setContStartDash(IContextService contextService) {
        return new SetContactStartDashboard(contextService);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.SET_LANG_ASK_CONT)
    public IProcessor setLangAskCont(IContextService contextService) {
        return new SetLanguageAskContactsProcessor(contextService);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.ASK_LANGUAGE)
    public IProcessor askLanguage(IContextService contextService) {
        return new AskLanguageProcessor(contextService);
    }

    @Provides
    @Singleton
    public IProcessorFactory factory(Map<CommandType, IProcessor> processors,
                                     IContextService contextService) {
        return new ProcessorFactory(processors, contextService);
    }
}
