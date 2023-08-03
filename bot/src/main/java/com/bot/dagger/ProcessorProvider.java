package com.bot.dagger;

import com.bot.model.CommandType;
import com.bot.processor.IProcessor;
import com.bot.processor.IProcessorFactory;
import com.bot.processor.impl.ProcessorFactory;
import com.bot.processor.impl.general.admin.appointment.AllAppointmentsFirstStepProcessor;
import com.bot.processor.impl.general.admin.appointment.AllAppointmentsSecondStepProcessor;
import com.bot.processor.impl.general.admin.appointment.AppointmentsTodayAndTomorrowProcessor;
import com.bot.processor.impl.general.admin.appointment.StartAppointmentsDashProcessor;
import com.bot.processor.impl.general.admin.dayoff.DayOffStartProcessor;
import com.bot.processor.impl.general.admin.dayoff.cancel.CancelDayOffFirstStepProcessor;
import com.bot.processor.impl.general.admin.dayoff.cancel.CancelDayOffFourthStepProcessor;
import com.bot.processor.impl.general.admin.dayoff.cancel.CancelDayOffSecondStepProcessor;
import com.bot.processor.impl.general.admin.dayoff.cancel.CancelDayOffThirdStepProcessor;
import com.bot.processor.impl.general.admin.dayoff.create.CreateDayOffFifthStepProcessor;
import com.bot.processor.impl.general.admin.dayoff.create.CreateDayOffFirstStepProcessor;
import com.bot.processor.impl.general.admin.dayoff.create.CreateDayOffFourthStepProcessor;
import com.bot.processor.impl.general.admin.dayoff.create.CreateDayOffSecondStepProcessor;
import com.bot.processor.impl.general.admin.dayoff.create.CreateDayOffThirdStepProcessor;
import com.bot.processor.impl.general.admin.dayoff.view.ViewDayOffFirstStepProcessor;
import com.bot.processor.impl.general.admin.dayoff.view.ViewDayOffSecondStepProcessor;
import com.bot.processor.impl.general.admin.dayoff.view.ViewDayOffThirdStepProcessor;
import com.bot.processor.impl.general.user.appointment.cancel.CancelAppointmentFirstStepProcessor;
import com.bot.processor.impl.general.user.appointment.cancel.CancelAppointmentFourthStepProcessor;
import com.bot.processor.impl.general.user.appointment.cancel.CancelAppointmentSecondStepProcessor;
import com.bot.processor.impl.general.user.appointment.cancel.CancelAppointmentThirdStepProcessor;
import com.bot.processor.impl.general.user.appointment.create.CreateAppointmentFifthStepProcessor;
import com.bot.processor.impl.general.user.appointment.create.CreateAppointmentFirstStepProcessor;
import com.bot.processor.impl.general.user.appointment.create.CreateAppointmentFourthStepProcessor;
import com.bot.processor.impl.general.user.appointment.create.CreateAppointmentSecondStepProcessor;
import com.bot.processor.impl.general.user.appointment.create.CreateAppointmentThirdStepProcessor;
import com.bot.processor.impl.general.user.appointment.view.ViewAppointmentsFirstStepProcessor;
import com.bot.processor.impl.general.user.appointment.view.ViewAppointmentsSecondStepProcessor;
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
        return new ViewAppointmentsFirstStepProcessor(appointmentService);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.MY_APP_2)
    public IProcessor myApps2(IAppointmentService appointmentService) {
        return new ViewAppointmentsSecondStepProcessor(appointmentService);
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
    @CommandKey(CommandType.START_APP_DASH)
    public IProcessor startAppointmentsDash(IAppointmentService appointmentService) {
        return new StartAppointmentsDashProcessor();
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.ALL_APP_BY_DATE_1)
    public IProcessor getAppointmentsByDateFirst(IAppointmentService appointmentService) {
        return new AllAppointmentsFirstStepProcessor(appointmentService);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.ALL_APP_BY_DATE_2)
    public IProcessor getAppointmentsByDateSecond(IAppointmentService appointmentService, IContextService contextService) {
        return new AllAppointmentsSecondStepProcessor(appointmentService, contextService);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.ALL_APP_TODAY_TOMORROW)
    public IProcessor getAppointmentsToday(IAppointmentService appointmentService, IContextService contextService) {
        return new AppointmentsTodayAndTomorrowProcessor(appointmentService, contextService);
    }

    // --------------------Day off -----------------------
    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.DAY_OFF_START)
    public IProcessor dayOffStartStep() {
        return new DayOffStartProcessor();
    }

    // --------------------Day off Create ------------------------

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.DAY_OFF_CREATE1)
    public IProcessor dayOffCreateFirstStep(@Named("createDayOffSecond") IProcessor nextStepProcessor) {
        return new CreateDayOffFirstStepProcessor(nextStepProcessor);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.DAY_OFF_CREATE2)
    public IProcessor dayOffCreateSecondStep(IAppointmentService appointmentService) {
        return new CreateDayOffSecondStepProcessor(appointmentService);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.DAY_OFF_CREATE3)
    public IProcessor dayOffCreateThirdStep(IAppointmentService appointmentService) {
        return new CreateDayOffThirdStepProcessor(appointmentService);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.DAY_OFF_CREATE4)
    public IProcessor dayOffCreateFourthStep(IAppointmentService appointmentService, IContextService contextService) {
        return new CreateDayOffFourthStepProcessor(appointmentService, contextService);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.DAY_OFF_CREATE5)
    public IProcessor dayOffCreateFifthStep(IAppointmentService appointmentService, IContextService contextService) {
        return new CreateDayOffFifthStepProcessor(appointmentService, contextService);
    }

    // --------------------Day off view ---------------------------

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.DAY_OFF_VIEW1)
    public IProcessor dayOffViewFirstStep(@Named("viewDayOffSecond") IProcessor nextStepProcessor) {
        return new ViewDayOffFirstStepProcessor(nextStepProcessor);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.DAY_OFF_VIEW2)
    public IProcessor viewDayOffSecondStep(IAppointmentService appointmentService) {
        return new ViewDayOffSecondStepProcessor(appointmentService);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.DAY_OFF_VIEW3)
    public IProcessor viewDayOffThirdStep(IAppointmentService appointmentService,
                                          @Named("viewDayOffSecond") IProcessor previousStepProcessor) {
        return new ViewDayOffThirdStepProcessor(appointmentService, previousStepProcessor);
    }

    // --------------------Day off cancel -------------------------

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.DAY_OFF_CANCEL1)
    public IProcessor dayOffCancelFirstStep(@Named("cancelDayOffSecond") IProcessor nextStepProcessor) {
        return new CancelDayOffFirstStepProcessor(nextStepProcessor);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.DAY_OFF_CANCEL2)
    public IProcessor dayOffCancelSecondStep(IAppointmentService appointmentService) {
        return new CancelDayOffSecondStepProcessor(appointmentService);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.DAY_OFF_CANCEL3)
    public IProcessor dayOffCancelThirdStep(IAppointmentService appointmentService,
                                            @Named("cancelDayOffSecond") IProcessor previousProcessor) {
        return new CancelDayOffThirdStepProcessor(appointmentService, previousProcessor);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.DAY_OFF_CANCEL4)
    public IProcessor dayOffCancelFourthStep(IAppointmentService appointmentService) {
        return new CancelDayOffFourthStepProcessor(appointmentService);
    }

    // --------------------Next step processors -------------------

    @Provides
    @Singleton
    @Named("cancelDayOffSecond")
    public IProcessor cancelDayOffSecond(IAppointmentService appointmentService) {
        return new CancelDayOffSecondStepProcessor(appointmentService);
    }

    @Provides
    @Singleton
    @Named("viewDayOffSecond")
    public IProcessor viewDayOffSecond(IAppointmentService appointmentService) {
        return new ViewDayOffSecondStepProcessor(appointmentService);
    }

    @Provides
    @Singleton
    @Named("createDayOffSecond")
    public IProcessor crDayOffSecond(IAppointmentService appointmentService) {
        return new CreateDayOffSecondStepProcessor(appointmentService);
    }

    @Provides
    @Singleton
    public IProcessorFactory factory(Map<CommandType, IProcessor> processors,
                                     IContextService contextService) {
        return new ProcessorFactory(processors, contextService);
    }
}
