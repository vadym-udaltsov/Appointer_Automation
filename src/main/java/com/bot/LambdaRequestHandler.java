package com.bot;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Slf4j
public class LambdaRequestHandler implements RequestStreamHandler {

    public LambdaRequestHandler() throws ContainerInitializationException {
        LambdaApplication.main(new String[]{});
    }

    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
        log.info("Lambda handler ------------------");
        LambdaApplication.handler.proxyStream(input, output, context);
    }
}
