package com.kp.ttg;

import java.lang.management.ManagementFactory;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

public class DataSourceHelper implements Constants {

    private static Map<String, javax.sql.DataSource> sources = new HashMap<>();

    private static final String prefix = "org.kp.data-source.";
    private static final Logger LOG = LoggerFactory.getLogger(DataSourceHelper.class);

    private static String defaultDataSourceName;

    private DataSourceHelper() {
    }
    
    public static synchronized String getDefaultDataSourceName() {
        if (defaultDataSourceName == null) {
            defaultDataSourceName = System.getProperty(DEFAULT_DATA_SOURCE_NAME_PROPERTY);
            Assert.notNull(defaultDataSourceName, "Required property '" + DEFAULT_DATA_SOURCE_NAME_PROPERTY + "' not defined");
        }
        
        return defaultDataSourceName;
    }

    public static synchronized javax.sql.DataSource getSource(String name) {
        if (!StringUtils.hasText(name)) {
            LOG.debug("DataSource name not provided, using default {}", getDefaultDataSourceName());
            name = getDefaultDataSourceName();
        }

        javax.sql.DataSource ds = sources.get(name);

        if (ds != null) {
            LOG.debug("DataSource {} already exists", name);
            return ds;
        }

        LOG.info("DataSource {} does not exist - creating", name);

        checkProperty(name, "driverClassName");
        checkProperty(name, "url");

        Properties props = getProps(name);

        LOG.info("name={}, props={}", name, props);

        DataSource bds = new DataSource();

        Class<?> cls = bds.getClass();

        for (String propName : props.stringPropertyNames()) {
            String propValue = props.getProperty(propName);
            String setterMethodName = "set" + Character.toUpperCase(propName.charAt(0)) + propName.substring(1);

            try {
                Method m = findSetter(cls, setterMethodName);
                if (m != null) {
                    Class<?> pt = m.getParameterTypes()[0];
                    Object op = null;

                    if (pt == String.class) {
                        op = propValue;
                    } else if (pt == Integer.class || pt == int.class) {
                        op = new Integer(propValue);
                    } else if (pt == Long.class || pt == long.class) {
                        op = new Long(propValue);
                    } else if (pt == Boolean.class || pt == boolean.class) {
                        op = new Boolean(propValue);
                    } else {
                        throw new IllegalArgumentException("Data type '" + pt.getName() + "' not supported for property '"
                                + propName + "'");
                    }

                    LOG.info("property: name={}, value={}", propName, op);
                    m.invoke(bds, op);
                }
            } catch (InvocationTargetException x) {
                LOG.error(x.toString(), x);
                throw new IllegalArgumentException("DataSource property '" + propName + "' cannot be set: " + x.getMessage(), x);
            } catch (IllegalAccessException x) {
                LOG.error(x.toString(), x);
                throw new IllegalArgumentException("DataSource property '" + propName + "' cannot be set: " + x.getMessage(), x);
            }
        }

        try {
            bds.setJmxEnabled(true);

            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            ObjectName oname = new ObjectName("org.kp.data-source:service=DataSource,name=" + name);

            mbs.registerMBean(bds.createPool().getJmxPool(), oname);
            bds.createPool().getJmxPool();
        } catch (Exception x) {
            throw new IllegalArgumentException(x.toString(), x);
        }

        ds = bds;
        sources.put(name, ds);

        LOG.info("DataSource {} created successfully", name);
        return ds;
    }

    private static String checkProperty(String name, String prop) {
        String pn = prefix + name + "." + prop;
        String val = System.getProperty(pn);
        Assert.hasText(val, "Missing property '" + pn + "'");
        return val;
    }

    private static Properties getProps(String name) {
        String pk = prefix + name + ".";
        int pkl = pk.length();

        Properties props = new Properties();

        for (Map.Entry<Object, Object> entry : System.getProperties().entrySet()) {
            String pn = entry.getKey().toString();
            if (pn.startsWith(pk)) {
                if (pn.length() == pkl) {
                    throw new IllegalArgumentException("Invalid data source property: " + pn);
                }

                props.setProperty(pn.substring(pkl), entry.getValue().toString());
            }
        }

        return props;
    }

    private static Method findSetter(Class<?> cls, String name) {
        for (Method m : cls.getMethods()) {
            if (name.equals(m.getName()) && m.getParameterTypes().length == 1) {
                return m;
            }
        }

        return null;
    }
}
