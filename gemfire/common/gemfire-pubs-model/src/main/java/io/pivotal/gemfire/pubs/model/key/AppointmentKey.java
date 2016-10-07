package io.pivotal.gemfire.pubs.model.key;



import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.gemstone.gemfire.DataSerializable;
import com.gemstone.gemfire.DataSerializer;



@SuppressWarnings("serial")
public class AppointmentKey implements DataSerializable {

	
	String appointmentId;

	@Override
	public void fromData(DataInput input) throws IOException, ClassNotFoundException {
		appointmentId = DataSerializer.readString(input);

	}

	@Override
	public void toData(DataOutput output) throws IOException {
		DataSerializer.writeString(appointmentId, output);
	}

	public String getAppointmentId() {
		return appointmentId;
	}

	public void setAppointmentId(String appointmentId) {
		this.appointmentId = appointmentId;
	}

	@Override
	public int hashCode() {
		return appointmentId.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AppointmentKey other = (AppointmentKey) obj;
		if (appointmentId == null) {
			if (other.appointmentId != null)
				return false;
		} else if (!appointmentId.equals(other.appointmentId))
			return false;
		return true;
	}
}
