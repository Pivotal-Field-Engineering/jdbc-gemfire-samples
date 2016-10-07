package io.pivotal.kaiser.test.spring.gemfire.sink.custom.config;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.client.ClientCache;
import com.gemstone.gemfire.cache.client.ClientRegionFactory;
import com.gemstone.gemfire.cache.client.ClientRegionShortcut;
import com.gemstone.gemfire.cache.client.Pool;

import io.pivotal.kaiser.gemfire.common.key.MemberKey;
import io.pivotal.kaiser.gemfire.common.model.Member;


@Component
public class RegionConfig {
	private static final Logger LOG = LoggerFactory.getLogger(RegionConfig.class);

	@Resource(name = "region")
	public Region<MemberKey, Member> region;

	@Bean(name = "region")
	public Region<MemberKey, Member> createDemoRegion(ClientCache cache, Pool pool) {
		LOG.info("Creating Region: {}", "Member");
		ClientRegionFactory<MemberKey, Member> crf = cache.createClientRegionFactory(ClientRegionShortcut.PROXY);
		crf.setPoolName(pool.getName());
		Region<MemberKey, Member> r = crf.create("Member");
		return r;
	}

}
