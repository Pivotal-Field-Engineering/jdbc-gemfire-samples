CREATE SCHEMA dbo DEFAULT SERVER GROUPS (CACHE);

CREATE DISKSTORE general ('./general');

----------- procedures ------------
    
CREATE PROCEDURE dbo.bulkloader (
    in schemaName varchar(100), in tableName varchar(100))
    language java parameter style java modifies sql data
    external name 'com.kp.ttg.BulkLoader.load';
    
CREATE PROCEDURE dbo.csvloader (
    in filePath varchar(200), in schemaName varchar(100), in tableName varchar(100))
    language java parameter style java modifies sql data
    external name 'com.kp.ttg.CSVFileLoader.load';

CREATE PROCEDURE dbo.updateappt (
    in oper varchar(30) ,
    in data varchar(30000),
    out retcode integer ,
    out retmsg varchar(200))
    language java parameter style java modifies sql data
    external name 'com.kp.ttg.UpdateAppt.updateAppointment';

----------- member data from SQLServer; partition ------------

CREATE TABLE dbo.member (
    region_code varchar(50) NULL,
    guid bigint NULL,
    uid varchar(50) NULL,  
    mrn char(10) NOT NULL,
    mrn_prefix char(2) NOT NULL,
    first_name varchar(50) NULL,
    last_name varchar(50) NULL,
    middle_name varchar(50) NULL,
    display_name varchar(50) NULL,
    gender char(1) NULL,
    birth_date timestamp NOT NULL,
    death_flag char(1) NULL,
    death_date timestamp NULL,
    email_address varchar(50) NULL,
    blind_flag char(1) NULL,
    deaf_flag char(1) NULL,
    mute_flag char(1) NULL,
    kpmcp_emp_flag char(1) NULL,
    intrptr_req_code char(1) NULL,
    invalid_mrn_code varchar(50) NULL,
    mrn_replaced_by varchar(50) NULL,
    last_rev_datetime timestamp NULL,
    last_verify_date timestamp NULL,
    update_datetime timestamp NULL,
    update_flag char(1) NULL,
    suffix char(5) NULL,
    PRIMARY KEY (mrn,mrn_prefix))
    PARTITION BY COLUMN (mrn,mrn_prefix)
    REDUNDANCY 1
    EVICTION BY LRUHEAPPERCENT EVICTACTION OVERFLOW
    EXPIRE ENTRY WITH TIMETOLIVE 14395 ACTION DESTROY
    PERSISTENT 'general' SYNCHRONOUS;

CALL sys.attach_loader('dbo','member','com.kp.ttg.MemberRowLoader','');

CREATE TABLE dbo.member_phone (
    uid bigint NOT NULL,
    member_id bigint NULL,
    mrn char(10) NOT NULL,
    mrn_prefix char(2) NOT NULL,
    member_phone_type_code varchar(2) NULL,
    member_phone_loc_code varchar(2) NULL,
    member_phone_area_code varchar(3) NULL,
    member_phone_number_prefix varchar(3) NULL,
    member_phone_number_suffix varchar(4) NULL,
    member_phone_number_xtn varchar(6) NULL,
    member_phone_sts_code varchar(1) NULL,
    entry_id varchar(10) NULL,
    entry_datetime timestamp NULL,
    update_id varchar(10) NULL,
    update_datetime timestamp NULL,
    ttg_update_datetime timestamp NULL,
    active_flag varchar(1) NULL,
    PRIMARY KEY (mrn,mrn_prefix))
    PARTITION BY COLUMN (mrn,mrn_prefix)
    COLOCATE WITH (dbo.member)
    REDUNDANCY 1
    EVICTION BY LRUHEAPPERCENT EVICTACTION OVERFLOW
    EXPIRE ENTRY WITH TIMETOLIVE 14400 ACTION DESTROY
    PERSISTENT 'general' SYNCHRONOUS;

CALL sys.attach_loader('dbo','member_phone','com.kp.ttg.SimpleRowLoader','');
    
----------- reference data from SQLServer; replicate ------------
 
