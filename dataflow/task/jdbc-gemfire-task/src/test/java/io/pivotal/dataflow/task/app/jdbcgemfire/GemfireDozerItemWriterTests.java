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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.util.*;

import com.gemstone.gemfire.cache.client.ClientCache;
import io.pivotal.dataflow.task.app.jdbcgemfire.common.GemfireDozerItemWriter;
import io.pivotal.dataflow.task.app.jdbcgemfire.common.JdbcGemfireTaskProperties;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.dozer.DozerBeanMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.batch.item.SpELItemKeyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.convert.converter.Converter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = JdbcGemfireTaskApplication.class)
@SuppressWarnings("serial")
public class GemfireDozerItemWriterTests {
    private static final Map<String, Object> MY_MAP = createMap();
    Exception ex = null;

        private static Map<String, Object> createMap() {
            Map<String, Object> result = new HashMap<String, Object>();
// output from log used failure log used for test data
//            {lorange=5001, hirange=50000, titleId=BU1032, royalty=0.12}
            result.put("lorange", 5001);
            result.put("hirange", 50000);
            result.put("titleId", "BU1032");
            result.put("royalty", 0.12);
            return Collections.unmodifiableMap(result);
        }


    @Autowired
    ConfigurableApplicationContext applicationContext;

    @Autowired
	GemfireDozerItemWriter itemWriter;

    @Autowired
	DozerBeanMapper dmb;	// private PlatformTransactionManager transactionManager = new ResourcelessTransactionManager();

	@Autowired
	JdbcGemfireTaskProperties props;

	@Test
 	public void testWrite() {

        try {
            itemWriter.write((List<? extends Map<String, Object>>) MY_MAP);
        } catch (Exception e) {
        	System.out.println("ex: " +  e.getMessage());
           ex = e;
        }
        assertEquals(null, ex);
    }


//	@Test
	public void testBasicDelete() throws Exception {
	}

	//	@Test
	public void testWriteWithCustomItemKeyMapper() throws Exception {
	}

//	@Test
	public void testWriteNoTransactionNoItems() throws Exception {
	}

	static class Foo {
		public Bar bar;

		public Foo(Bar bar) {
			this.bar = bar;
		}
	}

	static class Bar {
		public String val;

		public Bar(String b1) {
			this.val = b1;
		}
	}
}
