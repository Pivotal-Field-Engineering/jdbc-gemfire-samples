package com.kp.rabbitmq.util;

import java.util.Map;
import java.util.function.BiFunction;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.client.ClientCache;
import com.kp.org.parrs.model.Parrs;

import io.pivotal.kaiser.gemfire.common.key.AppointmentKey;
import io.pivotal.kaiser.gemfire.common.model.Appointment;

@Component
public class GemfireWriter {
	private static final Logger LOG = LoggerFactory.getLogger(GemfireWriter.class);

	@Autowired
	ClientCache cache;

	@Autowired
	DozerBeanMapper dmb;

	public int write(Parrs p) {
		// TODO: Its never going to return 99 to rmq. Need to fix our exception
		// handling.
		int code = 0;
		try {
			AppointmentMapper apptMapper = new AppointmentMapper();
			Map<String, Object> value = apptMapper.mapAppointment(p);
			if (value != null) {
				Mapper mp = dmb;
				Appointment v = mp.map(value, Appointment.class);
				AppointmentKey k = new AppointmentKey();
				k.setAppointmentId(v.getAppointmentId());
				Region<AppointmentKey, Appointment> region = cache.getRegion("Appointment");
				region.compute(k, new BiFunction<AppointmentKey, Appointment, Appointment>() {

					@Override
					public Appointment apply(AppointmentKey key, Appointment old) {
						if (old == null) {
							LOG.info("Appointment DNE: {Creating..}");
							return v; // create
						} else if (v.getEventDatetime().after(old.getEventDatetime())) {
							LOG.info("New Update : {Updating..}");
							return v; // update
						}
						LOG.debug("Appointment is old = {}", old.getAppointmentId());
						return old;
					}
				});
				code = 0;
			} else {
				LOG.error("Appointment from Parrs is null");
				code = 0;
			}
		} catch (Exception e) {
			LOG.error("Exception in {} : [{}]", "GemfireWriter", e.getMessage());
		}
		return code;
	}
}
