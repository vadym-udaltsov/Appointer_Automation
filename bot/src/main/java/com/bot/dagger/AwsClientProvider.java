package com.bot.dagger;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.commons.dao.impl.DynamoDbFactory;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class AwsClientProvider {

    @Provides
    @Singleton
    public static DynamoDbFactory dynamoDbFactory(AmazonDynamoDB dynamoDb) {
        return new DynamoDbFactory(dynamoDb);
    }

    @Provides
    @Singleton
    public static AmazonDynamoDB dynamoDb() {
        return AmazonDynamoDBClientBuilder.standard().build();
    }

    @Provides
    @Singleton
    public static DynamoDBMapper dynamoDBMapper(AmazonDynamoDB dynamoDB) {
        return new DynamoDBMapper(dynamoDB);
    }

    @Provides
    @Singleton
    public static AmazonS3 s3Client() {
        return AmazonS3ClientBuilder.standard().build();
    }
}
