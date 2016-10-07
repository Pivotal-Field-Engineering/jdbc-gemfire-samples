package com.kp.ttg;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.springframework.util.Assert;

import com.kp.ttg.MetadataHelper.MetadataHolder;

public class SimpleRowLoader extends RowLoaderBase {

    public SimpleRowLoader() {
    }

    @Override
    public Object getRow(String schemaName, String tableName, Object[] primaryKey) throws SQLException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("schemaName={}, tableName={}, primaryKey={}", schemaName, tableName, Arrays.toString(primaryKey));
        }

        MetadataHolder sqlfMeta = MetadataHelper.getMetadata(EMBEDDED_DATA_SOURCE_NAME, null, schemaName, tableName,
                CaseSensitivity.none);

        Set<String> keyCols = sqlfMeta.keyColumnNames;
        Assert.isTrue(primaryKey.length == keyCols.size(), "Invalid metadata - incorrect number of primary key columns in sqlfire");

        Map<String, Object> params = new TreeMap<>();

        int index = 0;
        for (String keyCol : keyCols) {
            params.put(keyCol, primaryKey[index++]);
        }

        LOG.debug("schemaName={}, tableName={}, params={}", schemaName, tableName, params);

        List<List<Object>> rows = queryTableMapped(schemaName, tableName, params);

        LOG.debug("schemaName={}, tableName={}, rows={}", schemaName, tableName, rows);

        if (rows == null || rows.isEmpty()) {
            return null;
        }

        Assert.isTrue(rows.size() < 2, "Expected zero or one row");

        List<Object> row = rows.get(0);
        LOG.debug("schemaName={}, tableName={}, row={}", schemaName, tableName, row);

        return row;
    }
}
