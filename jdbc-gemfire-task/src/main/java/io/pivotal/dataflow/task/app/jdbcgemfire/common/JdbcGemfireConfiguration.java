/*
 * Copyright 2016 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.pivotal.dataflow.task.app.jdbcgemfire.common;


import java.util.Map;

import javax.sql.DataSource;

import io.pivotal.dataflow.task.app.jdbcgemfire.common.support.JdbcGemfireDataSourceConfiguration;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.task.batch.configuration.TaskBatchExecutionListenerFactoryBean;
import org.springframework.cloud.task.configuration.DefaultTaskConfigurer;
import org.springframework.cloud.task.configuration.EnableTask;
import org.springframework.cloud.task.configuration.TaskConfigurer;
import org.springframework.cloud.task.repository.support.TaskRepositoryInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;


@EnableTask
@EnableBatchProcessing
@org.springframework.context.annotation.Configuration
@EnableConfigurationProperties({JdbcGemfireTaskProperties.class})
@Import(JdbcGemfireDataSourceConfiguration.class)
public class JdbcGemfireConfiguration {

	public static final String[] PROMOTION_LISTENER_KEYS = {"batch.incremental.maxId"};

	@Autowired
	private JdbcGemfireTaskProperties props;

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Autowired
	@Qualifier("taskDataSource")
	public DataSource taskDataSource;

	@Autowired
	@Qualifier("JdbcGemfireDataSource")
	public DataSource jdbcGemfireDataSource;

	@Autowired
	public JobRepository jobRepository;

	@Autowired
	private Environment environment;

	@Autowired(required = false)
	private ConfigurableApplicationContext context;

	
	@Bean
	@StepScope
	public JdbcCursorItemReader<Map<String, Object>> jdbcCursorItemReader() throws Exception {
		JdbcCursorItemReader<Map<String, Object>> jdbcCursorItemReader = new JdbcCursorItemReader<>();
		JdbcColumnToPojoRowMapper rowMapper = new JdbcColumnToPojoRowMapper();
		jdbcCursorItemReader.setDataSource(this.jdbcGemfireDataSource);
		jdbcCursorItemReader.setSql(this.props.getSql());
		jdbcCursorItemReader.setFetchSize(this.props.getCommitInterval());
		jdbcCursorItemReader.setRowMapper(rowMapper);
		jdbcCursorItemReader.afterPropertiesSet();
		return jdbcCursorItemReader;
	}

	
	@Bean
	@StepScope
	public GemfireDozerItemWriter writer()  throws Exception {
		GemfireDozerItemWriter gemfireItemWriter = new GemfireDozerItemWriter();
		return gemfireItemWriter;
	}



	@Bean
	public Step workerStep() throws Exception {
		Step step = this.stepBuilderFactory.get("workerStep")
				.<Map<String, Object>, Map<String, Object>>chunk(this.props.getCommitInterval())
				.reader(jdbcCursorItemReader())
				.writer(writer())
				.build();
		return step;
	}

	@Bean
	public Job partitionedJob() throws Exception {
		String jobName = environment.getProperty("spring.cloud.task.name") != null ?
				environment.getProperty("spring.cloud.task.name"): this.context.getId();
		JobBuilder jobBuilder =  this.jobBuilderFactory.get(jobName);
		if(!this.props.isRestartable()) {
			jobBuilder.preventRestart();
		}
		return jobBuilder.start(workerStep())
				.build();
	}

	@Bean
	public DefaultTaskConfigurer taskConfigurer( ) {
		return new DefaultTaskConfigurer(this.taskDataSource);
	}

	@Bean
	public DefaultBatchConfigurer batchConfigurer() {
		return new DefaultBatchConfigurer(this.taskDataSource);
	}

	@Bean
	public TaskRepositoryInitializer taskRepositoryInitializer() {
		TaskRepositoryInitializer taskRepositoryInitializer = new TaskRepositoryInitializer();
		taskRepositoryInitializer.setDataSource(this.taskDataSource);
		return taskRepositoryInitializer;
	}

	@Bean
	public TaskBatchExecutionListenerFactoryBean taskBatchExecutionListener(TaskConfigurer taskConfigurer) {
		return new TaskBatchExecutionListenerFactoryBean(this.taskDataSource, taskConfigurer.getTaskExplorer());
	}

}
