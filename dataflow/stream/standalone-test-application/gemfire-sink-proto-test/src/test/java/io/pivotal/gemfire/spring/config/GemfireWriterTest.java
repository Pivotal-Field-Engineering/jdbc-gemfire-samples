package io.pivotal.gemfire.spring.config;

import com.google.common.collect.ImmutableMap;
import io.pivotal.gemfire.pubs.model.RoyaltySchedule;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.*;

import io.pivotal.dataflow.task.app.jdbcgemfire.common.GemfireDozerItemWriter;
import io.pivotal.gemfire.pubs.key.RoyaltyScheduleKey;
import io.pivotal.gemfire.pubs.model.Author;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;
import com.gemstone.gemfire.cache.Region;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = GemfireSinkProtoTestApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
// @SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class GemfireWriterTest {

/* Groovy version of this crap that follows
     List items = [
            [lorange : 0, hirange:5000, titleId : "BU1032", royalty : 0.10],
            [lorange : 5001, hirange: 50000, titleId : "BU1032", royalty: 0.12],
            [lorange : 0, hirange : 2000, titleId : "PC1035", royalty : 0.10],
            [lorange : 2001, hirange : 4000, titleId : "PC1035", royalty : 0.12],
            [lorange : 4001, hirange : 50000, titleId : "PC1035", royalty : 0.16],
            [lorange : 0, hirange : 1000, titleId : "BU2075", royalty : 0.10]
        ]
     */
    private static final List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
    private static final Map<String, Object> RST1 = new HashMap<String, Object>();
    static {
        RST1.put("lorange", Integer.valueOf(0));
        RST1.put("hirange", Integer.valueOf(5000));
        RST1.put("titleId", "BU1032");
        RST1.put("royalty", BigDecimal.valueOf(0.10));
        items.add(RST1);
    }
    Exception ex;

    @Autowired
	GemfireDozerItemWriter writer;

	@Autowired
	Region<RoyaltyScheduleKey, RoyaltySchedule> region;
	
	RoyaltyScheduleKey appKey = new RoyaltyScheduleKey();

 	@Before
	public void init() {

	}
	
	@After
	public void postTest() {
		region.remove(appKey);
	}

	@Test
	public void testInsertRoyaltySchedule() {
        try {
            writer.write(items);
        } catch (Exception e) {
            ex = e;
        }
        assertEquals(null, ex);
    }
}
