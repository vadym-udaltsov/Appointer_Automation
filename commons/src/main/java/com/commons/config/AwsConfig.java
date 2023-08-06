package com.commons.config;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.sqs.SqsClient;

@Configuration
@Slf4j
public class AwsConfig {

    @Bean
    public CognitoIdentityProviderClient cognitoClient() {
        log.info("Cognito initialized");
        return CognitoIdentityProviderClient.builder()
                .region(Region.EU_CENTRAL_1)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }

    @Bean
    public AmazonSimpleEmailService sesClient() {
        log.info("Ses client initialized");
        return AmazonSimpleEmailServiceClientBuilder.defaultClient();
    }

    @Bean
    public SqsClient sqsClient() {
        return SqsClient.builder()
                .build();
    }

    @Bean
    public AWSSimpleSystemsManagement ssmClient() {
        log.info("Ssm client initialized");
        return AWSSimpleSystemsManagementClientBuilder.defaultClient();
    }

    @Bean
    public AmazonDynamoDB dynamoDb() {
        log.info("DynamoDb initialized");
        return AmazonDynamoDBClientBuilder.standard().build();
    }

    @Bean
    public DynamoDBMapper dynamoDBMapper(AmazonDynamoDB dynamoDB) {
        return new DynamoDBMapper(dynamoDB);
    }
}
