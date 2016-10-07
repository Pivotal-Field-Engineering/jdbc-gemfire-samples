package io.pivotal.kaiser.dataflow.task.app.jdbcgemfire.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.gemstone.gemfire.cache.client.ClientCache;
import com.gemstone.gemfire.cache.client.ClientCacheFactory;
import com.gemstone.gemfire.cache.client.Pool;
import com.gemstone.gemfire.pdx.ReflectionBasedAutoSerializer;

import io.pivotal.kaiser.dataflow.task.app.jdbcgemfire.common.JdbcGemfireTaskProperties;

@Configuration
public class CacheConfig {


	private static final Logger LOG = LoggerFactory.getLogger(CacheConfig.class);

	@Bean
	public ClientCache createCache(JdbcGemfireTaskProperties props) {
		LOG.info("creating cache");
		ClientCacheFactory ccf = new ClientCacheFactory();

		String locators = props.locators;

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
