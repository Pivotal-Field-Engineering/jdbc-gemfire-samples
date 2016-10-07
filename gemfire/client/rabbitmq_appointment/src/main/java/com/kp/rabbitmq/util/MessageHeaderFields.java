package com.kp.rabbitmq.util;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kp.org.parrs.constant.MessageType;
import com.kp.org.parrs.model.MessageHeader;

public class MessageHeaderFields {
	private static final Logger log = LoggerFactory.getLogger(MessageHeaderFields.class);
	
	private static Map<String, FieldDetail> map = new HashMap<String, FieldDetail>();
	static {
		map.put("sendingApplication",
				new FieldDetail("SENDING_APPLICATION", "String"));
		map.put("receivingApplication", new FieldDetail("RECEIVING_APPLICATION", "String"));
		map.put("dateAndTimeOfMessage", new FieldDetail("MESSAGE_DATETIME",
				"Calendar"));
		map.put("messageType", new FieldDetail("MESSAGE_TYPE_CODE",
				"MessageType"));
		map.put("eventDateAndTime", new FieldDetail("EVENT_DATETIME",
				"Calendar"));

	}

	public static StringBuilder getMessageHeaderFields(MessageHeader msg)
			throws Exception {
		StringBuilder buf = new StringBuilder();
		Field[] fields = MessageHeader.class.getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			FieldDetail filedDetail = map.get(field.getName());
			if (filedDetail != null && field.get(msg) != null) {
				buf.append(filedDetail.sqlField);
				if (filedDetail.getDataType().equals("String")) {
					buf.append("=\"");
					buf.append((String) field.get(msg));
					buf.append('\"');
				} else if (filedDetail.getDataType().equals("Calendar")) {
					buf.append("="
							+ DatatypeConverter.printDateTime((Calendar)field.get(msg)));
				} else if (filedDetail.getDataType().equals("MessageType")) {
					buf.append("=\"");
					buf.append(((MessageType) field.get(msg)));
					buf.append('\"');
				}
				buf.append(',');
			}

		}
		log.debug("getMessageHeaderFields=="+buf);
		return buf;
	}
}
