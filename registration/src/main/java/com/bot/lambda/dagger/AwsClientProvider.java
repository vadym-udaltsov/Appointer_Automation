package com.bot.lambda.dagger;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.commons.dao.impl.DynamoDbFactory;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class AwsClientProvider {

    @Provides
    @Singleton
    public DynamoDbFactory dynamoDBMapper() {
        AmazonDynamoDB dynamoDB = AmazonDynamoDBClientBuilder.standard().build();
        return new DynamoDbFactory(dynamoDB);
    }
}
