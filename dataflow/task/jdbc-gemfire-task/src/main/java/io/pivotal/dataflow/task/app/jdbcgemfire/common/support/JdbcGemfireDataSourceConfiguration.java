package io.pivotal.dataflow.task.app.jdbcgemfire.common.support;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

/**
 * Supports the datasource configurations required for the JdbcGemfire application.
 *
 * @author Glenn Renfro
 */
@EnableConfigurationProperties({JdbcGemfireDataSourceProperties.class})
public class JdbcGemfireDataSourceConfiguration {

	@Autowired
	private JdbcGemfireDataSourceProperties props;

	@Autowired
	private Environment environment;

	@Bean(name="taskDataSource")
	@Primary
	public DataSource taskDataSource() {
		return getDefaultDataSource();
	}


	@Bean(name="JdbcGemfireDataSource")
	public DataSource jdbcGemfireDataSource() {
		DataSource dataSource;
		if(props.getUrl() != null && props.getUsername() != null) {
			dataSource = DataSourceBuilder.create().driverClassName(props.getDriverClassName())
				.url(props.getUrl())
				.username(props.getUsername())
				.password(props.getPassword()).build();
		} else {
			dataSource = getDefaultDataSource();
		}
		return dataSource;
	}

	private DataSource getDefaultDataSource() {
		return DataSourceBuilder.create().driverClassName(environment.getProperty("spring.datasource.driverClassName"))
				.url(environment.getProperty("spring.datasource.url"))
				.username(environment.getProperty("spring.datasource.username"))
				.password(environment.getProperty("spring.datasource.password")).build();
	}
}
