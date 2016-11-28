package io.pivotal.dataflow.task.app.jdbcgemfire;

import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.client.ClientCache;
import io.pivotal.dataflow.task.app.jdbcgemfire.common.GemfireDozerItemWriter;
import io.pivotal.dataflow.task.app.jdbcgemfire.config.ClientConfiguration;
import io.pivotal.dataflow.task.app.jdbcgemfire.config.DozerConfig;
import io.pivotal.gemfire.pubs.model.Sale;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by zhansen on 11/4/16.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ClientConfiguration.class, DozerConfig.class, GemfireDozerItemWriter.class, GemfireDozerItemWriterTestUtil.class})
@SuppressWarnings("serial")
@ActiveProfiles(profiles = {"sale"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class SaleTest {

    @Autowired
    GemfireDozerItemWriterTestUtil util;

    @Autowired
    ClientCache cache;

    @Test
    @Profile("sale")
    public void testSale() {
        String regionName = "Sale";
        Region region = cache.getRegion(regionName);
        Object k = util.testItemWriter(regionName);
        Sale value = (Sale) region.get(k);
        Map<String,Object> testValue = util.createSalesMap();
        assertEquals(testValue.get("ponum"), value.getPonum());
        region.remove(k);
    }
}
