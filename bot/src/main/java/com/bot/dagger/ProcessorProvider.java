package com.bot.dagger;

import com.bot.model.CommandType;
import com.bot.processor.IProcessor;
import com.bot.processor.IProcessorFactory;
import com.bot.processor.impl.ProcessorFactory;
import com.bot.processor.impl.general.admin.appointments.AllAppointmentsFirstStepProcessor;
import com.bot.processor.impl.general.admin.appointments.AllAppointmentsSecondStepProcessor;
import com.bot.processor.impl.general.user.appointment.cancel.CancelAppointmentFirstStepProcessor;
import com.bot.processor.impl.general.user.appointment.cancel.CancelAppointmentFourthStepProcessor;
import com.bot.processor.impl.general.user.appointment.cancel.CancelAppointmentSecondStepProcessor;
import com.bot.processor.impl.general.user.appointment.cancel.CancelAppointmentThirdStepProcessor;
import com.bot.processor.impl.general.user.appointment.create.CreateAppointmentFifthStepProcessor;
import com.bot.processor.impl.general.user.appointment.create.CreateAppointmentFirstStepProcessor;
import com.bot.processor.impl.general.user.appointment.create.CreateAppointmentFourthStepProcessor;
import com.bot.processor.impl.general.user.appointment.create.CreateAppointmentSecondStepProcessor;
import com.bot.processor.impl.general.user.appointment.create.CreateAppointmentThirdStepProcessor;
import com.bot.processor.impl.general.user.appointment.my.MyAppointmentsFirstStepProcessor;
import com.bot.processor.impl.general.user.appointment.my.MyAppointmentsSecondStepProcessor;
import com.bot.processor.impl.start.AskLanguageProcessor;
import com.bot.processor.impl.start.SetContactProcessor;
import com.bot.processor.impl.start.SetLanguageAskContactsProcessor;
import com.bot.processor.impl.start.StartDashProcessor;
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
    //             ----------------Cancel-------------------

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.CANCEL_APP1)
    public IProcessor cancelApp1(IAppointmentService appointmentService) {
        return new CancelAppointmentFirstStepProcessor(appointmentService);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.CANCEL_APP2)
    public IProcessor cancelApp2(IAppointmentService appointmentService) {
        return new CancelAppointmentSecondStepProcessor(appointmentService);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.CANCEL_APP3)
    public IProcessor cancelApp3() {
        return new CancelAppointmentThirdStepProcessor();
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.CANCEL_APP4)
    public IProcessor cancelApp4(IAppointmentService appointmentService, ISendMessageService sendMessageService) {
        return new CancelAppointmentFourthStepProcessor(appointmentService, sendMessageService);
    }

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
    public IProcessor createAppThird(IAppointmentService appointmentService, @Named("createAppFourth") IProcessor processor,
                                     @Named("createAppSecond") IProcessor prevProcessor) {
        return new CreateAppointmentThirdStepProcessor(appointmentService, processor, prevProcessor);
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
    @CommandKey(CommandType.SET_CONT)
    public IProcessor setCont(IContextService contextService) {
        return new SetContactProcessor(contextService);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.START_DASH)
    public IProcessor startDash(IContextService contextService) {
        return new StartDashProcessor();
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

    // ------------------- admin --------------------------
    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.GET_ALL_APPOINTMENTS_1)
    public IProcessor getAllAppointmentsFirst(IAppointmentService appointmentService) {
        return new AllAppointmentsFirstStepProcessor(appointmentService);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.GET_ALL_APPOINTMENTS_2)
    public IProcessor getAllAppointmentsSecond(IAppointmentService appointmentService) {
        return new AllAppointmentsSecondStepProcessor(appointmentService);
    }

    @Provides
    @Singleton
    public IProcessorFactory factory(Map<CommandType, IProcessor> processors,
                                     IContextService contextService) {
        return new ProcessorFactory(processors, contextService);
    }
}
