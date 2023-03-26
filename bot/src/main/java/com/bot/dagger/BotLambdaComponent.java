package com.bot.dagger;

import com.bot.lambda.BotLambda;
import dagger.Component;

import javax.inject.Singleton;

@Component
@Singleton
public interface BotLambdaComponent {

    void inject(BotLambda lambda);
}
