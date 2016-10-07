package com.kp.ttg;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.sql.DataSource;

import org.springframework.util.Assert;

import com.kp.ttg.MetadataHelper.MetadataHolder;

public class MemberRowLoader extends SimpleRowLoader {

    public MemberRowLoader() {
    }

    @Override
    public void init(String initStr) throws SQLException {
        super.init(initStr);
    }

    @Override
    public Object getRow(String schemaName, String tableName, Object[] primaryKey) throws SQLException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("schemaName={}, tableName={}, primaryKey={}", schemaName, tableName, Arrays.toString(primaryKey));
        }

        Object orow = super.getRow(schemaName, tableName, primaryKey);

        if (orow == null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("no record found: schemaName={}, tableName={}, primaryKey={}", schemaName, tableName,
                        Arrays.toString(primaryKey));
            }

            return null;
        }

        Assert.isTrue(primaryKey.length == 2, "Expected two columns for member primary key");
        Assert.isTrue(primaryKey[0].getClass() == String.class, "Expected key column for member to be a String");
        Assert.isTrue(primaryKey[1].getClass() == String.class, "Expected key column for member to be a String");
        String mrn = (String) primaryKey[0];
        String mrnPrefix = (String) primaryKey[1];

        Map<String, Object> params = new TreeMap<>();
        params.put("mrn_prefix", mrnPrefix);
        params.put("mrn", mrn);

        copyTable(schemaName, "member_phone", params);

        return orow;
    }

    private void copyTable(String schemaName, String tableName, Map<String, Object> params) throws SQLException {
        LOG.debug("schemaName={}, tableName={}, params={}", schemaName, tableName, params);
        
        DataSourceInfo info = DataSourceInfo.getInfo(EMBEDDED_DATA_SOURCE_NAME);

        MetadataHolder meta = MetadataHelper.getMetadata(EMBEDDED_DATA_SOURCE_NAME, null, schemaName, tableName,
                info.getCaseSensitivity());

        Set<String> cols = meta.columnNames;
        Map<String, Integer> colTypes = meta.columnTypes;

        DataSource source = DataSourceHelper.getSource(EMBEDDED_DATA_SOURCE_NAME);
        Connection conn = source.getConnection();

        try {
            LOG.debug("deleting rows: schemaName={}, tableName={}, params={}", schemaName, tableName, params);
            executeDelete(conn, null, schemaName, tableName, params, colTypes);

            LOG.debug("querying backend database: schemaName={}, tableName={}, params={}", schemaName, tableName, params);
            List<List<Object>> rows = queryTableMapped(schemaName, tableName, params);

            if (LOG.isDebugEnabled()) {
                LOG.debug("got {} rows: schemaName={}, tableName={}, params={}", rows == null ? 0 : rows.size(), schemaName,
                        tableName, params);
            }

            if (rows != null && !rows.isEmpty()) {
                int count = 0;

                for (List<Object> row : rows) {
                    Map<String, Object> insParams = new TreeMap<>();

                    int index = 0;
                    for (String col : cols) {
                        insParams.put(col, row.get(index++));
                    }
                    
                    ++count;
                    LOG.debug("inserting row {}: schemaName={}, tableName={}, insParams={}", count, schemaName, tableName,
                            insParams);
                    executeInsert(conn, null, schemaName, tableName, insParams, colTypes);
                }
            } else {
                LOG.debug("no rows: schemaName={}, tableName={}, params={}", schemaName, tableName, params);
            }
        } finally {
            conn.close();
        }
    }
}
