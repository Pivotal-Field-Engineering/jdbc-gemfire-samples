package io.pivotal.kaiser.gemfire.common.model;

import java.sql.Timestamp;

//PDX Serializer
public class Appointment {
	String appointmentId; //key : move to key class for explicit type checking
	String cadenceApptId;
	String cadenceApptType;
	String regionCode;
	String mrn;
	String mrnPrefix;
	String facilityCode;
	String clinicCode;   // new field
    String clinicName;
	String adminDeptCode;
	String subDeptCode;  // new field
	Timestamp apptDatetime; // new field
	Timestamp apptBeginDatetime; 
	String apptDay;
	String apptDuration; // new field
	String apptDurationUnit; // new field
	String availabilityCode; // new field
    String bookedByRacfId;
    String apptTypeCode;
    String epicVisitTypeCode;
    String bookingGuidelineId;
    String apptResourceId;
	String resourceTypeCode;
    String refResourceId; 	// new field
	String memberClassCode;
    String secureCode; 	// new field
    String apptStatusCode; 	// new field
    String apptReasonDesc; 	// new field
    Timestamp apptCancelTimestamp; 	// new field
	String apptCancellationCode;
    String apptCancelReasonDesc; 	// New Field
    String apptRoutingAddress; 	// New Field
    String referringFacCode; 	// New Field
    String patientFacilityCode; 	// New Field
    String patientSubDeptCode; // New Field
	String reminderPrefCode;
	String xrayRequestFlag;
    String chartRequestFlag; // 	String StringtRequestFlag;   this field was an error.  Don't even know how it worked unless always null
	String medicalRecordFlag;
	String explicitlyLinkedFlag;
	String implicitlyLinkedFlag;
	String sendConfirmNoticeFlag;
	String schedulableFlag;
	String reschedulableFlag;
	String cancellableFlag;
	String inquirableFlag;
	String note;
	String instructions;
	String sourceQueueId;
	String sendingApplication;
	String receivingApplication;
	Timestamp messageDatetime;
	String messageTypeCode;
	Timestamp createTimestamp;
	Timestamp updateTimestamp;
	//	Timestamp entryTime;  field not in record
    Timestamp entryDate; // Fixed type to Timestamp
	String createId;
	String updateId;
	String createdBy;
	String updatedBy;
	Timestamp eventDatetime;
	
	public String getAppointmentId() {
		return appointmentId;
	}
	public void setAppointmentId(String appointmentId) {
		this.appointmentId = appointmentId;
	}

