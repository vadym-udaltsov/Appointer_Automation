package com.bot.config;

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
    public AWSSimpleSystemsManagement ssmClient() {
        log.info("Ssm client initialized");
        return AWSSimpleSystemsManagementClientBuilder.defaultClient();
    }

}
