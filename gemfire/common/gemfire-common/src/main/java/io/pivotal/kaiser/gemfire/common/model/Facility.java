package io.pivotal.kaiser.gemfire.common.model;



import java.math.BigInteger;

public class Facility {

	String facilityCode;
	String medicalCenterCode;
	String LMACode;
	String regionCode;
	String facilityName;
	String webSiteUrl;
	BigInteger addressId;

	public String getFacilityCode() {
		return facilityCode;
	}

	public void setFacilityCode(String facilityCode) {
		this.facilityCode = facilityCode;
	}

	public String getMedicalCenterCode() {
		return medicalCenterCode;
	}

	public void setMedicalCenterCode(String medicalCenterCode) {
		this.medicalCenterCode = medicalCenterCode;
	}

	public String getLMACode() {
		return LMACode;
	}

	public void setLMACode(String lMACode) {
		LMACode = lMACode;
	}

	public String getRegionCode() {
		return regionCode;
	}

	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}

	public String getFacilityName() {
		return facilityName;
	}

	public void setFacilityName(String facilityName) {
		this.facilityName = facilityName;
	}

	public String getWebSiteUrl() {
		return webSiteUrl;
	}

	public void setWebSiteUrl(String webSiteUrl) {
		this.webSiteUrl = webSiteUrl;
	}

	public BigInteger getAddressId() {
		return addressId;
	}

	public void setAddressId(BigInteger addressId) {
		this.addressId = addressId;
	}



}
