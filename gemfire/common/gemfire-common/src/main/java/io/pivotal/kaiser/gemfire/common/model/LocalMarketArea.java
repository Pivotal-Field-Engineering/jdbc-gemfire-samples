package io.pivotal.kaiser.gemfire.common.model;

public class LocalMarketArea {

	String LMACode;
	String regionCode;
	String LMAName;

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

	public String getLMAName() {
		return LMAName;
	}

	public void setLMAName(String lMAName) {
		LMAName = lMAName;
	}

}
