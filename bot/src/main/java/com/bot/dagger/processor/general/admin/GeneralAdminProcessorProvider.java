package com.bot.dagger.processor.general.admin;

import com.bot.dagger.CommandKey;
import com.bot.model.CommandType;
import com.bot.processor.IProcessor;
import com.bot.processor.impl.general.admin.appointment.AppointmentsByDateFirstStepProcessor;
import com.bot.processor.impl.general.admin.appointment.AppointmentsByDateSecondStepProcessor;
import com.bot.processor.impl.general.admin.appointment.AppointmentsTodayAndTomorrowProcessor;
import com.bot.processor.impl.general.admin.appointment.StartAdminAppointmentDashProcessor;
import com.bot.processor.impl.general.admin.appointment.ViewAppointmentsDashProcessor;
import com.bot.processor.impl.general.admin.block.BlockUserDashProcessor;
import com.bot.processor.impl.general.admin.block.BlockUserFirstStepProcessor;
import com.bot.processor.impl.general.admin.block.BlockUserSecondStepProcessor;
import com.bot.processor.impl.general.admin.block.BlockUserThirdStepProcessor;
import com.bot.processor.impl.general.admin.block.UnBlockUserThirdStepProcessor;
import com.bot.processor.impl.general.admin.block.UnblockUserFirstStepProcessor;
import com.bot.processor.impl.general.admin.block.UnblockUserSecondStepProcessor;
import com.bot.processor.impl.general.admin.cancelappointment.CancelPhoneAppointmentFirstStepProcessor;
import com.bot.processor.impl.general.admin.cancelappointment.CancelPhoneAppointmentFourthStepProcessor;
import com.bot.processor.impl.general.admin.cancelappointment.CancelPhoneAppointmentSecondStepProcessor;
import com.bot.processor.impl.general.admin.cancelappointment.CancelPhoneAppointmentThirdStepProcessor;
import com.bot.processor.impl.general.admin.dayoff.DayOffDashProcessor;
import com.bot.processor.impl.general.admin.dayoff.DayOffStartProcessor;
import com.bot.processor.impl.general.admin.dayoff.PeriodDayOffCommonFirstStepProcessor;
import com.bot.processor.impl.general.admin.dayoff.PeriodDayOffCommonSecondStepProcessor;
import com.bot.processor.impl.general.admin.dayoff.PeriodDayOffCommonThirdStepProcessor;
import com.bot.processor.impl.general.admin.dayoff.cancel.daily.CancelDayOffFirstStepProcessor;
import com.bot.processor.impl.general.admin.dayoff.cancel.daily.CancelDayOffFourthStepProcessor;
import com.bot.processor.impl.general.admin.dayoff.cancel.daily.CancelDayOffSecondStepProcessor;
import com.bot.processor.impl.general.admin.dayoff.cancel.daily.CancelDayOffThirdStepProcessor;
import com.bot.processor.impl.general.admin.dayoff.cancel.period.DeletePeriodDayOffFourthStepProcessor;
import com.bot.processor.impl.general.admin.dayoff.create.daily.CreateDayOffFifthStepProcessor;
import com.bot.processor.impl.general.admin.dayoff.create.daily.CreateDayOffFirstStepProcessor;
import com.bot.processor.impl.general.admin.dayoff.create.daily.CreateDayOffFourthStepProcessor;
import com.bot.processor.impl.general.admin.dayoff.create.daily.CreateDayOffSecondStepProcessor;
import com.bot.processor.impl.general.admin.dayoff.create.daily.CreateDayOffThirdStepProcessor;
import com.bot.processor.impl.general.admin.dayoff.create.period.CreatePeriodDayOffFifthStepProcessor;
import com.bot.processor.impl.general.admin.dayoff.create.period.CreatePeriodDayOffFourthStepProcessor;
import com.bot.processor.impl.general.admin.dayoff.view.daily.ViewDayOffFirstStepProcessor;
import com.bot.processor.impl.general.admin.dayoff.view.daily.ViewDayOffSecondStepProcessor;
import com.bot.processor.impl.general.admin.dayoff.view.daily.ViewDayOffThirdStepProcessor;
import com.bot.processor.impl.general.admin.dayoff.view.period.ViewPeriodDayOffFourthStepProcessor;
import com.bot.processor.impl.general.admin.messaging.SendPhotoMessageFirstStepProcessor;
import com.bot.processor.impl.general.admin.messaging.SendPhotoMessageSecondStepProcessor;
import com.bot.processor.impl.general.admin.messaging.SendPhotoMessageThirdStepProcessor;
import com.bot.processor.impl.general.admin.messaging.SendTextMessageFirstStepProcessor;
import com.bot.processor.impl.general.admin.messaging.SendTextMessageSecondStepProcessor;
import com.bot.processor.impl.general.admin.messaging.SendTextMessageThirdStepProcessor;
import com.bot.processor.impl.general.admin.messaging.ViewSendMessageDashProcessor;
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
    @CommandKey(CommandType.BLOCK_USER_DASH)
    public IProcessor blockUserDash() {
        return new BlockUserDashProcessor();
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.UNBLOCK_USER1)
    public IProcessor unblockUser1(IContextService contextService) {
        return new UnblockUserFirstStepProcessor(contextService);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.UNBLOCK_USER2)
    public IProcessor unblockUser2() {
        return new UnblockUserSecondStepProcessor();
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.UNBLOCK_USER3)
    public IProcessor unblockUser3(IContextService contextService) {
        return new UnBlockUserThirdStepProcessor(contextService);
    }

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

    //---------------------------------------- Appointments -------------------------------

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

    //---------------------------------------- Messaging -------------------------------

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.SEND_MESSAGE_DASH)
    public IProcessor startSendMessageDash() {
        return new ViewSendMessageDashProcessor();
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.SEND_MESSAGE_TEXT_1)
    public IProcessor getSendMessageTextFirst() {
        return new SendTextMessageFirstStepProcessor();
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.SEND_MESSAGE_TEXT_2)
    public IProcessor getSendMessageTextSecond() {
        return new SendTextMessageSecondStepProcessor();
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.SEND_MESSAGE_TEXT_3)
    public IProcessor getSendMessageTextThird(IContextService contextService, ISendMessageService sendMessageService) {
        return new SendTextMessageThirdStepProcessor(contextService, sendMessageService);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.SEND_MESSAGE_PHOTO_1)
    public IProcessor getSendMessagePhotoFirst() {
        return new SendPhotoMessageFirstStepProcessor();
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.SEND_MESSAGE_PHOTO_2)
    public IProcessor getSendMessagePhotoSecond() {
        return new SendPhotoMessageSecondStepProcessor();
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.SEND_MESSAGE_PHOTO_3)
    public IProcessor getSendMessagePhotoThird(IContextService contextService, ISendMessageService sendMessageService) {
        return new SendPhotoMessageThirdStepProcessor(contextService, sendMessageService);
    }

    // --------------------Day off start-----------------------
    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.DAY_OFF_START)
    public IProcessor dayOffStartStep() {
        return new DayOffStartProcessor();
    }

    // --------------------Day off Dash ------------------------

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.DAILY_DAY_OFF_START)
    public IProcessor dailyDayOffStartStep() {
        return new DayOffDashProcessor();
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.PERIOD_DAY_OFF_START)
    public IProcessor periodDayOffStartStep() {
        return new DayOffDashProcessor();
    }

    // --------------------Period Day off Delete ------------------------

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.PERIOD_DAY_OFF_DELETE1)
    public IProcessor periodDayOffDeleteFirstStep(@Named("periodDayOffSecond") IProcessor nextStepProcessor) {
        return new PeriodDayOffCommonFirstStepProcessor(nextStepProcessor);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.PERIOD_DAY_OFF_DELETE2)
    public IProcessor periodDayOffDeleteSecondStep(IAppointmentService appointmentService) {
        return new PeriodDayOffCommonSecondStepProcessor(appointmentService);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.PERIOD_DAY_OFF_DELETE3)
    public IProcessor periodDayOffDeleteThirdStep(IAppointmentService appointmentService) {
        return new PeriodDayOffCommonThirdStepProcessor(appointmentService);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.PERIOD_DAY_OFF_DELETE4)
    public IProcessor periodDayOffDeleteFourthStep(IAppointmentService appointmentService) {
        return new DeletePeriodDayOffFourthStepProcessor(appointmentService);
    }

    // --------------------Period Day off Create ------------------------

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.PERIOD_DAY_OFF_CREATE1)
    public IProcessor periodDayOffCreateFirstStep(@Named("periodDayOffSecond") IProcessor nextStepProcessor) {
        return new PeriodDayOffCommonFirstStepProcessor(nextStepProcessor);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.PERIOD_DAY_OFF_CREATE2)
    public IProcessor periodDayOffCreateSecondStep(IAppointmentService appointmentService) {
        return new PeriodDayOffCommonSecondStepProcessor(appointmentService);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.PERIOD_DAY_OFF_CREATE3)
    public IProcessor periodDayOffCreateThirdStep(IAppointmentService appointmentService) {
        return new PeriodDayOffCommonThirdStepProcessor(appointmentService);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.PERIOD_DAY_OFF_CREATE4)
    public IProcessor periodDayOffCreateFourthStep(IAppointmentService appointmentService,
                                                   IContextService contextService,
                                                   ISendMessageService sendMessageService) {
        return new CreatePeriodDayOffFourthStepProcessor(appointmentService, contextService, sendMessageService);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.PERIOD_DAY_OFF_CREATE5)
    public IProcessor periodDayOffCreateFifthStep(ISendMessageService sendMessageService) {
        return new CreatePeriodDayOffFifthStepProcessor(sendMessageService);
    }

    // --------------------Period Day off View ------------------------

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.PERIOD_DAY_OFF_VIEW1)
    public IProcessor periodDayOffViewFirstStep(@Named("periodDayOffSecond") IProcessor nextStepProcessor) {
        return new PeriodDayOffCommonFirstStepProcessor(nextStepProcessor);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.PERIOD_DAY_OFF_VIEW2)
    public IProcessor periodDayOffViewSecondStep(IAppointmentService appointmentService) {
        return new PeriodDayOffCommonSecondStepProcessor(appointmentService);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.PERIOD_DAY_OFF_VIEW3)
    public IProcessor periodDayOffViewThirdStep(IAppointmentService appointmentService) {
        return new PeriodDayOffCommonThirdStepProcessor(appointmentService);
    }

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.PERIOD_DAY_OFF_VIEW4)
    public IProcessor periodDayOffViewFourthStep(IAppointmentService appointmentService) {
        return new ViewPeriodDayOffFourthStepProcessor(appointmentService);
    }

    // --------------------Daily Day off Create ------------------------

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

    // --------------------Daily Day off view ---------------------------

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

    // --------------------Daily Day off cancel -------------------------

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
    @Named("periodDayOffSecond")
    public IProcessor periodDayOffSecond(IAppointmentService appointmentService) {
        return new PeriodDayOffCommonSecondStepProcessor(appointmentService);
    }
}
