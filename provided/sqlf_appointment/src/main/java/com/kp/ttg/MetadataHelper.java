package com.kp.ttg;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

public class MetadataHelper implements Constants {

    private static final Map<String, MetadataHolder> metadata = new HashMap<>();

    private static final Logger LOG = LoggerFactory.getLogger(MetadataHelper.class);

    public static final class MetadataHolder {

        public Set<String> columnNames = new LinkedHashSet<>();
        public Set<String> keyColumnNames = new LinkedHashSet<>();
        public Map<String, Integer> columnTypes = new HashMap<>();
        public Map<String, Integer> columnSizes = new HashMap<>();

        @Override
        public String toString() {
            return "MetadataHolder [columnNames=" + columnNames + ", keyColumnNames=" + keyColumnNames + ", columnTypes="
                    + columnTypes + ", columnSizes=" + columnSizes + "]";
        }

    }

    public static synchronized MetadataHolder getMetadata(String dsName, String catalogName, String schemaName, String tableName,
            CaseSensitivity caseSen) throws SQLException {

        if (!StringUtils.hasText(dsName)) {
            dsName = System.getProperty(DEFAULT_DATA_SOURCE_NAME_PROPERTY);
            Assert.notNull(dsName, "Required property '" + DEFAULT_DATA_SOURCE_NAME_PROPERTY + "' not defined");
        }

        String catKey = StringUtils.hasText(catalogName) ? catalogName.toLowerCase() : "null";
        String schKey = StringUtils.hasText(schemaName) ? schemaName.toLowerCase() : "null";

        String key = new StringBuilder().append(dsName).append('.').append(catKey).append('.').append(schKey).append('.')
                .append(tableName.toLowerCase()).toString();

        MetadataHolder m = metadata.get(key);

        if (m != null) {
            LOG.debug("metadata already exists for dsName={}, catalogName={}, schemaName={}, tableName={}", dsName, catalogName,
                    schemaName, tableName);
            return m;
        }

        LOG.info("loading metadata for dsName={}, catalogName={}, schemaName={}, tableName={}", dsName, catalogName, schemaName,
                tableName);

        DataSource ds = DataSourceHelper.getSource(dsName);
        DataSourceInfo info = DataSourceInfo.getInfo(dsName);

        String cat = info.fixCase(catalogName);
        String sch = info.fixCase(schemaName);
        String tbl = info.fixCase(tableName);
        LOG.info("fixed case: cat={}, sch={}, tbl={}", cat, sch, tbl);

        Connection conn = ds.getConnection();

        try {
            DatabaseMetaData meta = conn.getMetaData();

            ResultSet metaTable = meta.getTables(cat, sch, tbl, null);

            if (!metaTable.next()) {
                LOG.error("cannot find metadata for dsName={}, catalogName={}, schemaName={}, tableName={}", dsName, catalogName,
                        schemaName, tableName);
                throw new IllegalArgumentException("Unknown table " + schemaName + '.' + tableName);
            }

            ResultSet metaCols = meta.getColumns(cat, sch, tbl, null);
            ResultSet metaKeys = meta.getPrimaryKeys(cat, sch, tbl);

            m = new MetadataHolder();

            while (metaCols.next()) {
                String colName = metaCols.getString("COLUMN_NAME").toLowerCase();
                int colType = metaCols.getInt("DATA_TYPE");
                int colSize = metaCols.getInt("COLUMN_SIZE");
                m.columnNames.add(colName);
                m.columnTypes.put(colName, colType);
                m.columnSizes.put(colName, colSize);
            }

            while (metaKeys.next()) {
                String colName = metaKeys.getString("COLUMN_NAME").toLowerCase();
                m.keyColumnNames.add(colName);
            }

            metadata.put(key, m);
            LOG.debug("metadata for dsName={}, catalogName={}, schemaName={}, tableName={}: {}", dsName, catalogName, schemaName,
                    tableName, m);

            return m;
        } finally {
            conn.close();
        }
    }
}
