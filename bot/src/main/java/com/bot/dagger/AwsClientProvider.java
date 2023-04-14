package com.bot.dagger;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.commons.dao.impl.DynamoDbFactory;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class AwsClientProvider {

    @Provides
    @Singleton
    DynamoDbFactory dynamoDbFactory(AmazonDynamoDB dynamoDb) {
        return new DynamoDbFactory(dynamoDb);
    }

    @Provides
    @Singleton
    public AmazonDynamoDB dynamoDb() {
        return AmazonDynamoDBClientBuilder.standard().build();
    }

    @Provides
    @Singleton
    public DynamoDBMapper dynamoDBMapper(AmazonDynamoDB dynamoDB) {
        return new DynamoDBMapper(dynamoDB);
    }
}
