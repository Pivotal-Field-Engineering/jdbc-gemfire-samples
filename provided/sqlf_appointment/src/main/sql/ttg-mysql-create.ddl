CREATE SCHEMA dbo;
CREATE SCHEMA PDMACC;

CREATE TABLE PDMACC.TPDM_PATDEM
(
   MRN_PRFX_ID char(2) NOT NULL,
   MRN char(10) NOT NULL,
   TERM_DIGIT char(2) NOT NULL,
   PATIENT_NAME char(30) NOT NULL,
   PHONE1_AREA_CODE char(3),
   PHONE1_NUMBER char(7),
   PHONE1_EXTENSION char(6),
   PHONE1_TTY_IND char(1),
   PHONE1_LOCATION char(1),
   PHONE2_AREA_CODE char(3),
   PHONE2_NUMBER char(7),
   PHONE2_EXTENSION char(6),
   PHONE2_TTY_IND char(1),
   PHONE2_LOCATION char(1),
   STREET_ADDR char(24) NOT NULL,
   CITY_ADDR char(24) NOT NULL,
   STATE_ADDR char(2) NOT NULL,
   ZIP_ADDR char(5) NOT NULL,
   ZIP_SUFFIX char(4),
   CONTACT_NAME char(30),
   CONTACT_RELATION char(1),
   DEAF_IND char(1),
   BLIND_IND char(1),
   MUTE_IND char(1),
   PAT_SEX char(1) NOT NULL,
   BIRTH_DATE date NOT NULL,
   DEATH_IND char(1),
   KPMCP_EMP_IND char(1),
   MED_AWARD_IND char(1),
   LAST_REVISED_DATE date,
   LAST_REVISED_TIME time,
   LAST_REVISED_BY char(8),
   LAST_REVISED_INIT char(3),
   LAST_VERF_DATE date,
   LAST_VERF_BY char(8),
   LAST_VERF_INITLS char(3),
   PREFRD_LANG1_SPOKN char(10),
   PREFRD_LANG2_SPOKN char(10),
   PREFRD_LANG1_WRITN char(10),
   INTRPTR_REQ_CODE char(1),
   INVAL_MRN_CD char(1),
   MRN_RPLCD_BY_PRFX char(2),
   MRN_REPLACED_BY char(10),
   DEATH_DATE date,
   UPDATE_ID char(4),
   NTNL_ID char(12),
   PRIMARY KEY (MRN_PRFX_ID,MRN)
)
;

CREATE TABLE PDMACC.TPDM_EMAIL_ADDR
(
   MRN_PRFX_ID char(2) NOT NULL,
   MRN char(10) NOT NULL,
   CNTCT_CD char(1) NOT NULL,
   USE_TP_CD char(1) NOT NULL,
   ENTRY_TS timestamp NOT NULL,
   EMAIL_ADDR char(50) NOT NULL,
   CMNT char(60),
   ENTRY_ID char(8) NOT NULL,
   ENTRY_INIT char(3),
   UPDT_TS timestamp,
   UPDT_ID char(8),
   UPDT_INIT char(3),
   MBR_EMAIL_VLDT_CD char(1) DEFAULT 'G' NOT NULL,
   PRIMARY KEY (MRN_PRFX_ID,MRN,CNTCT_CD,USE_TP_CD,ENTRY_TS)
)
;

CREATE TABLE PDMACC.TPDM_MBR_PHONE
(
   MRN_PRFX_ID char(2) NOT NULL,
   MRN char(10) NOT NULL,
   MBR_PHN_TYP_CD char(2) NOT NULL,
   MBR_PHN_LOC_CD char(2) NOT NULL,
   MBR_PHN_AREA_CD char(3) NOT NULL,
   MBR_PHN_PFX_NB char(3) NOT NULL,
   MBR_PHN_SFX_NB char(4),
   MBR_PHN_EXT_NB char(6),
   MBR_PHN_STS_CD char(1) DEFAULT 'G' NOT NULL,
   ENTRY_ID char(8) NOT NULL,
   ENTRY_TS timestamp NOT NULL,
   UPDT_ID char(8),
   UPDT_TS timestamp
)
;

