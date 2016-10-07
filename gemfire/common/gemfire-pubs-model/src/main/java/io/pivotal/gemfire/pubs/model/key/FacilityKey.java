package io.pivotal.gemfire.pubs.model.key;



import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.gemstone.gemfire.DataSerializable;
import com.gemstone.gemfire.DataSerializer;

@SuppressWarnings("serial")
public class FacilityKey implements DataSerializable {

	String facilityCode;
	String medicalCenterCode;
	String LMACode;
	String regionCode;

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((LMACode == null) ? 0 : LMACode.hashCode());
		result = prime * result + ((facilityCode == null) ? 0 : facilityCode.hashCode());
		result = prime * result + ((medicalCenterCode == null) ? 0 : medicalCenterCode.hashCode());
		result = prime * result + ((regionCode == null) ? 0 : regionCode.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FacilityKey other = (FacilityKey) obj;
		if (LMACode == null) {
			if (other.LMACode != null)
				return false;
		} else if (!LMACode.equals(other.LMACode))
			return false;
		if (facilityCode == null) {
			if (other.facilityCode != null)
				return false;
		} else if (!facilityCode.equals(other.facilityCode))
			return false;
		if (medicalCenterCode == null) {
			if (other.medicalCenterCode != null)
				return false;
		} else if (!medicalCenterCode.equals(other.medicalCenterCode))
			return false;
		if (regionCode == null) {
			if (other.regionCode != null)
				return false;
		} else if (!regionCode.equals(other.regionCode))
			return false;
		return true;
	}

	@Override
	public void fromData(DataInput input) throws IOException, ClassNotFoundException {
		facilityCode = DataSerializer.readString(input);
		medicalCenterCode =  DataSerializer.readString(input);
		LMACode =  DataSerializer.readString(input);
		regionCode = DataSerializer.readString(input);
		
	}

	@Override
	public void toData(DataOutput output) throws IOException {
		DataSerializer.writeString(facilityCode, output);
		DataSerializer.writeString(medicalCenterCode, output);
		DataSerializer.writeString(LMACode, output);
		DataSerializer.writeString(regionCode, output);
		
	}
}
