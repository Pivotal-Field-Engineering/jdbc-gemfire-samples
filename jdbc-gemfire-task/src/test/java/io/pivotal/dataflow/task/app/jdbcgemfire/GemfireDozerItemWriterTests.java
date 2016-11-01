/*
 * Copyright 2013-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.pivotal.dataflow.task.app.jdbcgemfire;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import io.pivotal.dataflow.task.app.jdbcgemfire.common.GemfireDozerItemWriter;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = JdbcGemfireTaskApplication.class)
@SuppressWarnings("serial")
@TestPropertySource(properties = {"spring.cloud.task.name=pubs-sales-test-10", "jdbcgemfire.region-name=Sale"})
public class GemfireDozerItemWriterTests {
    private static final Map<String, Map<String, Object>> DATA_SET = createDataSet();
    Map<String, Object> map = new HashMap<>();

    Exception ex = null;

    @Autowired
    ConfigurableApplicationContext applicationContext;

    @Autowired
    GemfireDozerItemWriter itemWriter;

    @Test
    public void testItemWriter() {
        try {
            String regionName = applicationContext.getEnvironment().getProperty("jdbcgemfire.region-name");
            System.out.println("Region to Test : " + regionName);
            if (regionName != null) {
                map = DATA_SET.get(regionName);
            }
            itemWriter.write((List<? extends Map<String, Object>>) map);
        } catch (Exception e) {
            System.out.println("ex: " + e.getMessage());
            ex = e;
        }
        assertEquals(null, ex);
    }

    private static Map<String, Map<String, Object>> createDataSet() {
        Map<String, Map<String, Object>> dataSet = new HashMap<>();
        dataSet.put("Sale", createSalesMap());
        dataSet.put("RoyaltySchedule", createRoyaltyScheduleMap());
        return Collections.unmodifiableMap(dataSet);
    }

    private static Map<String, Object> createRoyaltyScheduleMap() {
        Map<String, Object> result = new HashMap<String, Object>();
        // output from log used failure log used for test data
        // {lorange=5001, hirange=50000, titleId=BU1032, royalty=0.12}
        result.put("lorange", 5001);
        result.put("hirange", 50000);
        result.put("titleId", "BU1032");
        result.put("royalty", 0.12);
        return Collections.unmodifiableMap(result);
    }

    private static Map<String, Object> createSalesMap() {
        Map<String, Object> salesItems = new HashMap<String, Object>();
        // output from log used failure log used for test data
        //  {sonum=2, sdate=1998-09-14, ponum=D4482, storId=7067}
        salesItems.put("sonum", 2);
        salesItems.put("sdate", "1998-09-14");
        salesItems.put("ponum", "D4482");
        salesItems.put("storId", 7067);
        return Collections.unmodifiableMap(salesItems);
    }
}
