package io.pivotal.spring.gemfire.sink.custom;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties
public class GemfireSinkProtoProperties {
	private String regionName;
	
	@Value("${locators:localhost:10334}")
	public String locators;

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public String getLocators() {
		return locators;
	}

	public void setLocators(String locators) {
		this.locators = locators;
	}
}
