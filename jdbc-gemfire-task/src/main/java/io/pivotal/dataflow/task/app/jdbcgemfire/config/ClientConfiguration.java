package io.pivotal.dataflow.task.app.jdbcgemfire.config;

import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.client.ClientCache;
import com.gemstone.gemfire.cache.client.ClientRegionFactory;
import com.gemstone.gemfire.cache.client.ClientRegionShortcut;
import com.gemstone.gemfire.pdx.PdxInstance;
import io.pivotal.dataflow.task.app.jdbcgemfire.common.JdbcGemfireTaskProperties;
import io.pivotal.spring.cloud.service.gemfire.GemfireServiceConnectorConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.Cloud;
import org.springframework.cloud.CloudFactory;
import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.cloud.service.ServiceConnectorConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//this class is required for the Spring-Boot app to connect to the Gemfire tile.
//it creates a client cache and a region IoT on the client side that is a Proxy to the Gemfire region

@Configuration
@EnableConfigurationProperties(JdbcGemfireTaskProperties.class)
public class ClientConfiguration extends AbstractCloudConfig {


    @Autowired
    private ClientCache cache;

    @Autowired
    private JdbcGemfireTaskProperties props;

    public ServiceConnectorConfig createGemfireConnectorConfig() {
        GemfireServiceConnectorConfig gemfireConfig = new GemfireServiceConnectorConfig();
        gemfireConfig.setPoolIdleTimeout(7777L);
        return gemfireConfig;
    }

    //creation of client cache using CloudFoundry ServiceInfo Connector to get Locator{host}:{port}
    @Bean(name = "my-client-cache")
    public ClientCache getGemfireClientCache() {
        CloudFactory cloudFactory = new CloudFactory();
        Cloud cloud = cloudFactory.getCloud();
        ClientCache cache = cloud.getServiceConnector("gemfire", ClientCache.class,
                createGemfireConnectorConfig());
        return cache;
    }

    //creation of Proxy region IoT in the client cache. Needed for querying the server.
    @Bean(name = "my-client-region")
    public Region<?, ?> clientRegion() {
        ClientRegionFactory<?, ?> theRegion = cache.createClientRegionFactory(ClientRegionShortcut.PROXY);
        Region<?, ?> r = theRegion.create(props.regionName);
        return r;

    }
}