CREATE TABLE dbo.ADDRESS (
    ADDRESS_ID bigint NOT NULL,
    STREET_NAME1 varchar(128) NULL,
    STREET_NAME2 varchar(128) NULL,
    CITY varchar(50) NULL,
    STATE char(2) NULL,
    ZIP_CODE char(10) NULL,
    ZIP_CODE_SUFFIX char(4) NULL,
    LATITUDE decimal(10, 6) NULL,
    LONGITUDE decimal(10, 6) NULL,
    COUNTRY varchar(50) NULL,
    ADDITIONAL_INFO varchar(50) NULL,
    LOCATION varchar(60) NULL,
    PRIMARY KEY (ADDRESS_ID));

CALL sys.attach_loader('dbo','ADDRESS','com.kp.ttg.SimpleRowLoader','');

CREATE TABLE dbo.BOARD_CERTIFICATION (
    BOARD_ID bigint NOT NULL,
    BOARD_NAME varchar(75) NULL,
    BOARD_X12_NAME varchar(50) NULL,
    PRIMARY KEY (BOARD_ID));

CALL sys.attach_loader('dbo','BOARD_CERTIFICATION','com.kp.ttg.SimpleRowLoader','');

CREATE TABLE dbo.DEPARTMENT (
    DEPARTMENT_CODE char(3) NOT NULL,
    FACILITY_CODE char(3) NOT NULL,
    MEDICAL_CENTER_CODE char(3) NOT NULL,
    LMA_CODE char(4) NOT NULL,
    REGION_CODE char(4) NOT NULL,
    DEPARTMENT_NAME varchar(50) NOT NULL,
    DEPARTMENT_DESCRIPTION varchar(50) NULL,
    WEB_SITE_URL varchar(100) NULL,
    ADDRESS_ID bigint NOT NULL,
    DEPARTMENT_MANAGER char(7) NULL,
    DEPARTMENT_CHIEF char(7) NULL,
    PRIMARY KEY (DEPARTMENT_CODE,FACILITY_CODE,MEDICAL_CENTER_CODE,LMA_CODE,REGION_CODE));

CALL sys.attach_loader('dbo','DEPARTMENT','com.kp.ttg.SimpleRowLoader','');

CREATE TABLE dbo.DEPARTMENT_PHONE (
    PHONE_TYPE_ID bigint NOT NULL,
    DEPARTMENT_CODE char(3) NOT NULL,
    PHONE_NUMBER varchar(10) NOT NULL,
    PRIMARY KEY (PHONE_TYPE_ID,DEPARTMENT_CODE));

CALL sys.attach_loader('dbo','DEPARTMENT_PHONE','com.kp.ttg.SimpleRowLoader','');

CREATE TABLE dbo.FACILITY (
    FACILITY_CODE char(3) NOT NULL,
    MEDICAL_CENTER_CODE char(3) NOT NULL,
    LMA_CODE char(4) NOT NULL,
    REGION_CODE char(4) NOT NULL,
    FACILITY_NAME varchar(50) NOT NULL,
    WEB_SITE_URL varchar(100) NULL,
    ADDRESS_ID bigint NOT NULL,
    PRIMARY KEY (FACILITY_CODE,MEDICAL_CENTER_CODE,LMA_CODE,REGION_CODE));

CALL sys.attach_loader('dbo','FACILITY','com.kp.ttg.SimpleRowLoader','');

CREATE TABLE dbo.LANGUAGES (
    LANGUAGE_ID bigint NOT NULL,
    LANGUAGE_NAME varchar(50) NOT NULL,
    PRIMARY KEY (LANGUAGE_ID));

CALL sys.attach_loader('dbo','LANGUAGES','com.kp.ttg.SimpleRowLoader','');

CREATE TABLE dbo.LOCAL_MARKET_AREA (
    LMA_CODE char(4) NOT NULL,
    REGION_CODE char(4) NOT NULL,
    LMA_NAME varchar(50) NOT NULL,
    PRIMARY KEY (LMA_CODE,REGION_CODE));

CALL sys.attach_loader('dbo','LOCAL_MARKET_AREA','com.kp.ttg.SimpleRowLoader','');

