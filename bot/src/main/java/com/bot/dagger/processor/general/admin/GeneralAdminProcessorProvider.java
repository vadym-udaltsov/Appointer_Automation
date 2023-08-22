package com.bot.dagger.processor.general.admin;

import com.bot.dagger.CommandKey;
import com.bot.model.CommandType;
import com.bot.processor.IProcessor;
import com.bot.processor.impl.general.admin.appointment.StartAdminAppointmentDashProcessor;
import com.bot.processor.impl.general.admin.appointment.AppointmentsByDateFirstStepProcessor;
import com.bot.processor.impl.general.admin.appointment.AppointmentsByDateSecondStepProcessor;
import com.bot.processor.impl.general.admin.appointment.AppointmentsTodayAndTomorrowProcessor;
import com.bot.processor.impl.general.admin.appointment.ViewAppointmentsDashProcessor;
import com.bot.processor.impl.general.admin.block.BlockUserFirstStepProcessor;
import com.bot.processor.impl.general.admin.block.BlockUserSecondStepProcessor;
import com.bot.processor.impl.general.admin.block.BlockUserThirdStepProcessor;
import com.bot.processor.impl.general.admin.cancelappointment.CancelPhoneAppointmentFirstStepProcessor;
import com.bot.processor.impl.general.admin.cancelappointment.CancelPhoneAppointmentFourthStepProcessor;
import com.bot.processor.impl.general.admin.cancelappointment.CancelPhoneAppointmentSecondStepProcessor;
import com.bot.processor.impl.general.admin.cancelappointment.CancelPhoneAppointmentThirdStepProcessor;
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
import com.bot.service.IContextService;
import com.bot.service.ISendMessageService;
import com.commons.service.IAppointmentService;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;

import javax.inject.Named;
import javax.inject.Singleton;

@Module
public class GeneralAdminProcessorProvider {

    //----------------------------------------Appointments -------------------------

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.ADMIN_APP_DASH)
    public IProcessor adminAppDash() {
        return new StartAdminAppointmentDashProcessor();
    }

    //---------------------------------------- Cancel appointment ------------------

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.CANCEL_PHONE_APP1)
    public IProcessor cancelPhoneApp1(IAppointmentService appointmentService) {
        return new CancelPhoneAppointmentFirstStepProcessor(appointmentService);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.CANCEL_PHONE_APP2)
    public IProcessor cancelPhoneApp2(IAppointmentService appointmentService, IContextService contextService) {
        return new CancelPhoneAppointmentSecondStepProcessor(appointmentService, contextService);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.CANCEL_PHONE_APP3)
    public IProcessor cancelPhoneApp3() {
        return new CancelPhoneAppointmentThirdStepProcessor();
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.CANCEL_PHONE_APP4)
    public IProcessor cancelPhoneApp4(IAppointmentService appointmentService, ISendMessageService sendMessageService,
                                      IContextService contextService) {
        return new CancelPhoneAppointmentFourthStepProcessor(appointmentService, sendMessageService, contextService);
    }

    //---------------------------------------- Block -------------------------------

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.BLOCK_USER1)
    public IProcessor blockUser1(IContextService contextService) {
        return new BlockUserFirstStepProcessor(contextService);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.BLOCK_USER2)
    public IProcessor blockUser2() {
        return new BlockUserSecondStepProcessor();
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.BLOCK_USER3)
    public IProcessor blockUser3(IAppointmentService appointmentService, IContextService contextService) {
        return new BlockUserThirdStepProcessor(appointmentService, contextService);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.START_APP_DASH)
    public IProcessor startAppointmentsDash(IAppointmentService appointmentService) {
        return new ViewAppointmentsDashProcessor();
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.ALL_APP_BY_DATE_1)
    public IProcessor getAppointmentsByDateFirst(IAppointmentService appointmentService) {
        return new AppointmentsByDateFirstStepProcessor(appointmentService);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.ALL_APP_BY_DATE_2)
    public IProcessor getAppointmentsByDateSecond(IAppointmentService appointmentService, IContextService contextService) {
        return new AppointmentsByDateSecondStepProcessor(appointmentService, contextService);
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

}
