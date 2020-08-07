package com.thoughtworks.rslist.config;

import com.thoughtworks.rslist.service.RsEventService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public RsEventService rsEventService() {
        return new RsEventService();
    }
}