CREATE TABLE dbo.LOCATION (
    LOCATION_ID bigint NOT NULL,
    ADDRESS_ID bigint NOT NULL,
    BUILDING_NAME varchar(50) NOT NULL,
    PRIMARY KEY (LOCATION_ID,ADDRESS_ID));

CALL sys.attach_loader('dbo','LOCATION','com.kp.ttg.SimpleRowLoader','');

CREATE TABLE dbo.MEDICAL_CENTER (
    MEDICAL_CENTER_CODE char(3) NOT NULL,
    LMA_CODE char(4) NOT NULL,
    REGION_CODE char(4) NOT NULL,
    MEDICAL_CENTER_NAME varchar(50) NOT NULL,
    NUID_PIC_1 varchar(7) NULL,
    NUID_PIC_2 varchar(7) NULL,
    NUID_APIC_TECH varchar(7) NULL,
    NUID_APIC_HP varchar(7) NULL,
    NUID_APIC_ACCESS varchar(7) NULL,
    WEB_SITE_URL varchar(100) NULL,
    NUID_MGA varchar(7) NULL,
    NUID_AMGA varchar(7) NULL,
    ADDRESS_ID bigint NOT NULL,
    PRIMARY KEY (MEDICAL_CENTER_CODE,LMA_CODE,REGION_CODE));

CALL sys.attach_loader('dbo','MEDICAL_CENTER','com.kp.ttg.SimpleRowLoader','');

CREATE TABLE dbo.PHARMACY (
    PHARMACY_CODE char(4) NOT NULL,
    FACILITY_CODE char(3) NOT NULL,
    MEDICAL_CENTER_CODE char(3) NOT NULL,
    LMA_CODE char(4) NOT NULL,
    REGION_CODE char(4) NOT NULL,
    PHARMACY_NAME varchar(50) NOT NULL,
    PHARMACY_PHONE varchar(10) NOT NULL,
    ADDRESS_ID bigint NOT NULL,
    PRIMARY KEY (PHARMACY_CODE,FACILITY_CODE,MEDICAL_CENTER_CODE,LMA_CODE,REGION_CODE));

CALL sys.attach_loader('dbo','PHARMACY','com.kp.ttg.SimpleRowLoader','');

CREATE TABLE dbo.PHONE_TYPE (
    PHONE_TYPE_ID bigint NOT NULL,
    PHONE_TYPE_CODE char(4) NOT NULL,
    PHONE_TYPE_DESCRIPTION varchar(50) NOT NULL,
    PRIMARY KEY (PHONE_TYPE_ID));

CALL sys.attach_loader('dbo','PHONE_TYPE','com.kp.ttg.SimpleRowLoader','');

CREATE TABLE dbo.PROVIDER (
    NUID char(7) NOT NULL,
    RESOURCE_ID char(7) NOT NULL,
    PRIMARY_CARE_INDICATOR char(1) NOT NULL,
    PROVIDER_ID char(9) NULL,
    PROF_TITLE char(9) NULL,
    NPI char(10) NULL,
    SELF_REPORTED_HIRE_DATE timestamp NULL,
    ACCEPTS_SECURE_MESSAGE char(1) NOT NULL,
    PHILOSOPHY_TEXT varchar(4000) NULL,
    HONORARIA_TEXT varchar(4000) NULL,
    PRIMARY KEY (NUID));

CALL sys.attach_loader('dbo','PROVIDER','com.kp.ttg.SimpleRowLoader','');

CREATE TABLE dbo.PROVIDERS_HAVING_HOME_PAGE (
    NUID char(7) NOT NULL,
    PRIMARY KEY (NUID));

CALL sys.attach_loader('dbo','PROVIDERS_HAVING_HOME_PAGE','com.kp.ttg.SimpleRowLoader','');

