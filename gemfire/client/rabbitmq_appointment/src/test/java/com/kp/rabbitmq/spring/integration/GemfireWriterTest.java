package com.kp.rabbitmq.spring.integration;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.gemstone.gemfire.cache.Region;
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

import io.pivotal.kaiser.gemfire.common.key.AppointmentKey;
import io.pivotal.kaiser.gemfire.common.model.Appointment;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppointmentEventsApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
// @SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class GemfireWriterTest {

	@Autowired
	GemfireWriter writer;
	Parrs p = new Parrs();

	@Autowired
	Region<AppointmentKey, Appointment>region;
	
	AppointmentKey appKey = new AppointmentKey();
	
	@Before
	public void init() {
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

		patient.setPatientVisit(patientVisit);
		p.setAppointmentResource(appointmentResource);
		p.setAppointmentSchedule(appointmentSchedule);
		p.setMessageHeader(messageHeader);
		p.setNotes(notes);
		p.setPatient(patient);
		
		
	}
	
	@After
	public void postTest() {
		region.remove(appKey);
	}

	@Test
	public void testInsert() {
		p.getAppointmentSchedule().setAppointmentId("1000");
		p.getMessageHeader().setMessageType("S12");
		appKey.setAppointmentId("1000");
		
		int code = writer.write(p);
		assertTrue(code == 0);
	}
	
	@Test
	public void testUpdate() {
		p.getAppointmentSchedule().setAppointmentId("2");
		p.getMessageHeader().setMessageType("S13"); 
		appKey.setAppointmentId("2");


		int code = writer.write(p);
		assertTrue(code == 0);
		
		AppointmentSchedule appointmentSchedule = new AppointmentSchedule();
		appointmentSchedule.setAppointmentId("2");
		p.setAppointmentSchedule(appointmentSchedule);

		code = writer.write(p);
		assertTrue(code == 0);
	}
}
