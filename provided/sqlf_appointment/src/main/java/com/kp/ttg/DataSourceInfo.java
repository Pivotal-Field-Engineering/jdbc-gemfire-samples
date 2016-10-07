package com.kp.ttg;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class DataSourceInfo {

    private String dataSourceName;
    private CaseSensitivity caseSensitivity;

    private static final Map<String, DataSourceInfo> info = new HashMap<>();

    private static final Logger LOG = LoggerFactory.getLogger(DataSourceInfo.class);

    public DataSourceInfo(String dataSourceName, CaseSensitivity caseSensitivity) {
        this.dataSourceName = dataSourceName;
        this.caseSensitivity = caseSensitivity;
    }

    public String getDataSourceName() {
        return dataSourceName;
    }

    public CaseSensitivity getCaseSensitivity() {
        return caseSensitivity;
    }
    
    public String fixCase(String s) {
        if (!StringUtils.hasText(s)) {
            return s;
        }
        
        switch (caseSensitivity) {
            case lower: return s.toLowerCase();
            case upper: return s.toUpperCase();
            default: return s;
        }
    }

    public static synchronized DataSourceInfo getInfo(String dsName) {
        if (!StringUtils.hasText(dsName)) {
            String defaultDataSourceName = DataSourceHelper.getDefaultDataSourceName();
            LOG.debug("DataSource name not provided, using default {}", defaultDataSourceName);
            dsName = defaultDataSourceName;
        }

        DataSourceInfo inf = info.get(dsName);

        if (inf != null) {
            return inf;
        }

        String scs = System.getProperty("org.kp.data-source." + dsName + ".case-sensitivity");

        CaseSensitivity cs = StringUtils.hasText(scs) ? CaseSensitivity.valueOf(scs) : CaseSensitivity.none;

        inf = new DataSourceInfo(dsName, cs);
        info.put(dsName, inf);

        LOG.info("inf={}", inf);

        return inf;
    }

    @Override
    public String toString() {
        return "DataSourceInfo [dataSourceName=" + dataSourceName + ", caseSensitivity=" + caseSensitivity + "]";
    }

}
