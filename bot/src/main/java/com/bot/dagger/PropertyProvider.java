package com.bot.dagger;

import com.bot.model.S3Property;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class PropertyProvider {

    @Provides
    @Singleton
    S3Property s3Property() {
        String accountId = System.getenv("ACCOUNT");
        return new S3Property("appointer-localization-" + accountId);
    }

}
