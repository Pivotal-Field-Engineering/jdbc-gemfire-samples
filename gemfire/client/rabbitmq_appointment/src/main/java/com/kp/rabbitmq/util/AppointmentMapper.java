package com.kp.rabbitmq.util;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AppointmentMapper {

	public AppointmentMapper() {

	}

	public Map<String, Object> mapAppointment(com.kp.org.parrs.model.Parrs p) {
		Map<String, Object> map = new HashMap<>();

		String operation = p.getMessageHeader().getMessageType().toString().equals("S12") ? "new" : "update";
//		log.debug("operation==" + operation);
		
		map.put("appointmentId", p.getAppointmentSchedule().getAppointmentId());
		// map.put("cadenceApptType", "");
		map.put("regionCode", "MRN");
		map.put("mrn", p.getAppointmentSchedule().getMrn());
		map.put("mrnPrefix", "11");
		map.put("facilityCode", p.getAppointmentSchedule().getFacilityId());
		map.put("clinicCode", p.getAppointmentSchedule().getClinicId());
		// map.put("clinicName", "");
		map.put("adminDeptCode", p.getPatient().getPatientVisit().getAdminDepartmentCode());
		map.put("subDeptCode", p.getAppointmentResource().getSubDepartmentCode());
		map.put("apptDatetime", p.getAppointmentSchedule().getAppointmentDateAndTime());
		map.put("apptBeginDatetime", p.getAppointmentSchedule().getAppointmentStartDateAndTime());
		// map.put("apptDay", "");
		map.put("apptDuration", p.getAppointmentSchedule().getAppointmentDuration());
		map.put("apptDurationUnit", p.getAppointmentSchedule().getAppointmentDurationUnit());
		map.put("availabilityCode", p.getAppointmentSchedule().getAvailabiltyCode());
		map.put("bookedByRacfId", p.getAppointmentSchedule().getBookedById());
		map.put("apptTypeCode", p.getAppointmentSchedule().getAppointmentType());
		map.put("epicVisitTypeCode", p.getPatient().getPatientVisit().getEpicVisitType());
		// map.put("bookingGuidelineId", "");
		map.put("apptResourceId", p.getAppointmentSchedule().getResourceId());
		map.put("resourceTypeCode", p.getAppointmentResource().getResourceTypes().get(0));
		map.put("refResourceId", p.getPatient().getPatientVisit().getReferringDoctorId());
		map.put("memberClassCode", p.getPatient().getPatientVisit().getPatientClass());
		// map.put("secureCode","");
		map.put("apptStatusCode", p.getAppointmentSchedule().getAppoinmentStatus());
		map.put("apptReasonDesc", p.getNotes().getAppointmentReason());
		// map.put("apptCancelTimestamp", "");
		map.put("apptCancellationCode", p.getAppointmentSchedule().getCancellationCd());
		map.put("apptCancelReasonDesc", p.getAppointmentSchedule().getCancellationDesc());
		// map.put("apptRoutingAddress", "");
		map.put("referringFacCode", p.getPatient().getPatientVisit().getReferringFacilty());
		map.put("patientFacilityCode", p.getPatient().getPatientVisit().getPatientFacilty());
		map.put("patientSubDeptCode", p.getPatient().getPatientVisit().getPatientSubDepartment());
		// map.put("reminderPrefCode", "");
		// map.put("xrayRequestFlag", "");
		// map.put("chartRequestFlag", "");
		// map.put("medicalRecordFlag", "");
		// map.put("explicitlyLinkedFlag", "");
		// map.put("implicitlyLinkedFlag", "");
		// map.put("sendConfirmNoticeFlag", "");
		// map.put("schedulableFlag", "");
		// map.put("reschedulableFlag", "");
		// map.put("cancellableFlag", "");
		// map.put("inquirableFlag", "");
		// map.put("note", p.
		// map.put("instructions", "");
//		map.put("sourceQueueId", "");
		map.put("sendingApplication", p.getMessageHeader().getSendingApplication());
		map.put("receivingApplication", p.getMessageHeader().getReceivingApplication());
		map.put("messageDatetime", p.getMessageHeader().getDateAndTimeOfMessage());
		map.put("messageTypeCode", p.getMessageHeader().getMessageType());
		if (operation.equals("new")) {
			map.put("createTimestamp", Calendar.getInstance());
			map.put("updateTimestamp", Calendar.getInstance());
		} else {
			map.put("updateTimestamp", Calendar.getInstance());			
		}
		map.put("entryDate", p.getAppointmentSchedule().getEntryDate());
//		map.put("createId", "");
//		map.put("updateId", "");
		map.put("createdBy", "MQ");
		map.put("updatedBy", "MQ");
		// Comes from MQ - system of record for dateTime
		map.put("eventDatetime", p.getMessageHeader().getEventDateAndTime());
		return map;
	}

}
