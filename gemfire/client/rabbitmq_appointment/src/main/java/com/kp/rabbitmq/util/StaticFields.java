package com.kp.rabbitmq.util;

import java.util.HashMap;
import java.util.Map;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StaticFields {
private static final Logger log = LoggerFactory.getLogger(StaticFields.class);
	
	private static Map<String, String> map = new HashMap<String, String>();
	static {
		map.put("REGION_CODE", "\"MRN\"");
		map.put("MRN_PREFIX","\"11\"");
		map.put("CREATED_BY", "\"MQ\"");
		map.put("UPDATED_BY", "\"MQ\"");
		/*//TODO : remove these values
		map.put("CREATE_TIMESTAMP", DatatypeConverter.printDateTime(Calendar.getInstance()));
		map.put("UPDATE_TIMESTAMP", DatatypeConverter.printDateTime(Calendar.getInstance()));
		map.put("ENTRY_TIME", DatatypeConverter.printDateTime(Calendar.getInstance()));
		*/
		

	}

	public static StringBuilder getStaticFields()
			throws Exception {
		StringBuilder buf = new StringBuilder();
		for (Map.Entry<String, String> entry : map.entrySet()){
					buf.append(entry.getKey());
					buf.append("=");
					buf.append(entry.getValue());
					buf.append(',');
		}
		log.debug("getStaticFields=="+buf);
		return buf;
	}

}
