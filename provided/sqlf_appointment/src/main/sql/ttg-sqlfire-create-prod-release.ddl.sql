
---- production sqlfire schema changes ----------

----- adding 2 columns in dbo.SUB_DEPARTMENT ---
ALTER TABLE dbo.SUB_DEPARTMENT
ADD COLUMN BUILDING_CODE char(4) NULL;

ALTER TABLE dbo.SUB_DEPARTMENT
ADD COLUMN BUSINESS_SITE_CODE char(4) NULL;

---- create table dbo.TRIDION_SYNC_STATUS ------------
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

---- create table dbo.TRIDION_CONF_STATUS ------------
CREATE TABLE dbo.TRIDION_CONF_STATUS (
	CONF_ID bigint NOT NULL,
	NUID char(7) NOT NULL,
	CONF_URL varchar(1000) NOT NULL,
	UPDATE_DT timestamp NULL,
	PRIMARY KEY (CONF_ID));
        
CALL sys.attach_loader('dbo','TRIDION_CONF_STATUS','com.kp.ttg.SimpleRowLoader','');

---- create table dbo.BUILDING ------------
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

---- create table dbo.BUSINESS_SITE ------------
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

---- create index ---------------------------------
--- dbo.PROVIDER_LOCATION --
CREATE INDEX NDX_PROVIDER_LOCATION_nuid_location ON dbo.PROVIDER_LOCATION (nuid, sub_department_code, department_code, facility_code, medical_center_code, lma_code, region_code, primary_indicator) 

--- dbo.PROVIDER ---
CREATE INDEX NDX_PROVIDER_resource_id ON dbo.PROVIDER (resource_id) 

--- dbo.BOARD_CERTIFICATION ---
CREATE INDEX NDX_BOARD_CERTIFICATION_board_name_board_x12_name ON dbo.BOARD_CERTIFICATION (board_name, board_x12_name) 

--- dbo.STAFF ---
CREATE INDEX NDX_STAFF_hr_type_code_business_code ON dbo.STAFF (hr_type_code, business_code)