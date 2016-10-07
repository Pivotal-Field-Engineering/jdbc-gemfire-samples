package io.pivotal.kaiser.gemfire.proto.client.appointment.util;

import java.util.Map;
import java.util.function.BiFunction;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.client.ClientCache;
import com.kp.org.parrs.model.Parrs;

import io.pivotal.kaiser.gemfire.common.key.AppointmentKey;
import io.pivotal.kaiser.gemfire.common.model.Appointment;

public class GemfireWriter {
	private static final Logger LOG = LoggerFactory.getLogger(GemfireWriter.class);
	@Autowired
	ClientCache cache;

	@Autowired
	DozerBeanMapper dmb;

	public void write(Parrs p) {
		try {
			AppointmentMapper apptMapper = new AppointmentMapper();
			Region<AppointmentKey, Appointment> region = cache.getRegion("Appointment");
			Map<String, Object> value = apptMapper.mapAppointment(p);
			if (value != null) {
				Mapper mp = dmb;
				final Appointment v = mp.map(value, Appointment.class);
				AppointmentKey k = mp.map(value, AppointmentKey.class);
				region.compute(k, new BiFunction<AppointmentKey, Appointment, Appointment>() {

					@Override
					public Appointment apply(AppointmentKey key, Appointment old) {
						if (old == null) {
							LOG.debug("Region DNE: {Creating..}");
							return v; //create
						} else if (v.getUpdateTimestamp().equals(old.getUpdateTimestamp())){
							LOG.warn("Record timestamps are equal : {Doing Nothing..}"); //do nothing
						}else if (v.getUpdateTimestamp().before(old.getUpdateTimestamp())){
							LOG.warn("Record is already current : {Doing Nothing ..}");
						}
							else if (v.getUpdateTimestamp().after(old.getUpdateTimestamp())) {
							LOG.debug("New Update : {Updating..}");
							return v; //update
						}
						return null;
					}
				});
			}
		} catch (Exception e) {
		}
	}
}
