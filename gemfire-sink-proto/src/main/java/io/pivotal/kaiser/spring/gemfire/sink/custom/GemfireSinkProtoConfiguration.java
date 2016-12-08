package io.pivotal.kaiser.spring.gemfire.sink.custom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.integration.annotation.ServiceActivator;

import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.client.ClientCache;
import java.util.Map;
import java.util.Set;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * Generate Key class and methods via Reflection Use the key class to build
 * composite key Store in Gemfire : region.put(KeyClass,ValueClass);
 * 
 */
@EnableBinding(Sink.class)
@EnableConfigurationProperties({ GemfireSinkProtoProperties.class })
public class GemfireSinkProtoConfiguration {

	private static Logger LOG = LoggerFactory.getLogger(GemfireSinkProtoConfiguration.class);

	@Autowired
	ClientCache cache;
	
	@Autowired
	DozerBeanMapper dmb;

	@Autowired
	GemfireSinkProtoProperties props;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ServiceActivator(inputChannel = Sink.INPUT)
	public void gemfireSink(Object payload) {
		LOG.info("RegionName : {}", props.getRegionName());
		try {
			LOG.info("Payload type : {}", payload.getClass());
			Map payloadAsMap = (Map) payload;
			LOG.info(payload.toString());

			Set columnNames = payloadAsMap.keySet();
			LOG.debug("Fields from Map = {}", columnNames.toString());

			Class K = Class.forName("io.pivotal.kaiser.gemfire.common.key." + props.getRegionName() + "Key");
			Object k = K.newInstance();
			LOG.debug("KeyClass to map : {}", K.getName());

			Class V = Class.forName("io.pivotal.kaiser.gemfire.common.model." + props.getRegionName());
			Object v = V.newInstance();
			LOG.debug("ValueClass to map : {}", V.getName());
			
			Mapper mp = dmb;
			LOG.info("Starting Dozer Mapping of Class [{}]", K.getName());
			k = mp.map(payloadAsMap, K);
			LOG.info("Starting Dozer Mapping of Class [{}]", V.getName());
			v = mp.map(payloadAsMap, V);

			Region region = cache.getRegion(props.getRegionName());
			LOG.info("Region to Load : {}", region.getFullPath().toString());
			region.put(k, v);

		} catch (Exception e) {
			LOG.error("Exception in {} : Exception {} : Caused by: {}", "GemfireSinkProtoConfiguration.class",
					e.getMessage(), e.getCause());

		}

	}

}
