package com.bot.dagger;

import com.bot.dagger.processor.common.CommonProcessorsProvider;
import com.bot.dagger.processor.general.admin.GeneralAdminProcessorProvider;
import com.bot.dagger.processor.general.user.GeneralUserProcessorProvider;
import com.bot.lambda.BotLambda;
import dagger.Component;

import javax.inject.Singleton;

@Component(modules = {ServiceProvider.class,
        GeneralAdminProcessorProvider.class,
        GeneralUserProcessorProvider.class,
        CommonProcessorsProvider.class
})
@Singleton
public interface BotLambdaComponent {

    void inject(BotLambda lambda);
}
