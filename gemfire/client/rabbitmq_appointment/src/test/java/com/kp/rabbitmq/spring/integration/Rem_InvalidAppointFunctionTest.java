package com.kp.rabbitmq.spring.integration;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import com.kp.org.parrs.model.AppointmentResource;
import com.kp.org.parrs.model.AppointmentSchedule;
import com.kp.org.parrs.model.MessageHeader;
import com.kp.org.parrs.model.Notes;
import com.kp.org.parrs.model.Parrs;
import com.kp.org.parrs.model.Patient;
import com.kp.org.parrs.model.PatientVisit;
import com.kp.org.parrs.model.ResourceType;
import com.kp.rabbitmq.AppointmentEventsApplication;
import com.kp.rabbitmq.util.GemfireWriter;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=AppointmentEventsApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)

public class Rem_InvalidAppointFunctionTest {
	@Autowired
	GemfireWriter writer;
	
	@Test
	public void testExecute() {
		Parrs p = new Parrs();
		AppointmentResource appointmentResource = new AppointmentResource();
		List<ResourceType> types = new ArrayList<>();
		ResourceType resourceType = new ResourceType();
		resourceType.setId("0");
		resourceType.setDescription("TPMG ID");
		resourceType.setType("String");
		types.add(resourceType);
		appointmentResource.setResourceTypes(types);
		AppointmentSchedule appointmentSchedule = new AppointmentSchedule();
		MessageHeader messageHeader = new MessageHeader();
		Patient patient = new Patient();
		Notes notes = new Notes();
		PatientVisit patientVisit = new PatientVisit();

		appointmentSchedule.setAppointmentId("1");
		patient.setPatientVisit(patientVisit);
		p.setAppointmentResource(appointmentResource);
		p.setAppointmentSchedule(appointmentSchedule);
		p.setMessageHeader(messageHeader);
		p.setNotes(notes);
		p.setPatient(patient);		
	}
}
