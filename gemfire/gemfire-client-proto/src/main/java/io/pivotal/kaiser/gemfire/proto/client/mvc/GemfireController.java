package io.pivotal.kaiser.gemfire.proto.client.mvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.client.ClientCache;

import io.pivotal.kaiser.gemfire.common.key.AppointmentKey;
import io.pivotal.kaiser.gemfire.common.model.Appointment;

@RestController
public class GemfireController {
	private static final Logger LOG = LoggerFactory.getLogger(GemfireController.class);

	@Autowired
	ClientCache cache;
	

	@RequestMapping(value = "/insert")
	public @ResponseBody void insertData() throws Exception {
		Region<AppointmentKey,Appointment> region = cache.getRegion("Appointment");
		Appointment value = new Appointment();
		AppointmentKey key = new AppointmentKey();
		
		
	
	}
	
	
	@RequestMapping(value = "/update")
	public @ResponseBody void updateData() throws Exception {
		Region<AppointmentKey,Appointment> region = cache.getRegion("Appointment");
		Appointment value = new Appointment();
		AppointmentKey key = new AppointmentKey();	
	
	
	}
}
