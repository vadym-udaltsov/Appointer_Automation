package com.bot.config;

import com.commons.dao.ICustomerDao;
import com.commons.dao.impl.CustomerDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class BotConfig {

    @Bean
    public ICustomerDao customerDao() {
        return new CustomerDao();
    }

}
