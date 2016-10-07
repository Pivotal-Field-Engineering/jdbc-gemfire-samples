package com.kp.rabbitmq.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kp.org.parrs.model.EpicVisitType;
import com.kp.org.parrs.model.PatientVisit;

public class PatientVisitFields {
	private static final Logger log = LoggerFactory.getLogger(PatientVisitFields.class);
	
	private static Map<String, FieldDetail> map = new HashMap<String, FieldDetail>();
	static {
		map.put("epicVisitType", new FieldDetail("EPIC_VISIT_TYPE_CODE",
				"EpicVisitType"));
		map.put("referringDoctorId", new FieldDetail("REF_RESOURCE_ID",
				"String"));
		map.put("patientClass", new FieldDetail("MEMBER_CLASS_CODE", "String"));
		map.put("referringFacilty", new FieldDetail("REFERRING_FAC_CODE",
				"String"));
		map.put("patientFacility", new FieldDetail("PATIENT_FACILITY_CODE",
				"String"));
		map.put("patientSubDepartment", new FieldDetail(
				"PATIENT_SUB_DEPT_CODE", "String"));
		map.put("adminDepartmentCode", new FieldDetail("ADMIN_DEPT_CODE",
				"String"));
		map.put("bookingMethodCode", new FieldDetail("BOOKING_METHOD_CODE",
				"String"));
		map.put("activityCode", new FieldDetail("ACTIVITY_CODE", "String"));

	}

	// TODO: Convert to Map for retaining types
	public static StringBuilder getPatientVisitFields(PatientVisit pv)
			throws Exception {
		StringBuilder buf = new StringBuilder();
		Field[] fields = PatientVisit.class.getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			FieldDetail filedDetail = map.get(field.getName());
			if (filedDetail != null && field.get(pv) != null) {
				buf.append(filedDetail.sqlField);
				if (filedDetail.getDataType().equals("String")) {
					buf.append("=\"");
					buf.append((String) field.get(pv));
					buf.append('\"');
				} else if (filedDetail.getDataType().equals("EpicVisitType")) {
					buf.append("=\"");
					buf.append(((EpicVisitType) field.get(pv)).getId());
					buf.append('\"');
				}
				buf.append(',');
			}
		}
		log.debug("getPatientVisitFields=="+buf);
		return buf;
	}
}
