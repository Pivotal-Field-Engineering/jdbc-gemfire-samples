package io.pivotal.gemfire.pubs.model.key;



import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.math.BigInteger;

import com.gemstone.gemfire.DataSerializable;
import com.gemstone.gemfire.DataSerializer;

@SuppressWarnings("serial")
public class AddressKey implements DataSerializable {

	BigInteger addressId;

	@Override
	public void fromData(DataInput input) throws IOException, ClassNotFoundException {
		addressId = DataSerializer.readObject(input);

	}

	@Override
	public void toData(DataOutput output) throws IOException {
		DataSerializer.writeObject(addressId, output);

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((addressId == null) ? 0 : addressId.hashCode());
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
		AddressKey other = (AddressKey) obj;
		if (addressId == null) {
			if (other.addressId != null)
				return false;
		} else if (!addressId.equals(other.addressId))
			return false;
		return true;
	}

	public BigInteger getAddressId() {
		return addressId;
	}

	public void setAddressId(BigInteger addressId) {
		this.addressId = addressId;
	}
}