CREATE TABLE dbo.PROVIDER_CREDENTIAL (
    NUID char(7) NOT NULL,
    SEQ_NO bigint NOT NULL,
    BOARD_NAME varchar(75) NULL,
    BOARD_X12_NAME varchar(50) NULL,
    CRED_ORDER int NULL,
    CREDENTIAL_NAME varchar(35) NULL,
    CREDENTIAL_YEAR char(20) NULL,
    CREDENTIAL_CODE char(20) NULL,
    CREDENTIAL_SPECIALTY varchar(20) NULL,
    CREDENTIAL_RECERTIFICATION_YEAR char(4) NULL,
    CREDENTIAL_EXPIRATION_DATE char(10) NULL,
    CREDENTIAL_TYPE varchar(20) NULL,
    PRIMARY KEY (NUID,SEQ_NO));

CALL sys.attach_loader('dbo','PROVIDER_CREDENTIAL','com.kp.ttg.SimpleRowLoader','');

CREATE TABLE dbo.PROVIDER_EDUCATION (
    NUID char(7) NOT NULL,
    SEQ_NO bigint NOT NULL,
    SCHOOL_ID char(10) NOT NULL,
    SRC_SYS_ID char(2) NOT NULL,
    EDU_ORDER varchar(20) NULL,
    DEGREE_EARNED varchar(25) NULL,
    SCHOOL_SPECIALTY varchar(50) NULL,
    YEAR_DEGREE_RECEIVED char(4) NULL,
    EDUCATION_TYPE varchar(25) NULL,
    PRIMARY KEY (NUID,SEQ_NO,SCHOOL_ID,SRC_SYS_ID));

CALL sys.attach_loader('dbo','PROVIDER_EDUCATION','com.kp.ttg.SimpleRowLoader','');

CREATE TABLE dbo.PROVIDER_LANGUAGE (
    NUID char(7) NOT NULL,
    LANGUAGE_ID bigint NOT NULL,
    SPEAK_INDICATOR char(1) NULL,
    WRITE_INDICATOR char(1) NULL,
    SELF_REPORTED_INDICATOR char(1) NULL,
    PRIMARY KEY (NUID,LANGUAGE_ID));

CALL sys.attach_loader('dbo','PROVIDER_LANGUAGE','com.kp.ttg.SimpleRowLoader','');

CREATE TABLE dbo.PROVIDER_LICENSE (
    NUID char(7) NOT NULL,
    LICENSE_NUMBER varchar(10) NOT NULL,
    LICENSE_TYPE varchar(7) NOT NULL,
    LICENSE_STATE varchar(2) NULL,
    GRANTING_BODY varchar(2) NULL,
    LICENSE_ISSUE_DATE timestamp NULL,
    LICENSE_EXPIRY_DATE timestamp NULL,
    LICENSE_STATUS char(1) NULL,
    AUTH_LICENSE_IND char(1) NULL,
    DISPLAY_LICENSE_ID varchar(10) NULL,
    PRIMARY KEY (NUID,LICENSE_NUMBER,LICENSE_TYPE));

CALL sys.attach_loader('dbo','PROVIDER_LICENSE','com.kp.ttg.SimpleRowLoader','');

CREATE TABLE dbo.PROVIDER_LOCATION (
    PROVIDER_LOCATION_ID bigint NOT NULL,
    NUID char(7) NOT NULL,
    SUB_DEPARTMENT_CODE char(4) NOT NULL,
    DEPARTMENT_CODE char(3) NOT NULL,
    FACILITY_CODE char(3) NOT NULL,
    MEDICAL_CENTER_CODE char(3) NULL,
    LMA_CODE char(4) NULL,
    REGION_CODE char(4) NOT NULL,
    PRIMARY_INDICATOR char(1) NOT NULL,
    SOURCE_FLAG char(4) NULL,
    PROVIDER_CERTIFIED char(1) NOT NULL,
    PRIMARY KEY (PROVIDER_LOCATION_ID));

CALL sys.attach_loader('dbo','PROVIDER_LOCATION','com.kp.ttg.SimpleRowLoader','');

CREATE TABLE dbo.PROVIDER_PANEL_ELIGIBILITY (
    NUID char(7) NOT NULL,
    REGION_CODE char(4) NOT NULL,
    FACILITY_CODE char(3) NOT NULL,
    PANEL_ELIGIBLE_INDICATOR char(1) NOT NULL,
    WTMP_INDICATOR char(1) NOT NULL,
    PANEL_STATUS char(7) NULL,
    PANEL_SIZE bigint NULL,
    SOURCE_FLAG char(4) NOT NULL,
    PRIMARY KEY (NUID,REGION_CODE,FACILITY_CODE));

