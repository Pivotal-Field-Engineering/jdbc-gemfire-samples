package io.pivotal.gemfire.proto.server.func;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.gemstone.gemfire.cache.Declarable;
import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.execute.Function;
import com.gemstone.gemfire.cache.execute.FunctionContext;
import com.gemstone.gemfire.cache.execute.RegionFunctionContext;
import com.gemstone.gemfire.cache.partition.PartitionRegionHelper;

import io.pivotal.kaiser.gemfire.common.key.AppointmentKey;
import io.pivotal.kaiser.gemfire.common.model.Appointment;

@SuppressWarnings("serial")
public class Rem_InvalidAppointFunction implements Function, Declarable {

	private static final String ID = "Rem_InvalidAppointFunction";
	private static final Logger LOG = LoggerFactory.getLogger(Rem_InvalidAppointFunction.class);

	public void init(Properties arg0) {
	}

	public void execute(FunctionContext context) {
		LOG.info("Executing Function : [{}]", Rem_InvalidAppointFunction.ID);
		Appointment appt = new Appointment();
		Date date = new Date();
		Set<AppointmentKey> keysToRemove = new HashSet<>();
		Region<AppointmentKey, Appointment> region = ((RegionFunctionContext) context).getDataSet();
		LOG.info("Region to Execute={}", region.getFullPath());
		region = PartitionRegionHelper.getLocalPrimaryData(region);

		// Snapshot keys to prevent dynamic view changes.
		Set<AppointmentKey> keys = new HashSet<>(region.keySet());
		long count = keys.size();
		if (count > 0) {
			for (AppointmentKey key : keys) {
				appt = region.get(key);
				Timestamp current = new Timestamp(date.getTime());
				Timestamp apptTime = appt.getApptDatetime();
				if (apptTime.before(current)) {
					keysToRemove.add(key);
				}
			}
		}
		region.removeAll(keysToRemove);
		context.getResultSender().lastResult("Done");

	}

	public String getId() {
		return Rem_InvalidAppointFunction.ID;
	}

	public boolean hasResult() {
		return true;
	}

	public boolean isHA() {
		return true;
	}

	public boolean optimizeForWrite() {
		return false;
	}

}
