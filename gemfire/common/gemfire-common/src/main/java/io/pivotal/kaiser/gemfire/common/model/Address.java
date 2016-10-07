package io.pivotal.kaiser.gemfire.common.model;



import java.math.BigDecimal;
import java.math.BigInteger;


public class Address {
	BigInteger addressId; //key
	String streetName1;
	String streetName2;
	String city;
	String state;
	String zipCode;
	String zipCodeSuffix;
	BigDecimal latitude;
	BigDecimal longitude;
	String country;
	String additionalInfo;
	String location;

	public BigInteger getAddressId() {
		return addressId;
	}

	public void setAddressId(BigInteger addressId) {
		this.addressId = addressId;
	}

	public String getStreetName1() {
		return streetName1;
	}

	public void setStreetName1(String streetName1) {
		this.streetName1 = streetName1;
	}

	public String getStreetName2() {
		return streetName2;
	}

	public void setStreetName2(String streetName2) {
		this.streetName2 = streetName2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getZipCodeSuffix() {
		return zipCodeSuffix;
	}

	public void setZipCodeSuffix(String zipCodeSuffix) {
		this.zipCodeSuffix = zipCodeSuffix;
	}

	public BigDecimal getLatitude() {
		return latitude;
	}

	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}

	public BigDecimal getLongitude() {
		return longitude;
	}

	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

}
