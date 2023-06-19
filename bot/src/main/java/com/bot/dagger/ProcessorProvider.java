package com.bot.dagger;

import com.bot.model.CommandType;
import com.bot.processor.IProcessor;
import com.bot.processor.IProcessorFactory;
import com.bot.processor.impl.ProcessorFactory;
import com.bot.processor.impl.appointment.create.CreateAppointmentFifthStepProcessor;
import com.bot.processor.impl.appointment.create.CreateAppointmentFirstStepProcessor;
import com.bot.processor.impl.appointment.create.CreateAppointmentFourthStepProcessor;
import com.bot.processor.impl.appointment.create.CreateAppointmentSecondStepProcessor;
import com.bot.processor.impl.appointment.create.CreateAppointmentThirdStepProcessor;
import com.bot.processor.impl.appointment.my.MyAppointmentsFirstStepProcessor;
import com.bot.processor.impl.appointment.my.MyAppointmentsSecondStepProcessor;
import com.bot.processor.impl.start.AskLanguageProcessor;
import com.bot.processor.impl.start.SetContactStartDashboard;
import com.bot.processor.impl.start.SetLanguageAskContactsProcessor;
import com.bot.service.IAppointmentService;
import com.bot.service.IContextService;
import com.bot.service.ISendMessageService;
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
    @CommandKey(CommandType.MY_APP_1)
    public IProcessor myApps(IAppointmentService appointmentService) {
        return new MyAppointmentsFirstStepProcessor(appointmentService);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.MY_APP_2)
    public IProcessor myApps2(IAppointmentService appointmentService) {
        return new MyAppointmentsSecondStepProcessor(appointmentService);
    }

    //             ----------------Create-----------------------
    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.CREATE_APP_1)
    public IProcessor createAppFirst(@Named("createAppSecond") IProcessor processor) {
        return new CreateAppointmentFirstStepProcessor(processor);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.CREATE_APP_2)
    public IProcessor createAppSecond(IAppointmentService appointmentService) {
        return new CreateAppointmentSecondStepProcessor(appointmentService);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.CREATE_APP_3)
    public IProcessor createAppThird(IAppointmentService appointmentService, @Named("createAppFourth") IProcessor processor) {
        return new CreateAppointmentThirdStepProcessor(appointmentService, processor);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.CREATE_APP_4)
    public IProcessor createAppFourth() {
        return new CreateAppointmentFourthStepProcessor();
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.CREATE_APP_5)
    public IProcessor createAppFifth(IContextService contextService, IAppointmentService appointmentService,
                                     ISendMessageService sendMessageService) {
        return new CreateAppointmentFifthStepProcessor(contextService, appointmentService, sendMessageService);
    }

    //next step processor beans

    @Provides
    @Singleton
    @Named("createAppSecond")
    public IProcessor crAppSecond(IAppointmentService appointmentService) {
        return new CreateAppointmentSecondStepProcessor(appointmentService);
    }

    @Provides
    @Singleton
    @Named("createAppFourth")
    public IProcessor crAppFourth() {
        return new CreateAppointmentFourthStepProcessor();
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
