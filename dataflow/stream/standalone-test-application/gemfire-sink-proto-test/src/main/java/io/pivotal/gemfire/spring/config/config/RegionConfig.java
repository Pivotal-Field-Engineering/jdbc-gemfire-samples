package io.pivotal.gemfire.spring.config.config;

import javax.annotation.Resource;

import io.pivotal.gemfire.pubs.key.AuthorKey;
import io.pivotal.gemfire.pubs.model.Author;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.client.ClientCache;
import com.gemstone.gemfire.cache.client.ClientRegionFactory;
import com.gemstone.gemfire.cache.client.ClientRegionShortcut;
import com.gemstone.gemfire.cache.client.Pool;


@Component
public class RegionConfig {
	private static final Logger LOG = LoggerFactory.getLogger(RegionConfig.class);

	@Resource(name = "region")
	public Region<AuthorKey, Author> region;

	@Bean(name = "region")
	public Region<AuthorKey, Author> createDemoRegion(ClientCache cache, Pool pool) {
		LOG.info("Creating Region: {}", "Author");
		ClientRegionFactory<AuthorKey, Author> crf = cache.createClientRegionFactory(ClientRegionShortcut.PROXY);
		crf.setPoolName(pool.getName());
		Region<AuthorKey, Author> r = crf.create("Author");
		return r;
	}

}
