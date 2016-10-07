CREATE GATEWAYRECEIVER receiver (
    STARTPORT 5550
    ENDPORT   5559
    )
    SERVER GROUPS (CACHE);

CREATE TABLE dbo.appointment (
    appointment_id varchar(50) NOT NULL,
    cadence_appt_id varchar(50) NULL,
    cadence_appt_type varchar(10) NULL,
    region_code varchar(50) NOT NULL , 
    mrn char(10) NOT NULL,
    mrn_prefix char(2) NOT NULL,
    facility_code char(3) NOT NULL,
    clinic_code char(3) NOT NULL,
    clinic_name varchar(100) NULL,
    admin_dept_code char(3) NOT NULL,
    sub_dept_code varchar(4) NOT NULL,
    appt_datetime timestamp NOT NULL,
    appt_begin_datetime timestamp NOT NULL,
    appt_day char(3) NULL,
    appt_duration varchar(50) NOT NULL,
    appt_duration_unit varchar(50) NOT NULL,
    booking_method_code varchar(10) NOT NULL,
    activity_code varchar(10) NOT NULL,
    availability_code varchar(10) NOT NULL,
    booked_by_racf_id varchar(50) NOT NULL,
    appt_type_code varchar(10)  NULL,
    epic_visit_type_code varchar(10) NOT NULL,
    booking_guideline_id varchar(25) NULL,
    appt_resource_id varchar(50) NOT NULL,
    resource_type_code char(1) NOT NULL,
    ref_resource_id varchar(50)  NULL,
    member_class_code char(1) NOT NULL,
    secure_code varchar(5) NULL,
    appt_status_code varchar(5) NULL,
    appt_reason_desc varchar(50)  NULL,
    appt_cancel_timestamp timestamp NULL,
    appt_cancellation_code char(2) NULL,
    appt_cancel_reason_desc varchar(50) NULL,
    appt_routing_address varchar(100) NULL,
    referring_fac_code varchar(3) NULL,
    patient_facility_code varchar(3) NULL,
    patient_sub_dept_code varchar(4) NULL,
    reminder_pref_code char(1) NULL,
    xray_request_flag char(1) NULL,
    chart_request_flag char(1) NULL,
    medical_record_flag char(1) NULL,
    explicitly_linked_flag char(1) NULL,
    implicitly_linked_flag char(1) NULL,
    send_confirm_notice_flag char(1) NULL,
    schedulable_flag char(1) NULL,
    reschedulable_flag char(1) NULL,
    cancellable_flag char(1) NULL,
    inquirable_flag char(1) NULL,
    note varchar(100) NULL,
    instructions varchar(100) NULL,
    source_queue_id varchar(250) NULL,
    sending_application varchar(25) NULL,
    receiving_application varchar(25) NULL,
    message_datetime timestamp NULL,
    message_type_code varchar(35) NULL,
    create_timestamp timestamp NULL,
    update_timestamp timestamp NULL,
    entry_date timestamp NOT NULL,
    create_id varchar(50) NULL,
    update_id varchar(50) NULL,
    created_by varchar(25) NULL, 
    updated_by varchar(25) NULL,
    event_datetime timestamp NOT NULL,
    PRIMARY KEY (appointment_id))
    PARTITION BY COLUMN (mrn,mrn_prefix)
    COLOCATE WITH (dbo.member)
    REDUNDANCY 1
    EVICTION BY LRUHEAPPERCENT EVICTACTION OVERFLOW
    PERSISTENT 'general' SYNCHRONOUS;
    

CREATE INDEX NDX_APPOINTMENT_appt_datetime ON dbo.appointment (appt_datetime);

CREATE INDEX NDX_APPOINTMENT_mrn_mrn_prefix ON dbo.appointment (mrn,mrn_prefix);
