package com.kp.rabbitmq.dbo;

import javax.naming.NamingException;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jndi.JndiTemplate;
import org.springframework.stereotype.Component;


@Component
public class  JNDIDataSource {
	private static final Logger log = LoggerFactory.getLogger(JNDIDataSource.class);
	
    public DataSource getDataSource() {
        DataSource dataSource = null;
        /*JndiTemplate jndi = new JndiTemplate();
        try {
            dataSource = (DataSource) jndi.lookup("java:comp/env/jdbc/SQLF_APT");
            System.out.println("##################Data source created"+dataSource);
        } catch (NamingException e) {
            log.error("NamingException for java:comp/env/jdbc/yourname", e);
        }*/
        return dataSource;
    }


}
