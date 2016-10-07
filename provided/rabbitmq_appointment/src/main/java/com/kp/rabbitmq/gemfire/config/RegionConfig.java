package com.kp.rabbitmq.gemfire.config;


import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.client.ClientCache;
import com.gemstone.gemfire.cache.client.ClientRegionFactory;
import com.gemstone.gemfire.cache.client.ClientRegionShortcut;
import com.gemstone.gemfire.cache.client.Pool;

import io.pivotal.kaiser.gemfire.common.key.AppointmentKey;
import io.pivotal.kaiser.gemfire.common.model.Appointment;

@Configuration
public class RegionConfig {
	private static final Logger LOG = LoggerFactory.getLogger(RegionConfig.class);

	@Bean(name = "Appointment")
	public Region<AppointmentKey, Appointment> createAppointmentRegion(ClientCache cache, Pool pool) {
		LOG.info("Creating Region: {}", "region");
		ClientRegionFactory<AppointmentKey, Appointment> crf = cache.createClientRegionFactory(ClientRegionShortcut.PROXY);
		crf.setPoolName(pool.getName());
		Region<AppointmentKey, Appointment> r = crf.create("Appointment");
		return r;
	}

}
