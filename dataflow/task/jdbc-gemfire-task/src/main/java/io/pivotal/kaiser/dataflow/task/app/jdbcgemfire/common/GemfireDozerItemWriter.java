package io.pivotal.kaiser.dataflow.task.app.jdbcgemfire.common;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.client.ClientCache;

/**
 * 
 * Generate Key class and methods via Reflection Use the key class to build
 * composite key Store in Gemfire : region.put(KeyClass,ValueClass);
 * 
 */
@EnableConfigurationProperties({ JdbcGemfireTaskProperties.class })
public class GemfireDozerItemWriter implements ItemWriter<Map<String, Object>> {

	private static Logger LOG = LoggerFactory.getLogger(GemfireDozerItemWriter.class);

	@Autowired
	ClientCache cache;

	@Autowired
	DozerBeanMapper dmb;

	@Autowired
	JdbcGemfireTaskProperties props;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void write(List<? extends Map<String, Object>> items) throws Exception {
		// TODO Auto-generated method stub
		LOG.info("RegionName : {}", props.regionName);
		for (Map<String, Object> item : items) {
			try {
				LOG.info("Payload type : {}", item.getClass());
				Map payloadAsMap = item;
				LOG.info(item.toString());

				Set columnNames = payloadAsMap.keySet();
				LOG.debug("Fields from Map = {}", columnNames.toString());

				Class K = Class.forName("io.pivotal.kaiser.gemfire.common.key." + props.regionName + "Key");
				Object k = K.newInstance();
				LOG.debug("KeyClass to map : {}", K.getName());

				Class V = Class.forName("io.pivotal.kaiser.gemfire.common.model." + props.regionName);
				Object v = V.newInstance();
				LOG.debug("ValueClass to map : {}", V.getName());

				Mapper mp = dmb;
				LOG.info("Starting Dozer Mapping of Class [{}]", K.getName());
				k = mp.map(payloadAsMap, K);
				LOG.info("Starting Dozer Mapping of Class [{}]", V.getName());
				v = mp.map(payloadAsMap, V);

				Region region = cache.getRegion(props.regionName);
				LOG.info("Region to Load : {}", region.getFullPath().toString());
				region.put(k, v);

			} catch (Exception e) {
				LOG.error("Exception in {} : Exception {} : Caused by: {}", "GemfireSinkProtoConfiguration.class",
						e.getMessage(), e.getCause());

			}
		}

	}
}