/**
 * 
 */
package com.kp.rabbitmq.spring.integration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kp.org.parrs.HL7Parser;
import com.kp.org.parrs.model.Parrs;
import com.kp.rabbitmq.util.GemfireWriter;

/**
 * @author Martin
 * 
 */
@Component("HL7Mapper")
public class HL7Mapper {
	private static final Logger log = LoggerFactory.getLogger(HL7Mapper.class);
	private static final String RECOVERABLE = "recoverable";
	private static final String NONRECOVERABLE = "nonrecoverable";

	@Autowired
	private HL7ErrorHandler hl7ErrorHandler;

	public void process(byte[] message) throws Exception {
		int code;
		try {
			log.debug("message received in HL7Mapper" + new String(message, "UTF-8"));
			Parrs parrs = new HL7Parser().getParrsObject(message);
			
			GemfireWriter gemfire = new GemfireWriter();

			code = gemfire.write(parrs);
			if (code != 0) {
				processReponse(new String(message, "UTF-8"), code, "msg",
						parrs.getAppointmentSchedule().getAppointmentId());
			}

		} catch (Exception ex) {
			log.error("exception in HL7Mapper.process ==" + ex.getMessage());
			log.warn("failed message :" + new String(message, "UTF-8"));
			hl7ErrorHandler.sendtoRabitMQ(new String(message, "UTF-8"), NONRECOVERABLE);
		}
	}

	private void processReponse(String message, int code, String errorMsg, String AppointmentId) {
		if (code == 99) {
			log.error("code=" + code + ", msg=" + AppointmentId + ":" + errorMsg);
			hl7ErrorHandler.sendtoRabitMQ(message, NONRECOVERABLE);
		} else {
			log.warn("code=" + code + ", msg=" + errorMsg);
		}
	}

}
