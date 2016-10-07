package io.pivotal.kaiser.gemfire.proto.client.appointment.util;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.kp.org.parrs.model.Parrs;

public class AppointmentMapper {

	public AppointmentMapper() {

	}
	
	public Map<String, Object> mapAppointment(Parrs p) {
		Map<String, Object> map = new HashMap<>();
		map.put("appointmentId", p.getAppointmentSchedule().getAppointmentId());
		map.put("regionCode", "MRN");
		map.put("mrn", p.getAppointmentSchedule().getMrn());
		map.put("mrnPrefix", "11");
		map.put("facilityCode", p.getAppointmentSchedule().getFacilityId());
		map.put("clinicCode", p.getAppointmentSchedule().getClinicId());
		map.put("adminDeptCode", p.getPatient().getPatientVisit().getAdminDepartmentCode());
		map.put("subDeptCode", p.getAppointmentResource().getSubDepartmentCode());
		map.put("apptDatetime", p.getAppointmentSchedule().getAppointmentDateAndTime());
		map.put("apptBeginDatetime", p.getAppointmentSchedule().getAppointmentStartDateAndTime());
		map.put("apptDuration", p.getAppointmentSchedule().getAppointmentDuration());
		map.put("apptDurationUnit", p.getAppointmentSchedule().getAppointmentDurationUnit());
		map.put("availabilityCode", p.getAppointmentSchedule().getAvailabiltyCode());
		map.put("bookedByRacfId", p.getAppointmentSchedule().getBookedById());
		map.put("apptTypeCode", p.getAppointmentSchedule().getAppointmentType());
		map.put("epicVisitTypeCode", p.getPatient().getPatientVisit().getEpicVisitType());
		map.put("apptResourceId", p.getAppointmentSchedule().getResourceId());
		map.put("resourceTypeCode", p.getAppointmentResource().getResourceTypes().get(0));
		map.put("refResourceId", p.getPatient().getPatientVisit().getReferringDoctorId());
		map.put("memberClassCode", p.getPatient().getPatientVisit().getPatientClass());
		map.put("apptStatusCode", p.getAppointmentSchedule().getAppoinmentStatus());
		map.put("apptReasonDesc", p.getNotes().getAppointmentReason());
		map.put("apptCancellationCode", p.getAppointmentSchedule().getCancellationCd());
		map.put("apptCancelReasonDesc", p.getAppointmentSchedule().getCancellationDesc());
		map.put("referringFacCode", p.getPatient().getPatientVisit().getReferringFacilty());
		map.put("patientFacilityCode", p.getPatient().getPatientVisit().getPatientFacilty());
		map.put("patientSubDeptCode", p.getPatient().getPatientVisit().getPatientSubDepartment());
		map.put("instructions", "");
		map.put("sourceQueueId", "");
		map.put("sendingApplication", p.getMessageHeader().getSendingApplication());
		map.put("receivingApplication", p.getMessageHeader().getReceivingApplication());
		map.put("messageDatetime", p.getMessageHeader().getDateAndTimeOfMessage());
		map.put("messageTypeCode", p.getMessageHeader().getMessageType());
		map.put("createTimestamp", Calendar.getInstance());
		map.put("updateTimestamp", Calendar.getInstance());
		map.put("entryDate", p.getAppointmentSchedule().getEntryDate());
		map.put("createId", "");
		map.put("updateId", "");
		map.put("createdBy", "MQ");
		map.put("updatedBy", "MQ");
		map.put("eventDatetime", p.getMessageHeader().getEventDateAndTime());
		return map;
	}
	// map.put("bookingGuidelineId", "");
	// map.put("secureCode","");
	// map.put("apptCancelTimestamp", "");
	// map.put("apptRoutingAddress", "");
	// map.put("reminderPrefCode", "");
	// map.put("apptDay", "");
	// map.put("clinicName", "");
	// map.put("cadenceApptType", "");
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
}
