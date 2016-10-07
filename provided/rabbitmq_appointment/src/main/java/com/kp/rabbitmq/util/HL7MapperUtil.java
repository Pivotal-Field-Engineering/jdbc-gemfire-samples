package com.kp.rabbitmq.util;


import com.kp.org.parrs.model.Parrs;

public class HL7MapperUtil {
	
	public static String convertParrsToString(Parrs parrs) throws Exception{
		StringBuilder buf = new StringBuilder();
		buf.append(AppointmentScheduleFields.getAppointmentScheduleFields(parrs.getAppointmentSchedule()));
		buf.append(AppointmentResourceFields.getAppointmentResourceFields(parrs.getAppointmentResource()));
		buf.append(PatientVisitFields.getPatientVisitFields(parrs.getPatient().getPatientVisit()));
		buf.append(MessageHeaderFields.getMessageHeaderFields(parrs.getMessageHeader()));
		buf.append(StaticFields.getStaticFields());
		buf.append("APPT_REASON_DESC=\""+parrs.getNotes().getAppointmentReason()+"\"");
		System.out.println(buf.toString());
		System.out.println("parrs=="+parrs);
		return buf.toString();
	}

}
