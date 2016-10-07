package com.kp.rabbitmq.spring.integration;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;

import com.kp.rabbitmq.AppointmentEventsApplication;

public class WebInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(AppointmentEventsApplication.class);
    }

}
