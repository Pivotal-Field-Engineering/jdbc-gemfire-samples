package io.pivotal.gemfire.pubs.model.key;



import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.gemstone.gemfire.DataSerializable;
import com.gemstone.gemfire.DataSerializer;

@SuppressWarnings("serial")
public class MemberKey implements DataSerializable {

	
	private String mrn;
	private String mrnPrefix;

	public String getMrn() {
		return mrn;
	}

	public void setMrn(String mrn) {
		this.mrn = mrn;
	}

	public String getMrnPrefix() {
		return mrnPrefix;
	}

	public void setMrnPrefix(String mrnPrefix) {
		this.mrnPrefix = mrnPrefix;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mrn == null) ? 0 : mrn.hashCode());
		result = prime * result + ((mrnPrefix == null) ? 0 : mrnPrefix.hashCode());
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
		MemberKey other = (MemberKey) obj;
		if (mrn == null) {
			if (other.mrn != null)
				return false;
		} else if (!mrn.equals(other.mrn))
			return false;
		if (mrnPrefix == null) {
			if (other.mrnPrefix != null)
				return false;
		} else if (!mrnPrefix.equals(other.mrnPrefix))
			return false;
		return true;
	}

	@Override
	public void fromData(DataInput input) throws IOException, ClassNotFoundException {
		mrn = DataSerializer.readString(input);
		mrnPrefix = DataSerializer.readString(input);
		
	}

	@Override
	public void toData(DataOutput output) throws IOException {
		DataSerializer.writeString(mrn, output);
		DataSerializer.writeString(mrnPrefix, output);
		
	}
}
