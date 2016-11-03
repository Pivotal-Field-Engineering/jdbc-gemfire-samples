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

import java.util.AbstractMap.SimpleEntry;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
@TestPropertySource(properties = { "spring.cloud.task.name=pubs-titles-authors-test-8",
		"jdbcgemfire.region-name=TitleAuthor", "jdbcgemfire.sql=select * from TITLEAUTHORS where au_id='486-29-1786' and title_id='PS7777'" })
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
			} else {
				map = DATA_SET.get("TitleAuthors");
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
		dataSet.put("RoyaltySchedule", createRoyaltyScheduleMap());
		dataSet.put("Sale", createSalesMap());
		dataSet.put("SalesDetail", createSalesDetailMap());
		dataSet.put("Title", createTitlesMap());
		dataSet.put("TitleAuthor", createTitleAuthorsMap());
		dataSet.put("TitleEditor", createTitleEditorsMap());
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
		// {sonum=2, sdate=1998-09-14, ponum=D4482, storId=7067}
		salesItems.put("sonum", 21);
		salesItems.put("sdate", "1998-09-14");
		salesItems.put("ponum", "D4482");
		salesItems.put("storId", 8178);
		return Collections.unmodifiableMap(salesItems);
	}

	private static Map<String, Object> createSalesDetailMap() {
		Map<String, Object> salesDetailsItems = new HashMap<String, Object>();
		// output from log used failure log used for test data
		// {sonum=2, sdate=1998-09-14, ponum=D4482, storId=7067}
		salesDetailsItems.put("sonum", 2);
		salesDetailsItems.put("qtyOrdered", 10);
		salesDetailsItems.put("titleId", "PS2091");
		salesDetailsItems.put("qtyShipped", 10);
		salesDetailsItems.put("dateShipped", "1998-09-22");
		return Collections.unmodifiableMap(salesDetailsItems);
	}

	private static Map<String, Object> createTitlesMap() {
		return Collections.unmodifiableMap(Stream
				.of(
						// output from log used failure log used for test data
						// {sonum=2, sdate=1998-09-14, ponum=D4482, storId=7067}
						new SimpleEntry<>("titleId", "PS7777"), new SimpleEntry<>("pubId", "0736"),
						new SimpleEntry<>("title", "Emotional Security: A New Algorithm"),
						new SimpleEntry<>("pubdate", "1998-06-12"), new SimpleEntry<>("price", 17.99),
						new SimpleEntry<>("ytdSales", 3336), new SimpleEntry<>("contract", 1),
						new SimpleEntry<>("type", "psychology"),
						new SimpleEntry<>("note",
								"Protecting yourself and your loved ones from undue emotional stress in the modern world.  Use of computer and nutritional aids emphasized."))
				.collect(Collectors.toMap((e) -> e.getKey(), (e) -> e.getValue())));

	}

	private static Map<String, Object> createTitleAuthorsMap() {
		return Collections.unmodifiableMap(Stream
				.of(
						// {auOrd=1, auId=409-56-7008, titleId=BU1032, royaltyshare=0.60}
						new SimpleEntry<>("auId", "409-56-7008"), new SimpleEntry<>("titleId", "BU1032"),
						new SimpleEntry<>("auOrd", 1), new SimpleEntry<>("royaltyshare", 0.60))
				.collect(Collectors.toMap((e) -> e.getKey(), (e) -> e.getValue())));
	}

	private static Map<String, Object> createTitleEditorsMap() {
		return Collections.unmodifiableMap(Stream
				.of(
						// output from log used failure log used for test data
						// {sonum=2, sdate=1998-09-14, ponum=D4482, storId=7067}
						new SimpleEntry<>("titleId", "PS7777"), new SimpleEntry<>("pubId", "0736"),
						new SimpleEntry<>("title", "Emotional Security: A New Algorithm"),
						new SimpleEntry<>("pubdate", "1998-06-12"), new SimpleEntry<>("price", 17.99),
						new SimpleEntry<>("ytdSales", 3336), new SimpleEntry<>("contract", 1),
						new SimpleEntry<>("type", "psychology"),
						new SimpleEntry<>("note",
								"Protecting yourself and your loved ones from undue emotional stress in the modern world.  Use of computer and nutritional aids emphasized."))
				.collect(Collectors.toMap((e) -> e.getKey(), (e) -> e.getValue())));
	}

}
