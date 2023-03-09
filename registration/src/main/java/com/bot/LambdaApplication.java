package com.bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.commons.config", "com.commons.service", "com.commons.dao", "com.bot.lambda"})
@Slf4j
public class LambdaApplication {

    public static ApplicationContext CONTEXT;

    public static void main(String[] args) {
        CONTEXT = SpringApplication.run(LambdaApplication.class, args);
    }
}