CALL sys.attach_loader('dbo','PROVIDER_PANEL_ELIGIBILITY','com.kp.ttg.SimpleRowLoader','');

CREATE TABLE dbo.PROVIDER_ROLE (
    NUID char(7) NOT NULL,
    ROLE_ID bigint NOT NULL,
    DEPARTMENT_CODE char(3) NOT NULL,
    PRIMARY KEY (NUID,ROLE_ID,DEPARTMENT_CODE));

CALL sys.attach_loader('dbo','PROVIDER_ROLE','com.kp.ttg.SimpleRowLoader','');

CREATE TABLE dbo.PROVIDER_SPECIALTY (
    NUID char(7) NOT NULL,
    SPECIALTY_ID bigint NOT NULL,
    SOURCE_FLAG char(4) NULL,
    PRACTICE_INDICATOR char(1) NULL,
    PRIMARY_INDICATOR char(1) NOT NULL,
    PROVIDER_CERTIFIED char(2) NULL,
    FACILITY_CODE char(3) NOT NULL,
    MEDICAL_CENTER_CODE char(3) NOT NULL,
    LMA_CODE char(4) NOT NULL,
    REGION_CODE char(4) NOT NULL,
    PRIMARY KEY (NUID,SPECIALTY_ID,FACILITY_CODE,MEDICAL_CENTER_CODE,LMA_CODE,REGION_CODE));

CALL sys.attach_loader('dbo','PROVIDER_SPECIALTY','com.kp.ttg.SimpleRowLoader','');

CREATE TABLE dbo.PROVIDER_TYPE (
    PROVIDER_TYPE_ID bigint NOT NULL,
    PROVIDER_TYPE_NAME varchar(50) NULL,
    PROVIDER_TYPE_DESCRIPTION varchar(50) NULL,
    PRIMARY KEY (PROVIDER_TYPE_ID));

CALL sys.attach_loader('dbo','PROVIDER_TYPE','com.kp.ttg.SimpleRowLoader','');

CREATE TABLE dbo.REGION (
    REGION_CODE char(4) NOT NULL,
    REGION_NAME varchar(20) NOT NULL,
    PRIMARY KEY (REGION_CODE));

CALL sys.attach_loader('dbo','REGION','com.kp.ttg.SimpleRowLoader','');

CREATE TABLE dbo.ROLES (
    ROLE_ID bigint NOT NULL,
    ROLE_NAME varchar(50) NOT NULL,
    ROLE_DESCRIPTION varchar(100) NOT NULL,
    ROLE_LEVEL varchar(50) NOT NULL,
    PRIMARY KEY (ROLE_ID));

CALL sys.attach_loader('dbo','ROLES','com.kp.ttg.SimpleRowLoader','');

CREATE TABLE dbo.SCHOOL_UNIVERSITY (
    SCHOOL_ID char(10) NOT NULL,
    SRC_SYS_ID char(2) NOT NULL,
    SCHOOL_NAME varchar(100) NOT NULL,
    SCHOOL_PHONE varchar(20) NULL,
    ADDRESS_ID bigint NOT NULL,
    PRIMARY KEY (SCHOOL_ID,SRC_SYS_ID));

CALL sys.attach_loader('dbo','SCHOOL_UNIVERSITY','com.kp.ttg.SimpleRowLoader','');

CREATE TABLE dbo.SPECIALTY (
    SPECIALTY_ID bigint NOT NULL,
    PROVIDER_TYPE_CODE char(4) NULL,
    PROFESSIONAL_TYPE_CODE char(4) NULL,
    AREA_SPECIALTY_CODE char(10) NOT NULL,
    SPECIALTY_NAME varchar(80) NULL,
    SPECIALTY_DESCRIPTION varchar(80) NULL,
    PARENT_SPECIALTY_ID bigint NULL,
    PRIMARY KEY (SPECIALTY_ID));

