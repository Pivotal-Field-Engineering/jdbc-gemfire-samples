package com.kp.ttg;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class TableMapping {

    private String sqlfSchemaName;
    private String sqlfTableName;
    private String backendCatalogName;
    private String backendSchemaName;
    private String backendTableName;
    private String backendDataSourceName;
    private ColumnMappings columnMappings;

    private static final Map<String, TableMapping> tableMappings = new HashMap<>();

    private static final Logger LOG = LoggerFactory.getLogger(TableMapping.class);

    public TableMapping(String sqlfSchemaName, String sqlfTableName) {
        this.sqlfSchemaName = sqlfSchemaName;
        this.sqlfTableName = sqlfTableName;
        this.backendSchemaName = sqlfSchemaName;
        this.backendTableName = sqlfTableName;
        columnMappings = new ColumnMappings();
    }

    public TableMapping(String sqlfSchemaName, String sqlfTableName, Properties props) {
        this.sqlfSchemaName = sqlfSchemaName;
        this.sqlfTableName = sqlfTableName;

        this.backendDataSourceName = (String) props.remove("backend-data-source-name");
        this.backendCatalogName = (String) props.remove("backend-catalog-name");

        this.backendSchemaName = (String) props.remove("backend-schema-name");
        this.backendSchemaName = StringUtils.hasText(this.backendSchemaName) ? this.backendSchemaName : this.sqlfSchemaName;

        this.backendTableName = (String) props.remove("backend-table-name");
        this.backendTableName = StringUtils.hasText(this.backendTableName) ? this.backendTableName : this.sqlfTableName;

        columnMappings = new ColumnMappings(props);
    }

    public String getBackendTableName() {
        return backendTableName;
    }

    public void setBackendTableName(String backendTableName) {
        this.backendTableName = backendTableName;
    }

    public String getSqlfSchemaName() {
        return sqlfSchemaName;
    }

    public String getSqlfTableName() {
        return sqlfTableName;
    }

    public String getBackendDataSourceName() {
        return backendDataSourceName;
    }

    public String getBackendCatalogName() {
        return backendCatalogName;
    }

    public String getBackendSchemaName() {
        return backendSchemaName;
    }

    public ColumnMappings getColumnMappings() {
        return columnMappings;
    }

    public static synchronized TableMapping getMapping(String schemaName, String tableName) {
        try {
            String sch = schemaName.toLowerCase();
            String tbl = tableName.toLowerCase();
            String key = sch + '.' + tbl;

            TableMapping tableMapping = tableMappings.get(key);

            if (tableMapping != null) {
                LOG.debug("mappings for schemaName {}, tableName {} already exist", schemaName, tableName);
                return tableMapping;
            }

            LOG.info("loading table mapping for schemaName {}, tableName {}", schemaName, tableName);

            String fn = sch + '.' + tbl + "-table-mapping.properties";
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fn);

            if (is == null) {
                LOG.info("mapping file {} not found for schemaName={}, tableName={} - using defaults", fn, schemaName, tableName);
                tableMapping = new TableMapping(schemaName, tableName);
            } else {
                LOG.info("mapping file {} found for schemaName={}, tableName={} - loading properties", fn, schemaName, tableName);
                Properties props = new Properties();
                props.load(is);
                tableMapping = new TableMapping(schemaName, tableName, props);
            }

            tableMappings.put(key, tableMapping);

            LOG.info("tableMapping={}", tableMapping);

            return tableMapping;
        } catch (IllegalArgumentException x) {
            throw x;
        } catch (Exception x) {
            throw new IllegalArgumentException(x.toString(), x);
        }
    }

    @Override
    public String toString() {
        return "TableMapping [sqlfSchemaName=" + sqlfSchemaName + ", sqlfTableName=" + sqlfTableName + ", backendCatalogName="
                + backendCatalogName + ", backendSchemaName=" + backendSchemaName + ", backendTableName=" + backendTableName
                + ", backendDataSourceName=" + backendDataSourceName + ", columnMappings=" + columnMappings + "]";
    }

}
