package io.pivotal.kaiser.dataflow.task.app.jdbcgemfire.config;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.client.ClientCache;
import com.gemstone.gemfire.cache.client.ClientRegionFactory;
import com.gemstone.gemfire.cache.client.ClientRegionShortcut;
import com.gemstone.gemfire.cache.client.Pool;

import io.pivotal.kaiser.dataflow.task.app.jdbcgemfire.common.JdbcGemfireTaskProperties;

@Component
public class RegionConfig {
	private static final Logger LOG = LoggerFactory.getLogger(RegionConfig.class);
	
	@Resource(name = "clientRegion")
	public Region<?, ?> region;

	@Bean(name = "clientRegion" )
	public Region<?, ?> createTaskRegion(ClientCache cache, Pool pool, JdbcGemfireTaskProperties props) {
		LOG.info("creating clientRegion");
		ClientRegionFactory<?, ?> crf = cache.createClientRegionFactory(ClientRegionShortcut.PROXY);
		crf.setPoolName(pool.getName());
		Region<?, ?> r = crf.create(props.regionName);
		LOG.info("Got region = {}", r.getName());
		return r;
	}

}


