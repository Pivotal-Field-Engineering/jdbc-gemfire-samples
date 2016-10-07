package com.kp.rabbitmq.util;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// import com.kp.org.parrs.constant.CancellationCode;
import com.kp.org.parrs.model.AppointmentSchedule;

public class AppointmentScheduleFields {
	private static final Logger log = LoggerFactory.getLogger(AppointmentScheduleFields.class);
	
	//TODO: Rename to javaBean properties to conform to gemfire-common object model
	private static Map<String, FieldDetail> map = new HashMap<String, FieldDetail>();
	static{
		//map.put("appointmentId", new FieldDetail("APPOINTMENT_ID", "String"));
		map.put("appointmentId", new FieldDetail("APPOINTMENT_ID", "long"));
		map.put("mrn", new FieldDetail("MRN", "String"));
		map.put("facilityId", new FieldDetail("FACILITY_CODE", "String"));
		map.put("clinicId", new FieldDetail("CLINIC_CODE", "String"));
		map.put("appointmentDateAndTime", new FieldDetail("APPT_DATETIME", "Calendar"));
		map.put("appointmentStartDateAndTime", new FieldDetail("APPT_BEGIN_DATETIME", "Calendar"));
		map.put("appointmentDuration", new FieldDetail("APPT_DURATION", "int"));
		map.put("appointmentDurationUnit", new FieldDetail("APPT_DURATION_UNIT", "String"));
		map.put("availabiltyCode", new FieldDetail("AVAILABILITY_CODE", "String"));
		map.put("bookedById", new FieldDetail("BOOKED_BY_RACF_ID", "String"));
		map.put("appointmentType", new FieldDetail("APPT_TYPE_CODE", "String"));
		
		map.put("resourceId", new FieldDetail("APPT_RESOURCE_ID", "String"));
		map.put("appoinmentStatus", new FieldDetail("APPT_STATUS_CODE", "String"));
		map.put("cancellationCode", new FieldDetail("APPT_CANCELLATION_CODE", "CancellationCode"));
		map.put("cancellationDesc", new FieldDetail("APPT_CANCEL_REASON_DESC", "CancellationCode"));
		map.put("entryDate", new FieldDetail("ENTRY_DATE", "Calendar"));
		//TODO This is not mapped in smooks
		map.put("eventDate", new FieldDetail("EVENT_DATETIME", "Calendar"));
		
	}
	
	//TODO:  Convert method to produce a map with correct types
	public static StringBuilder getAppointmentScheduleFields(AppointmentSchedule sch) throws Exception{
		StringBuilder buf = new StringBuilder();
		Field [] fields=AppointmentSchedule.class.getDeclaredFields();
		for(Field field : fields){
			field.setAccessible(true);
			FieldDetail filedDetail=map.get(field.getName());
			if(filedDetail != null && field.get(sch)!= null){
				buf.append(filedDetail.sqlField);
				if(filedDetail.getDataType().equals("String") ){
					buf.append("=\"");
					buf.append((String) field.get(sch));
					buf.append('\"');
				}else if(filedDetail.getDataType().equals("Calendar")){
					buf.append("="+DatatypeConverter.printDateTime((Calendar)field.get(sch)));
				}		
				else if(filedDetail.getDataType().equals("CancellationCode")){
					buf.append("=\"");
					buf.append(field.get(sch));
					buf.append('\"');
				}else if(filedDetail.getDataType().equals("int")){
					buf.append("=\"");
					buf.append((Integer) field.get(sch));
					buf.append('\"');
				}
				else if(filedDetail.getDataType().equals("long")){
					buf.append("=");
					buf.append((String) field.get(sch));
					
				}
				buf.append(',');
			}
		}
		log.debug("getAppointmentScheduleFields=="+buf);
		return buf;
	}
}