CALL sys.attach_loader('dbo','SPECIALTY','com.kp.ttg.SimpleRowLoader','');

CREATE TABLE dbo.STAFF (
    NUID char(7) NOT NULL,
    FIRST_NAME varchar(50) NULL,
    MIDDLE_NAME varchar(50) NULL,
    LAST_NAME varchar(50) NOT NULL,
    HIRE_DATE timestamp NULL,
    TERMINATION_DATE timestamp NULL,
    PHOTO_WEB varchar(100) NULL,
    REHIRE_DATE timestamp NULL,
    SIGNATURE_PRINT varchar(25) NULL,
    PHOTO_PRINT varchar(50) NULL,
    HOME_PAGE_URL varchar(100) NULL,
    PHONE_TYPE_ID bigint NULL,
    GENDER char(1) NULL,
    RACF_ID char(7) NULL,
    EMAIL_ADDRESS varchar(100) NULL,
    DISPLAY_NAME varchar(50) NULL,
    NAME_SUFFIX varchar(50) NULL,
    HR_TYPE_CODE char(2) NULL,
    BUSINESS_CODE char(2) NULL,
    PRIMARY KEY (NUID));

CALL sys.attach_loader('dbo','STAFF','com.kp.ttg.SimpleRowLoader','');

CREATE TABLE dbo.SUB_DEPARTMENT (
    SUB_DEPARTMENT_CODE char(4) NOT NULL,
    DEPARTMENT_CODE char(3) NOT NULL,
    FACILITY_CODE char(3) NOT NULL,
    MEDICAL_CENTER_CODE char(3) NOT NULL,
    LMA_CODE char(4) NOT NULL,
    REGION_CODE char(4) NOT NULL,
    SUB_DEPARTMENT_NAME varchar(50) NULL,
    SUB_DEPARTMENT_TYPE char(10) NULL,
    ADDRESS_ID bigint NOT NULL,
    SUB_DEPARTMENT_SPECIALTY char(20) NULL,
    SUB_DEPT_TYPE_CODE char(1) NULL,
    BUILDING_CODE char(4) NULL,
    BUSINESS_SITE_CODE char(4) NULL,
    PRIMARY KEY (SUB_DEPARTMENT_CODE,DEPARTMENT_CODE,FACILITY_CODE,MEDICAL_CENTER_CODE,LMA_CODE,REGION_CODE));

CALL sys.attach_loader('dbo','SUB_DEPARTMENT','com.kp.ttg.SimpleRowLoader','');

CREATE TABLE dbo.ZIP4 (
    ID int NOT NULL,
    ZIP_CODE char(5) NOT NULL,
    ZIP_TYPE char(1) NOT NULL,
    CITY_NAME varchar(64) NULL,
    CITY_TYPE char(1) NULL,
    STATE_ABBR char(10) NULL,
    STATE_NAME varchar(64) NULL,
    AREA_CODE char(3) NULL,
    LATITUDE decimal(9, 6) NULL,
    LONGITUDE decimal(9, 6) NULL,
    PRIMARY KEY (ID));

CALL sys.attach_loader('dbo','ZIP4','com.kp.ttg.SimpleRowLoader','');

CREATE TABLE dbo.non_human_resource (
    resource_id varchar(7) NOT NULL,
    display_name varchar(50) NULL,
    short_name varchar(25) NULL,
    equip_category_code varchar(25) NULL,
    kpmcp_flag varchar(50) NULL,
    effective_date timestamp NULL,
    end_date timestamp NULL,
    PRIMARY KEY (resource_id));

CALL sys.attach_loader('dbo','non_human_resource','com.kp.ttg.SimpleRowLoader','');

CREATE TABLE dbo.resource_type (
    resource_type_id bigint NOT NULL,
    resource_type_code varchar(50) NULL,
    resource_type_desc varchar(50) NULL,
    PRIMARY KEY (resource_type_id));

CALL sys.attach_loader('dbo','resource_type','com.kp.ttg.SimpleRowLoader','');

