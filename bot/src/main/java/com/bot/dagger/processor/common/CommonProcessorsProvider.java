package com.bot.dagger.processor.common;

import com.bot.dagger.CommandKey;
import com.bot.model.CommandType;
import com.bot.processor.IProcessor;
import com.bot.processor.impl.common.ChangeLanguageFirstStepProcessor;
import com.bot.processor.impl.common.ChangeLanguageSecondStepProcessor;
import com.bot.processor.impl.common.ProfileProcessor;
import com.bot.service.IDictionaryService;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;

import javax.inject.Singleton;

@Module
public class CommonProcessorsProvider {

    @Provides
    @Singleton
    @IntoMap
    @CommandKey(CommandType.PROFILE)
    public IProcessor profile() {
        return new ProfileProcessor();
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
