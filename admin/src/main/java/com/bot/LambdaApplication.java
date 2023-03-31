package com.bot;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.spring.SpringBootLambdaContainerHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@Slf4j
@SpringBootApplication
@ComponentScan({"com.commons.config", "com.commons.service", "com.commons.dao", "com.bot.controller", "com.commons.filter",
        "com.bot.service", "com.bot.lambda"})
public class LambdaApplication {

    public static SpringBootLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler;

    public static void main(String[] args) throws ContainerInitializationException {
        handler = SpringBootLambdaContainerHandler.getAwsProxyHandler(LambdaApplication.class);
//        SpringApplication.run(LambdaApplication.class);
    }
}