CREATE TABLE dbo.STAFF(
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

CREATE TABLE dbo.PROVIDER(
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

CREATE TABLE dbo.FACILITY(
        FACILITY_CODE char(3) NOT NULL,
        MEDICAL_CENTER_CODE char(3) NOT NULL,
        LMA_CODE char(4) NOT NULL,
        REGION_CODE char(4) NOT NULL,
        FACILITY_NAME varchar(50) NOT NULL,
        WEB_SITE_URL varchar(100) NULL,
        ADDRESS_ID bigint NOT NULL,
    PRIMARY KEY (FACILITY_CODE,MEDICAL_CENTER_CODE,LMA_CODE,REGION_CODE));

CREATE TABLE dbo.DEPARTMENT(
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

CREATE TABLE dbo.SUB_DEPARTMENT(
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
    PRIMARY KEY (SUB_DEPARTMENT_CODE,DEPARTMENT_CODE,FACILITY_CODE,MEDICAL_CENTER_CODE,LMA_CODE,REGION_CODE));

CREATE TABLE dbo.PROVIDER_CREDENTIAL(
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

CREATE TABLE dbo.PROVIDER_EDUCATION(
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

CREATE TABLE dbo.PROVIDER_LANGUAGE(
        NUID char(7) NOT NULL,
        LANGUAGE_ID bigint NOT NULL,
        SPEAK_INDICATOR char(1) NULL,
        WRITE_INDICATOR char(1) NULL,
        SELF_REPORTED_INDICATOR char(1) NULL,
    PRIMARY KEY (NUID,LANGUAGE_ID));

CREATE TABLE dbo.PROVIDER_LICENSE(
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

CREATE TABLE dbo.PROVIDER_LOCATION(
        NUID char(7) NOT NULL,
        SUB_DEPARTMENT_CODE char(4) NULL,
        DEPARTMENT_CODE char(3) NULL,
        FACILITY_CODE char(3) NOT NULL,
        MEDICAL_CENTER_CODE char(3) NULL,
        LMA_CODE char(4) NULL,
        REGION_CODE char(4) NOT NULL,
        PRIMARY_INDICATOR char(1) NOT NULL,
        SOURCE_FLAG char(4) NULL,
        PROVIDER_CERTIFIED char(1) NOT NULL,
        PRIMARY KEY (NUID));

CREATE TABLE dbo.PROVIDER_PANEL_ELIGIBILITY(
        NUID char(7) NOT NULL,
        REGION_CODE char(4) NOT NULL,
        FACILITY_CODE char(3) NOT NULL,
        PANEL_ELIGIBLE_INDICATOR char(1) NOT NULL,
        WTMP_INDICATOR char(1) NOT NULL,
        PANEL_STATUS char(7) NULL,
        PANEL_SIZE bigint NULL,
        SOURCE_FLAG char(4) NOT NULL,
    PRIMARY KEY (NUID,REGION_CODE,FACILITY_CODE));

CREATE TABLE dbo.PROVIDER_ROLE(
        NUID char(7) NOT NULL,
        ROLE_ID bigint NOT NULL,
        DEPARTMENT_CODE char(3) NOT NULL,
    PRIMARY KEY (NUID,ROLE_ID,DEPARTMENT_CODE));

CREATE TABLE dbo.PROVIDER_SPECIALTY(
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

CREATE TABLE dbo.PROVIDER_TYPE(
        PROVIDER_TYPE_ID bigint NOT NULL,
        PROVIDER_TYPE_NAME varchar(50) NULL,
        PROVIDER_TYPE_DESCRIPTION varchar(50) NULL,
    PRIMARY KEY (PROVIDER_TYPE_ID));

CREATE TABLE dbo.PROVIDERS_HAVING_HOME_PAGE(
        NUID char(7) NOT NULL,
    PRIMARY KEY (NUID));

CREATE TABLE dbo.ADDRESS(
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

CREATE TABLE dbo.BOARD_CERTIFICATION(
        BOARD_ID bigint NOT NULL,
        BOARD_NAME varchar(75) NULL,
        BOARD_X12_NAME varchar(50) NULL,
    PRIMARY KEY (BOARD_ID));

CREATE TABLE dbo.DEPARTMENT_PHONE(
        PHONE_TYPE_ID bigint NOT NULL,
        DEPARTMENT_CODE char(3) NOT NULL,
        PHONE_NUMBER varchar(10) NOT NULL,
    PRIMARY KEY (PHONE_TYPE_ID,DEPARTMENT_CODE));

CREATE TABLE dbo.LANGUAGES(
        LANGUAGE_ID bigint NOT NULL,
        LANGUAGE_NAME varchar(50) NOT NULL,
    PRIMARY KEY (LANGUAGE_ID));

CREATE TABLE dbo.LOCAL_MARKET_AREA(
        LMA_CODE char(4) NOT NULL,
        REGION_CODE char(4) NOT NULL,
        LMA_NAME varchar(50) NOT NULL,
    PRIMARY KEY (LMA_CODE,REGION_CODE));

CREATE TABLE dbo.LOCATION(
        LOCATION_ID bigint NOT NULL,
        ADDRESS_ID bigint NOT NULL,
        BUILDING_NAME varchar(50) NOT NULL,
    PRIMARY KEY (LOCATION_ID,ADDRESS_ID));

CREATE TABLE dbo.MEDICAL_CENTER(
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

CREATE TABLE dbo.PHARMACY(
        PHARMACY_CODE char(4) NOT NULL,
        FACILITY_CODE char(3) NOT NULL,
        MEDICAL_CENTER_CODE char(3) NOT NULL,
        LMA_CODE char(4) NOT NULL,
        REGION_CODE char(4) NOT NULL,
        PHARMACY_NAME varchar(50) NOT NULL,
        PHARMACY_PHONE varchar(10) NOT NULL,
        ADDRESS_ID bigint NOT NULL,
    PRIMARY KEY (PHARMACY_CODE,FACILITY_CODE,MEDICAL_CENTER_CODE,LMA_CODE,REGION_CODE));

CREATE TABLE dbo.PHONE_TYPE(
        PHONE_TYPE_ID bigint NOT NULL,
        PHONE_TYPE_CODE char(4) NOT NULL,
        PHONE_TYPE_DESCRIPTION varchar(50) NOT NULL,
    PRIMARY KEY (PHONE_TYPE_ID));

CREATE TABLE dbo.REGION(
        REGION_CODE char(4) NOT NULL,
        REGION_NAME varchar(20) NOT NULL,
    PRIMARY KEY (REGION_CODE));

CREATE TABLE dbo.ROLES(
        ROLE_ID bigint NOT NULL,
        ROLE_NAME varchar(50) NOT NULL,
        ROLE_DESCRIPTION varchar(100) NOT NULL,
        ROLE_LEVEL varchar(50) NOT NULL,
    PRIMARY KEY (ROLE_ID));

CREATE TABLE dbo.SCHOOL_UNIVERSITY(
        SCHOOL_ID char(10) NOT NULL,
        SRC_SYS_ID char(2) NOT NULL,
        SCHOOL_NAME varchar(100) NOT NULL,
        SCHOOL_PHONE varchar(20) NULL,
        ADDRESS_ID bigint NOT NULL,
    PRIMARY KEY (SCHOOL_ID,SRC_SYS_ID));

CREATE TABLE dbo.SPECIALTY(
        SPECIALTY_ID bigint NOT NULL,
        PROVIDER_TYPE_CODE char(4) NULL,
        PROFESSIONAL_TYPE_CODE char(4) NULL,
        AREA_SPECIALTY_CODE char(10) NOT NULL,
        SPECIALTY_NAME varchar(80) NULL,
        SPECIALTY_DESCRIPTION varchar(80) NULL,
        PARENT_SPECIALTY_ID bigint NULL,
    PRIMARY KEY (SPECIALTY_ID));

CREATE TABLE dbo.ZIP4(
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

CREATE TABLE dbo.non_human_resource(
        resource_id varchar(7) NOT NULL,
        display_name varchar(50) NULL,
        short_name varchar(25) NULL,
        equip_category_code varchar(25) NULL,
        kpmcp_flag varchar(50) NULL,
        effective_date timestamp NULL,
        end_date timestamp NULL,
    PRIMARY KEY (resource_id));

CREATE TABLE dbo.non_human_resource_location(
        non_human_resource_location_id bigint NOT NULL,
        resource_id varchar(7) NULL,
        facility_code varchar(50) NULL,
        department_code varchar(50) NULL,
        sub_department_code varchar(50) NULL,
        region_code varchar(50) NULL,
        medical_center_code varchar(50) NULL,
        lma_code varchar(50) NULL,
        update_flag char(1) NULL,
        update_timestamp timestamp NULL,
    PRIMARY KEY (non_human_resource_location_id));

CREATE TABLE dbo.resource_type(
    resource_type_id bigint NOT NULL,
    resource_type_code varchar(50) NULL,
    resource_type_desc varchar(50) NULL,
    PRIMARY KEY (resource_type_id));

