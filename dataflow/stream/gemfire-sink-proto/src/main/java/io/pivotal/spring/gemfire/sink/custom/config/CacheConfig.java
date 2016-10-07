package io.pivotal.spring.gemfire.sink.custom.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.gemstone.gemfire.cache.client.ClientCache;
import com.gemstone.gemfire.cache.client.ClientCacheFactory;
import com.gemstone.gemfire.cache.client.Pool;
import com.gemstone.gemfire.pdx.ReflectionBasedAutoSerializer;

import io.pivotal.spring.gemfire.sink.custom.GemfireSinkProtoProperties;

@Configuration
public class CacheConfig {


	private static final Logger LOG = LoggerFactory.getLogger(CacheConfig.class);

	@Bean
	public ClientCache createCache(GemfireSinkProtoProperties props) {
		LOG.info("creating cache");
		ClientCacheFactory ccf = new ClientCacheFactory();

		String locators = props.getLocators();

		String[] sa1 = locators.split(",");
		for (String st : sa1) {
			String[] sat = st.split(":");
			String host = sat[0];
			int port = sat.length > 1 ? Integer.parseInt(sat[1]) : 10334;
			LOG.info("creating cache: adding locator: host={}, port={}", host, port);

			ccf.addPoolLocator(host, port);
		}

		ccf.setPdxReadSerialized(false);
		ccf.setPdxSerializer(new ReflectionBasedAutoSerializer("io.pivotal.kaiser.gemfire.common.model.*"));

		return ccf.create();
	}

	@Bean
	public Pool createPool(ClientCache cache) {
		LOG.info("creating pool");
		return cache.getDefaultPool();
	}
}
