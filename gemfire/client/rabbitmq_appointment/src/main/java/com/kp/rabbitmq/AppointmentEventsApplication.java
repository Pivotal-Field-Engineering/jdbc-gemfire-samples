package com.kp.rabbitmq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Felipe Gutierrez <fgutierrezcruz@gopivotal.com>
 *
 */

@Configuration
@EnableAutoConfiguration
@ComponentScan
@ImportResource("classpath:META-INF/spring/integration/kaiser-integration-context.xml")
public class AppointmentEventsApplication {

	 public static void main(String[] args) throws Exception {
		 SpringApplication.run(AppointmentEventsApplication.class, args);
	 }
}
