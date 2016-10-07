package com.kp.rabbitmq.dbo;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

//This class added for standalone jar. Need to convert to use JNDI.'
@Component("DataSourceHelper")
public class DataSourceHelper {
	private static DataSource datasource = new DataSource();
	private static DataSourceHelper dataSourceHelper= new DataSourceHelper();
	@Value("${jdbc_url}") private String jdbc_url;

	private DataSourceHelper(){
        datasource.setDriverClassName("com.vmware.sqlfire.jdbc.ClientDriver");
        datasource.setUrl("jdbc:sqlfire://TTGNAPR0VRHSF01.TTGTPMG.NET:1527");
        //datasource.setUrl(jdbc_url);
        datasource.setUsername("sa");
        datasource.setPassword("password");
        datasource.setInitialSize(10);
        datasource.setMaxWait(10000);
        datasource.setRemoveAbandoned(true);
    }
	
	public static synchronized javax.sql.DataSource getSource(){
		return datasource;
	}
	public static DataSourceHelper getInstance(){
		return dataSourceHelper;
	}

}
