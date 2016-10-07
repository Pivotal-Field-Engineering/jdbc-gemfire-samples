package io.pivotal.kaiser.gemfire.proto.client.hl7.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.kp.org.parrs.HL7Parser;
import com.kp.org.parrs.model.Parrs;

import io.pivotal.kaiser.gemfire.proto.client.appointment.util.GemfireWriter;

@Component("HL7GemfireMapper")
public class HL7GemfireMapper {
	private static final Logger log = LoggerFactory.getLogger(HL7GemfireMapper.class);

	public void process(byte[] message) throws Exception {
		GemfireWriter gemfire = new GemfireWriter();
		try {
			log.debug("message received in HL7Mapper" + new String(message, "UTF-8"));
			Parrs parrs = new HL7Parser().getParrsObject(message);
			gemfire.write(parrs);
		} catch (Exception ex) {

			log.error("exception in HL7Mapper.process ==" + ex.getMessage());
			log.warn("failed message :" + new String(message, "UTF-8"));
		}
	}
}
