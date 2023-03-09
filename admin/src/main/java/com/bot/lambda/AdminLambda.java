package com.bot.lambda;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.bot.LambdaApplication;
import com.bot.LambdaFunction;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@LambdaFunction("adminLambda")
public class AdminLambda implements RequestStreamHandler {

    public AdminLambda() throws ContainerInitializationException, IOException {
        LambdaApplication.main(new String[]{});
    }

    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
        LambdaApplication.handler.proxyStream(input, output, context);
    }
}
