package io.pivotal.kaiser.gemfire.proto.server.func;

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

/**
 *  Clears all data in specified region. Has Partition support. 
 * @author zhansen
 */
@SuppressWarnings("serial")
public class ExampleClearFunctionOnRegion implements Function, Declarable {

	private static final String ID = "ExampleClearFunctionOnRegion";
	private static final Logger LOG = LoggerFactory.getLogger(ExampleClearFunctionOnRegion.class);
	
	public void init(Properties props) {
	}

	public void execute(FunctionContext context) {
		Region<Object, Object> region = ((RegionFunctionContext) context).getDataSet();
		LOG.info("Region to Execute={}",region.getFullPath());
		region = PartitionRegionHelper.getLocalPrimaryData(region);

		// Snapshot keys to prevent dynamic view changes.
		Set<Object> keys = new HashSet<>(region.keySet());
		long count = keys.size();
		if (count > 0) {
			region.removeAll(keys);
		}
		context.getResultSender().lastResult("Done");
		
	}

	public String getId() {
		return ExampleClearFunctionOnRegion.ID;
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