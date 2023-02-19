package com.bot;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.spring.SpringBootLambdaContainerHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@Slf4j
public class LambdaApplication {

	public static SpringBootLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler;

	public static void main(String[] args) throws ContainerInitializationException {
		log.info("Main method -------------------");
		handler = SpringBootLambdaContainerHandler.getAwsProxyHandler(LambdaApplication.class);
	}
}