CREATE TABLE dbo.TRIDION_SYNC_STATUS ( 
        SYNC_ID bigint NOT NULL, 
        ACTIVITY char(1) NOT NULL, 
        SYNC_STATUS char(1) NOT NULL, 
        ENTITY varchar(100) NOT NULL, 
        ENTITY_ID varchar(50) NOT NULL, 
        UPDATE_TIMESTAMP timestamp NOT NULL, 
        LEVEL_1_ENTITY varchar(100) NULL, 
        LEVEL_2_ENTITY varchar(100) NULL, 
        LEVEL_3_ENTITY varchar(100) NULL, 
        LEVEL_1_ENTITY_ID varchar(50) NULL, 
        LEVEL_2_ENTITY_ID varchar(50) NULL, 
        LEVEL_3_ENTITY_ID varchar(50) NULL, 
        PROVISIONED_IN_TRIDION char(1) NOT NULL, 
        REGION_CODE char(4) NOT NULL, 
        LEVEL_4_ENTITY_ID varchar(50) NULL, 
        LEVEL_4_ENTITY varchar(100) NULL, 
        PHOTO_SYNC char(1) NULL, 
        PROVISIONING_IN_PROGRESS char(1) NULL, 
        UNPUBLISHED char(10) NULL, 
        UNPUBLISHED_TIMESTAMP timestamp NULL, 
        TERMINATED char(1) NULL, 
        TERMINATED_TIMESTAMP timestamp NULL,
        PRIMARY KEY (SYNC_ID));
        
CALL sys.attach_loader('dbo','TRIDION_SYNC_STATUS','com.kp.ttg.SimpleRowLoader','');

CREATE TABLE dbo.TRIDION_CONF_STATUS (
	CONF_ID bigint NOT NULL,
	NUID char(7) NOT NULL,
	CONF_URL varchar(1000) NOT NULL,
	UPDATE_DT timestamp NULL,
	PRIMARY KEY (CONF_ID));
        
CALL sys.attach_loader('dbo','TRIDION_CONF_STATUS','com.kp.ttg.SimpleRowLoader','');

CREATE TABLE dbo.BUILDING ( 
        BUILDING_CODE varchar(4) NOT NULL, 
        BUILDING_NAME varchar(50) NULL, 
        BUILDING_STREET1 varchar(50) NULL, 
        BUILDING_STREET2 varchar(50) NULL, 
        BUILDING_CITY varchar(50) NULL, 
        BUILDING_STATE varchar(2) NULL, 
        BUILDING_ZIPCODE char(5) NULL, 
        BUILDING_ZIPCODE_SUFFIX char(4) NULL, 
        FACILITY_CODE char(3) NOT NULL, 
        REGION_CODE char(4) NOT NULL, 
        PRIMARY KEY (BUILDING_CODE,FACILITY_CODE,REGION_CODE));
        
CALL sys.attach_loader('dbo','BUILDING','com.kp.ttg.SimpleRowLoader','');

CREATE TABLE dbo.BUSINESS_SITE ( 
        BUSINESS_SITE_CODE char(4) NOT NULL, 
        FACILITY_CODE char(3) NOT NULL, 
        BUSINESS_SITE_NAME char(50) NULL, 
        BUSINESS_SITE_DESCIPTION char(75) NULL, 
        STREET1 char(50) NULL, 
        STREET2 char(50) NULL, 
        CITY char(30) NULL, 
        STATE char(2) NULL, 
        ZIP_CODE char(5) NULL, 
        ZIP_CODE_SUFFIX char(4) NULL, 
        BUSINESS_SITE_PHONE_AREA char(3) NULL, 
        BUSINESS_SITE_PHONE_PREFIX char(3) NULL, 
        BUSINESS_SITE_PHONE_SUFFIX char(4) NULL, 
        AMR_ORG_TYPE_CD char(4) NULL, 
        PARENT_BUSINESS_SITE char(4) NULL, 
        REGION_CODE char(4) NOT NULL, 
        PRIMARY KEY (BUSINESS_SITE_CODE,FACILITY_CODE,REGION_CODE));
        
CALL sys.attach_loader('dbo','BUSINESS_SITE','com.kp.ttg.SimpleRowLoader','');