	public String getCadenceApptId() {
		return cadenceApptId;
	}
	public void setCadenceApptId(String cadenceApptId) {
		this.cadenceApptId = cadenceApptId;
	}
	public String getCadenceApptType() {
		return cadenceApptType;
	}
	public void setCadenceApptType(String cadenceApptType) {
		this.cadenceApptType = cadenceApptType;
	}
	public String getRegionCode() {
		return regionCode;
	}
	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}
	public String getMrn() {
		return mrn;
	}
	public void setMrn(String mrn) {
		this.mrn = mrn;
	}
	public String getMrnPrefix() {
		return mrnPrefix;
	}
	public void setMrnPrefix(String mrnPrefix) {
		this.mrnPrefix = mrnPrefix;
	}
	public String getFacilityCode() {
		return facilityCode;
	}
	public void setFacilityCode(String facilityCode) {
		this.facilityCode = facilityCode;
	}
	public String getClinicCode() {
		return clinicCode;
	}
	public void setClinicCode(String clinicCode) {
		this.clinicCode = clinicCode;
	}
	public String getAdminDeptCode() {
		return adminDeptCode;
	}
	public void setAdminDeptCode(String adminDeptCode) {
		this.adminDeptCode = adminDeptCode;
	}
	public String getApptDay() {
		return apptDay;
	}
	public void setApptDay(String apptDay) {
		this.apptDay = apptDay;
	}
	public String getResourceTypeCode() {
		return resourceTypeCode;
	}
	public void setResourceTypeCode(String resourceTypeCode) {
		this.resourceTypeCode = resourceTypeCode;
	}
	public String getMemberClassCode() {
		return memberClassCode;
	}
	public void setMemberClassCode(String memberClassCode) {
		this.memberClassCode = memberClassCode;
	}
	public String getApptCancellationCode() {
		return apptCancellationCode;
	}
	public void setApptCancellationCode(String apptCancellationCode) {
		this.apptCancellationCode = apptCancellationCode;
	}
	public String getReminderPrefCode() {
		return reminderPrefCode;
	}
	public void setReminderPrefCode(String reminderPrefCode) {
		this.reminderPrefCode = reminderPrefCode;
	}
	public String getXrayRequestFlag() {
		return xrayRequestFlag;
	}
	public void setXrayRequestFlag(String xrayRequestFlag) {
		this.xrayRequestFlag = xrayRequestFlag;
	}
	public String getChartRequestFlag() {
		return chartRequestFlag;
	}
	public void setChartRequestFlag(String chartRequestFlag) {
		chartRequestFlag = chartRequestFlag;
	}
	public String getClinicName() {
		return clinicName;
	}
	public void setClinicName(String clinicName) {
		this.clinicName = clinicName;
	}
	public String getSubDeptCode() {
		return subDeptCode;
	}
	public void setSubDeptCode(String subDeptCode) {
		this.subDeptCode = subDeptCode;
	}
	public Timestamp getApptDatetime() {
		return apptDatetime;
	}
	public void setApptDatetime(Timestamp apptDatetime) {
		this.apptDatetime = apptDatetime;
	}
	public Timestamp getApptBeginDatetime() {
		return apptBeginDatetime;
	}
	public void setApptBeginDatetime(Timestamp apptBeginDatetime) {
		this.apptBeginDatetime = apptBeginDatetime;
	}
	public String getApptDuration() {
		return apptDuration;
	}
	public void setApptDuration(String apptDuration) {
		this.apptDuration = apptDuration;
	}
	public String getApptDurationUnit() {
		return apptDurationUnit;
	}
	public void setApptDurationUnit(String apptDurationUnit) {
		this.apptDurationUnit = apptDurationUnit;
	}
	public String getAvailabilityCode() {
		return availabilityCode;
	}
	public void setAvailabilityCode(String availabilityCode) {
		this.availabilityCode = availabilityCode;
	}
	public String getBookedByRacfId() {
		return bookedByRacfId;
	}
	public void setBookedByRacfId(String bookedByRacfId) {
		this.bookedByRacfId = bookedByRacfId;
	}
	public String getApptTypeCode() {
		return apptTypeCode;
	}
	public void setApptTypeCode(String apptTypeCode) {
		this.apptTypeCode = apptTypeCode;
	}
	public String getEpicVisitTypeCode() {
		return epicVisitTypeCode;
	}
	public void setEpicVisitTypeCode(String epicVisitTypeCode) {
		this.epicVisitTypeCode = epicVisitTypeCode;
	}
	public String getBookingGuidelineId() {
		return bookingGuidelineId;
	}
	public void setBookingGuidelineId(String bookingGuidelineId) {
		this.bookingGuidelineId = bookingGuidelineId;
	}
	public String getApptResourceId() {
		return apptResourceId;
	}
	public void setApptResourceId(String apptResourceId) {
		this.apptResourceId = apptResourceId;
	}
	public String getRefResourceId() {
		return refResourceId;
	}
	public void setRefResourceId(String refResourceId) {
		this.refResourceId = refResourceId;
	}
	public String getSecureCode() {
		return secureCode;
	}
	public void setSecureCode(String secureCode) {
		this.secureCode = secureCode;
	}
	public String getApptStatusCode() {
		return apptStatusCode;
	}
	public void setApptStatusCode(String apptStatusCode) {
		this.apptStatusCode = apptStatusCode;
	}
	public String getApptReasonDesc() {
		return apptReasonDesc;
	}
	public void setApptReasonDesc(String apptReasonDesc) {
		this.apptReasonDesc = apptReasonDesc;
	}
	public Timestamp getApptCancelTimestamp() {
		return apptCancelTimestamp;
	}
	public void setApptCancelTimestamp(Timestamp apptCancelTimestamp) {
		this.apptCancelTimestamp = apptCancelTimestamp;
	}
	public String getApptCancelReasonDesc() {
		return apptCancelReasonDesc;
	}
	public void setApptCancelReasonDesc(String apptCancelReasonDesc) {
		this.apptCancelReasonDesc = apptCancelReasonDesc;
	}
	public String getApptRoutingAddress() {
		return apptRoutingAddress;
	}
	public void setApptRoutingAddress(String apptRoutingAddress) {
		this.apptRoutingAddress = apptRoutingAddress;
	}
	public String getReferringFacCode() {
		return referringFacCode;
	}
	public void setReferringFacCode(String referringFacCode) {
		this.referringFacCode = referringFacCode;
	}
	public String getPatientFacilityCode() {
		return patientFacilityCode;
	}
	public void setPatientFacilityCode(String patientFacilityCode) {
		this.patientFacilityCode = patientFacilityCode;
	}
	public String getPatientSubDeptCode() {
		return patientSubDeptCode;
	}
	public void setPatientSubDeptCode(String patientSubDeptCode) {
		this.patientSubDeptCode = patientSubDeptCode;
	}
	public String getMedicalRecordFlag() {
		return medicalRecordFlag;
	}
	public void setMedicalRecordFlag(String medicalRecordFlag) {
		this.medicalRecordFlag = medicalRecordFlag;
	}
	public String getExplicitlyLinkedFlag() {
		return explicitlyLinkedFlag;
	}
	public void setExplicitlyLinkedFlag(String explicitlyLinkedFlag) {
		this.explicitlyLinkedFlag = explicitlyLinkedFlag;
	}
	public String getImplicitlyLinkedFlag() {
		return implicitlyLinkedFlag;
	}
	public void setImplicitlyLinkedFlag(String implicitlyLinkedFlag) {
		this.implicitlyLinkedFlag = implicitlyLinkedFlag;
	}
	public String getSendConfirmNoticeFlag() {
		return sendConfirmNoticeFlag;
	}
	public void setSendConfirmNoticeFlag(String sendConfirmNoticeFlag) {
		this.sendConfirmNoticeFlag = sendConfirmNoticeFlag;
	}
	public String getSchedulableFlag() {
		return schedulableFlag;
	}
	public void setSchedulableFlag(String schedulableFlag) {
		this.schedulableFlag = schedulableFlag;
	}
	public String getReschedulableFlag() {
		return reschedulableFlag;
	}
	public void setReschedulableFlag(String reschedulableFlag) {
		this.reschedulableFlag = reschedulableFlag;
	}
	public String getCancellableFlag() {
		return cancellableFlag;
	}
	public void setCancellableFlag(String cancellableFlag) {
		this.cancellableFlag = cancellableFlag;
	}
	public String getInquirableFlag() {
		return inquirableFlag;
	}
	public void setInquirableFlag(String inquirableFlag) {
		this.inquirableFlag = inquirableFlag;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getInstructions() {
		return instructions;
	}
	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}
	public String getSourceQueueId() {
		return sourceQueueId;
	}
	public void setSourceQueueId(String sourceQueueId) {
		this.sourceQueueId = sourceQueueId;
	}
	public String getSendingApplication() {
		return sendingApplication;
	}
	public void setSendingApplication(String sendingApplication) {
		this.sendingApplication = sendingApplication;
	}
	public String getReceivingApplication() {
		return receivingApplication;
	}
	public void setReceivingApplication(String receivingApplication) {
		this.receivingApplication = receivingApplication;
	}
	public Timestamp getMessageDatetime() {
		return messageDatetime;
	}
	public void setMessageDatetime(Timestamp messageDatetime) {
		this.messageDatetime = messageDatetime;
	}
	public String getMessageTypeCode() {
		return messageTypeCode;
	}
	public void setMessageTypeCode(String messageTypeCode) {
		this.messageTypeCode = messageTypeCode;
	}
	public Timestamp getCreateTimestamp() {
		return createTimestamp;
	}
	public void setCreateTimestamp(Timestamp createTimestamp) {
		this.createTimestamp = createTimestamp;
	}
	public Timestamp getUpdateTimestamp() {
		return updateTimestamp;
	}
	public void setUpdateTimestamp(Timestamp updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}
	public Timestamp getEntryDate() {
		return entryDate;
	}
	public void setEntryDate(Timestamp entryDate) {
		this.entryDate = entryDate;
	}
	public String getCreateId() {
		return createId;
	}
	public void setCreateId(String createId) {
		this.createId = createId;
	}
	public String getUpdateId() {
		return updateId;
	}
	public void setUpdateId(String updateId) {
		this.updateId = updateId;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public Timestamp getEventDatetime() {
		return eventDatetime;
	}
	public void setEventDatetime(Timestamp eventDatetime) {
		this.eventDatetime = eventDatetime;
	}
}
