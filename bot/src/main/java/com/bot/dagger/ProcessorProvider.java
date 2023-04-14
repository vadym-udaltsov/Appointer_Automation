package com.bot.dagger;

import com.bot.model.CommandType;
import com.bot.processor.IProcessor;
import com.bot.processor.IProcessorFactory;
import com.bot.processor.impl.start.AskLanguageProcessor;
import com.bot.processor.impl.appointment.my.MyAppointmentsProcessor;
import com.bot.processor.impl.ProcessorFactory;
import com.bot.processor.impl.start.SetContactStartDashboard;
import com.bot.processor.impl.start.SetLanguageAskContactsProcessor;
import com.bot.processor.impl.appointment.create.StartCreateAppointment;
import com.bot.service.IContextService;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;

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
    @CommandKey(CommandType.START_CREATE_APP)
    public IProcessor startCreateApp(IContextService contextService) {
        return new StartCreateAppointment();
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
