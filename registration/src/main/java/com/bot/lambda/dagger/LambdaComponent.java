package com.bot.lambda.dagger;

import com.bot.lambda.RegistrationLambda;
import dagger.Component;

import javax.inject.Singleton;

@Component(modules = ServiceProvider.class)
@Singleton
public interface LambdaComponent {

    void inject(RegistrationLambda lambda);
}
