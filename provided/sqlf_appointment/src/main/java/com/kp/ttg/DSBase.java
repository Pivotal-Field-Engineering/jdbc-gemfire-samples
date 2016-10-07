package com.kp.ttg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.kp.ttg.MetadataHelper.MetadataHolder;

public class DSBase implements Constants {

    protected Properties properties = new Properties();

    protected final Logger LOG = LoggerFactory.getLogger(getClass());

    public DSBase() {
    }

    protected void doInit(String initStr) {
        LOG.info("initStr={}", initStr);

        parseProps(System.getProperty("org.kp.dsbase.init-str"));
        parseProps(initStr);

        LOG.info("properties={}", properties);
    }

    protected void parseProps(String s) {
        if (StringUtils.hasText(s)) {
            String[] ss = s.split("\\|");

            for (String st : ss) {
                String[] sst = st.split("=");
                Assert.isTrue(sst.length == 2, "Invalid init string: " + s);
                properties.setProperty(sst[0], sst[1]);
            }
        }
    }

    protected static String parseTableName(String qualTblNm) {
        String[] ss = qualTblNm.split("\\.");
        return ss.length == 1 ? ss[0] : ss[1];
    }

    protected DataSource getDataSource(TableMapping tmap) {
        String backendDataSourceName = tmap.getBackendDataSourceName();
        return DataSourceHelper.getSource(backendDataSourceName);
    }

    protected DataSourceInfo getDataSourceInfo(TableMapping tmap) {
        String backendDataSourceName = tmap.getBackendDataSourceName();
        return DataSourceInfo.getInfo(backendDataSourceName);
    }

    protected static String buildSelect(String catalogName, String schemaName, String tableName, Collection<String> selectCols,
            Collection<String> keyCols) {
        return buildSelect(catalogName, schemaName, tableName, selectCols, keyCols, null, null);
    }

    protected static String buildSelect(String catalogName, String schemaName, String tableName, Collection<String> selectCols,
            Collection<String> keyCols, Collection<String> orderByCols, String descOrAsc) {

        StringBuilder buf = new StringBuilder();

        buf.append("select ");

        addColumnsList(selectCols, buf);

        buf.append(" from ");

        if (StringUtils.hasText(catalogName)) {
            buf.append(catalogName).append('.');
        }

        if (StringUtils.hasText(schemaName)) {
            buf.append(schemaName).append('.');
        }

        buf.append(tableName);

        if (keyCols != null && !keyCols.isEmpty()) {
            buf.append(" where ");
            addColumnsForWhere(keyCols, buf);
        }

        if (orderByCols != null && !orderByCols.isEmpty()) {
            buf.append(" order by ");
            addColumnsForOrderBy(orderByCols, descOrAsc, buf);
        }

        return buf.toString();
    }

    protected static String buildDelete(String catalogName, String schemaName, String tableName, Collection<String> keyCols) {
        StringBuilder buf = new StringBuilder();

        buf.append("delete from ");

        if (StringUtils.hasText(catalogName)) {
            buf.append(catalogName).append('.');
        }

        if (StringUtils.hasText(schemaName)) {
            buf.append(schemaName).append('.');
        }

        buf.append(tableName);

        if (!keyCols.isEmpty()) {
            buf.append(" where ");
            addColumnsForWhere(keyCols, buf);
        }

        return buf.toString();
    }

    protected static String buildInsert(String catalogName, String schemaName, String tableName, Collection<String> cols) {
        StringBuilder buf = new StringBuilder();

        buf.append("insert into ");

        if (StringUtils.hasText(catalogName)) {
            buf.append(catalogName).append('.');
        }

        if (StringUtils.hasText(schemaName)) {
            buf.append(schemaName).append('.');
        }

        buf.append(tableName).append(" (");

        addColumnsList(cols, buf);

        buf.append(") values (");

        for (Iterator<String> iter = cols.iterator(); iter.hasNext();) {
            iter.next();
            buf.append("?");

            if (iter.hasNext()) {
                buf.append(',');
            }
        }

        buf.append(')');

        return buf.toString();
    }

    protected static String buildUpdate(String catalogName, String schemaName, String tableName, Collection<String> updateCols,
            Collection<String> keyCols) {
        StringBuilder buf = new StringBuilder();

        buf.append("update ");

        if (StringUtils.hasText(catalogName)) {
            buf.append(catalogName).append('.');
        }

        if (StringUtils.hasText(schemaName)) {
            buf.append(schemaName).append('.');
        }

        buf.append(tableName).append(" set ");

        addColumnsForUpdate(updateCols, buf);

        buf.append(" where ");

        addColumnsForWhere(keyCols, buf);

        return buf.toString();
    }

    protected static int executeDelete(Connection conn, String catalogName, String schemaName, String tableName,
            Map<String, Object> params, Map<String, Integer> colTypes) throws SQLException {

        Set<String> keyCols = params.keySet();

        String sql = buildDelete(catalogName, schemaName, tableName, keyCols);

        Timer timer = Timer.getTimer(StatementType.delete, schemaName, tableName);
        timer.start();

        PreparedStatement stmt = conn.prepareStatement(sql);

        try {
            if (!keyCols.isEmpty()) {
                int index = 1;
                for (Iterator<Map.Entry<String, Object>> iter = params.entrySet().iterator(); iter.hasNext();) {
                    Map.Entry<String, Object> entry = iter.next();
                    String col = entry.getKey();
                    Object val = entry.getValue();
                    Integer colType = colTypes.get(col.toLowerCase());

                    if (colType == null) {
                        stmt.setObject(index++, val);
                    } else {
                        stmt.setObject(index++, val, colType);
                    }
                }
            }

            int cnt = stmt.executeUpdate();
            timer.end();

            return cnt;
        } finally {
            stmt.close();
        }
    }

    protected static int executeInsert(Connection conn, String catalogName, String schemaName, String tableName,
            Map<String, Object> params, Map<String, Integer> colTypes) throws SQLException {

        Set<String> cols = params.keySet();

        String sql = buildInsert(catalogName, schemaName, tableName, cols);

        Timer timer = Timer.getTimer(StatementType.insert, schemaName, tableName);
        timer.start();

        PreparedStatement stmt = conn.prepareStatement(sql);

        try {
            int index = 1;
            for (Iterator<Map.Entry<String, Object>> iter = params.entrySet().iterator(); iter.hasNext();) {
                Map.Entry<String, Object> entry = iter.next();
                String col = entry.getKey();
                Object val = entry.getValue();
                Integer colType = colTypes.get(col.toLowerCase());

                if (colType == null) {
                    stmt.setObject(index++, val);
                } else {
                    stmt.setObject(index++, val, colType);
                }
            }

            int cnt = stmt.executeUpdate();
            timer.end();

            return cnt;
        } finally {
            stmt.close();
        }
    }

    protected static int executeUpdate(Connection conn, String catalogName, String schemaName, String tableName,
            Map<String, Object> keyCols, Map<String, Object> updateCols, Map<String, Integer> colTypes) throws SQLException {

        String sql = buildUpdate(catalogName, schemaName, tableName, updateCols.keySet(), keyCols.keySet());

        Timer timer = Timer.getTimer(StatementType.update, schemaName, tableName);
        timer.start();

        PreparedStatement stmt = conn.prepareStatement(sql);

        try {
            int index = 1;

            for (Iterator<Map.Entry<String, Object>> iter = updateCols.entrySet().iterator(); iter.hasNext();) {
                Map.Entry<String, Object> entry = iter.next();
                String col = entry.getKey();
                Object val = entry.getValue();
                Integer colType = colTypes.get(col.toLowerCase());

                if (colType == null) {
                    stmt.setObject(index++, val);
                } else {
                    stmt.setObject(index++, val, colType);
                }
            }

            for (Iterator<Map.Entry<String, Object>> iter = keyCols.entrySet().iterator(); iter.hasNext();) {
                Map.Entry<String, Object> entry = iter.next();
                String col = entry.getKey();
                Object val = entry.getValue();
                Integer colType = colTypes.get(col.toLowerCase());

                if (colType == null) {
                    stmt.setObject(index++, val);
                } else {
                    stmt.setObject(index++, val, colType);
                }
            }

            int cnt = stmt.executeUpdate();
            timer.end();

            return cnt;
        } finally {
            stmt.close();
        }
    }

    protected int executeDeleteMapped(String schemaName, String tableName, Map<String, Object> params) throws SQLException {

        LOG.debug("schemaName={}, tableName={}, params={}", schemaName, tableName, params);

        TableMapping tmap = TableMapping.getMapping(schemaName, tableName);
        ColumnMappings cmap = tmap.getColumnMappings();

        String backendDataSourceName = tmap.getBackendDataSourceName();
        String backendTableName = tmap.getBackendTableName();
        String backendCatalogName = tmap.getBackendCatalogName();
        String backendSchemaName = tmap.getBackendSchemaName();

        DataSourceInfo dsInfo = getDataSourceInfo(tmap);

        CaseSensitivity backendCaseSensitivity = dsInfo.getCaseSensitivity();

        DataSource source = getDataSource(tmap);

        MetadataHolder sqlfMeta = MetadataHelper.getMetadata(EMBEDDED_DATA_SOURCE_NAME, null, schemaName, tableName,
                CaseSensitivity.upper);

        MetadataHolder backendMeta = MetadataHelper.getMetadata(backendDataSourceName, backendCatalogName, backendSchemaName,
                backendTableName, backendCaseSensitivity);

        Map<String, Integer> backendColTypes = backendMeta.columnTypes;
        Map<String, Object> backendParams = mapParams(sqlfMeta, backendMeta, cmap, params, true);

        Connection conn = source.getConnection();

        try {
            int cnt = executeDelete(conn, backendCatalogName, backendSchemaName, backendTableName, backendParams, backendColTypes);
            return cnt;
        } finally {
            conn.close();
        }
    }

    protected int executeInsertMapped(String schemaName, String tableName, Map<String, Object> params) throws SQLException {

        LOG.debug("schemaName={}, tableName={}, params={}", schemaName, tableName, params);

        TableMapping tmap = TableMapping.getMapping(schemaName, tableName);

        LOG.debug("schemaName={}, tableName={}, params={}, tmap={}", schemaName, tableName, params, tmap);

        ColumnMappings cmap = tmap.getColumnMappings();

        String backendDataSourceName = tmap.getBackendDataSourceName();
        String backendTableName = tmap.getBackendTableName();
        String backendCatalogName = tmap.getBackendCatalogName();
        String backendSchemaName = tmap.getBackendSchemaName();

        DataSourceInfo dsInfo = getDataSourceInfo(tmap);

        LOG.debug("schemaName={}, tableName={}, params={}, dsInfo={}", schemaName, tableName, params, dsInfo);

        CaseSensitivity backendCaseSensitivity = dsInfo.getCaseSensitivity();

        DataSource source = getDataSource(tmap);

        MetadataHolder sqlfMeta = MetadataHelper.getMetadata(EMBEDDED_DATA_SOURCE_NAME, null, schemaName, tableName,
                CaseSensitivity.upper);

        MetadataHolder backendMeta = MetadataHelper.getMetadata(backendDataSourceName, backendCatalogName, backendSchemaName,
                backendTableName, backendCaseSensitivity);

        Map<String, Integer> backendColTypes = backendMeta.columnTypes;
        Map<String, Object> backendParams = mapParams(sqlfMeta, backendMeta, cmap, params, true);

        Connection conn = source.getConnection();

        try {
            int cnt = executeInsert(conn, backendCatalogName, backendSchemaName, backendTableName, backendParams, backendColTypes);
            return cnt;
        } finally {
            conn.close();
        }
    }

    protected int executeUpdateMapped(String schemaName, String tableName, Map<String, Object> updateParams,
            Map<String, Object> keyParams) throws SQLException {

        LOG.debug("schemaName={}, tableName={}, updateParams={}, keyParams={}", schemaName, tableName, updateParams, keyParams);

        TableMapping tmap = TableMapping.getMapping(schemaName, tableName);

        LOG.debug("schemaName={}, tableName={}, updateParams={}, keyParams={}, tmap={}", schemaName, tableName, updateParams,
                keyParams, tmap);

        ColumnMappings cmap = tmap.getColumnMappings();

        String backendDataSourceName = tmap.getBackendDataSourceName();
        String backendTableName = tmap.getBackendTableName();
        String backendCatalogName = tmap.getBackendCatalogName();
        String backendSchemaName = tmap.getBackendSchemaName();

        DataSourceInfo dsInfo = getDataSourceInfo(tmap);

        CaseSensitivity backendCaseSensitivity = dsInfo.getCaseSensitivity();

        DataSource source = getDataSource(tmap);

        MetadataHolder sqlfMeta = MetadataHelper.getMetadata(EMBEDDED_DATA_SOURCE_NAME, null, schemaName, tableName,
                CaseSensitivity.upper);

        MetadataHolder backendMeta = MetadataHelper.getMetadata(backendDataSourceName, backendCatalogName, backendSchemaName,
                backendTableName, backendCaseSensitivity);

        Map<String, Integer> backendColTypes = backendMeta.columnTypes;
        Map<String, Object> backendUpdateParams = mapParams(sqlfMeta, backendMeta, cmap, updateParams, true);
        Map<String, Object> backendKeyParams = mapParams(sqlfMeta, backendMeta, cmap, keyParams, true);

        Connection conn = source.getConnection();

        try {
            int cnt = executeUpdate(conn, backendCatalogName, backendSchemaName, backendTableName, backendUpdateParams,
                    backendKeyParams, backendColTypes);
            return cnt;
        } finally {
            conn.close();
        }
    }

    protected List<Map<String, Object>> queryTable(Connection conn, String catalogName, String schemaName, String tableName,
            Collection<String> selectCols, Map<String, Object> params, Map<String, Integer> colTypes) throws SQLException {

        LOG.debug("catalogName={}, schemaName={}, tableName={}, selectCols={}, params={}, colTypes={}", catalogName, schemaName,
                tableName, selectCols, params, colTypes);

        Set<String> keyCols = params.keySet();

        LOG.debug("catalogName={}, schemaName={}, tableName={}, keyCols={}", catalogName, schemaName, tableName, keyCols);

        String sql = buildSelect(catalogName, schemaName, tableName, selectCols, keyCols);

        LOG.debug("catalogName={}, schemaName={}, tableName={}, sql={}", catalogName, schemaName, tableName, sql);

        Timer timer = Timer.getTimer(StatementType.select, schemaName, tableName);
        timer.start();

        LOG.debug("catalogName={}, schemaName={}, tableName={}, preparing statement", catalogName, schemaName, tableName);

        PreparedStatement stmt = conn.prepareStatement(sql);

        try {
            LOG.debug("catalogName={}, schemaName={}, tableName={}, binding params", catalogName, schemaName, tableName);

            if (!keyCols.isEmpty()) {
                int index = 1;
                for (Iterator<Map.Entry<String, Object>> iter = params.entrySet().iterator(); iter.hasNext();) {
                    Map.Entry<String, Object> entry = iter.next();
                    String col = entry.getKey();
                    Object val = entry.getValue();
                    Integer colType = colTypes.get(col.toLowerCase());

                    if (colType == null) {
                        stmt.setObject(index++, val);
                    } else {
                        stmt.setObject(index++, val, colType);
                    }
                }
            }

            LOG.debug("catalogName={}, schemaName={}, tableName={}, executing query", catalogName, schemaName, tableName);

            List<Map<String, Object>> data = new ArrayList<>();
            ResultSet res = stmt.executeQuery();

            timer.end();

            try {
                while (res.next()) {
                    Map<String, Object> m = new HashMap<>();
                    data.add(m);
                    for (String cn : selectCols) {
                        Object ov = res.getObject(cn);
                        m.put(cn, ov);
                    }
                }

                LOG.debug("catalogName={}, schemaName={}, tableName={}, data={}", catalogName, schemaName, tableName, data);
                return data;
            } finally {
                res.close();
            }
        } finally {
            stmt.close();
        }
    }

    protected List<List<Object>> queryTableMapped(String schemaName, String tableName, Map<String, Object> params)
            throws SQLException {

        LOG.debug("schemaName={}, tableName={}, params={}", schemaName, tableName, params);

        TableMapping tmap = TableMapping.getMapping(schemaName, tableName);
        ColumnMappings cmap = tmap.getColumnMappings();

        String backendDataSourceName = tmap.getBackendDataSourceName();
        String backendTableName = tmap.getBackendTableName();
        String backendCatalogName = tmap.getBackendCatalogName();
        String backendSchemaName = tmap.getBackendSchemaName();

        DataSourceInfo dsInfo = getDataSourceInfo(tmap);

        CaseSensitivity backendCaseSensitivity = dsInfo.getCaseSensitivity();

        DataSource source = getDataSource(tmap);

        MetadataHolder sqlfMeta = MetadataHelper.getMetadata(EMBEDDED_DATA_SOURCE_NAME, null, schemaName, tableName,
                CaseSensitivity.none);

        MetadataHolder backendMeta = MetadataHelper.getMetadata(backendDataSourceName, backendCatalogName, backendSchemaName,
                backendTableName, backendCaseSensitivity);

        Set<String> selectCols = sqlfMeta.columnNames;
        Set<String> backendSelectCols = mapColumnNames(tableName, selectCols, cmap, true, true);
        Map<String, Integer> backendColTypes = backendMeta.columnTypes;

        Map<String, Object> backendParams = mapParams(sqlfMeta, backendMeta, cmap, params, true);

        LOG.debug("schemaName={}, tableName={}, backendSelectCols={}, backendParams={}", schemaName, tableName, backendSelectCols,
                backendParams);

        List<Map<String, Object>> backednRows = null;

        LOG.debug("schemaName={}, tableName={} - getting connection", schemaName, tableName);

        Connection conn = source.getConnection();

        try {
            LOG.debug("schemaName={}, tableName={} - querying backend database", schemaName, tableName);
            backednRows = queryTable(conn, backendCatalogName, backendSchemaName, backendTableName, backendSelectCols,
                    backendParams, backendColTypes);
        } finally {
            LOG.debug("schemaName={}, tableName={} - closing connection", schemaName, tableName);
            conn.close();
        }

        LOG.debug("schemaName={}, tableName={}, extRows={}", schemaName, tableName, backednRows);

        if (backednRows.isEmpty()) {
            return null;
        }

        List<List<Object>> rows = new ArrayList<>();

        for (Map<String, Object> backendRow : backednRows) {
            List<Object> row = new ArrayList<>();

            for (String sqlfCol : selectCols) {
                String backendCol = cmap.getForwardMapping(sqlfCol);

                if (backendCol == null) {
                    row.add(null);
                } else {
                    Object value = backendRow.get(backendCol);

                    if (value != null && value.getClass() == String.class) {
                        String sval = (String) value;

                        Integer sqlfColSize = sqlfMeta.columnSizes.get(sqlfCol);
                        Integer backendColSize = backendMeta.columnSizes.get(backendCol);

                        if (sqlfColSize != null && backendColSize != null && sqlfColSize != backendColSize) {
                            value = adjustPadding(sqlfColSize, sval);
                            LOG.debug(
                                    "adjusted padding: sqlfCol={}, backendCol={}, sqlfColSize={}, backendColSize={}, sval={}, value={}",
                                    sqlfCol, backendCol, sqlfColSize, backendColSize, sval, value);
                        }
                    }

                    row.add(value);
                }
            }

            rows.add(row);
        }

        LOG.debug("schemaName={}, tableName={}, rows={}", schemaName, tableName, rows);

        return rows;
    }

    protected static String adjustPadding(int size, String s) {
        if (s == null) {
            return s;
        }

        if (s.length() > size) {
            return s.substring(s.length() - size);
        } else if (s.length() < size) {
            int diff = size-s.length();
            StringBuilder buf = new StringBuilder();
            
            for (int i = 0; i < diff; ++i) {
                buf.append('0');
            }
            
            buf.append(s);
            
            return buf.toString();
        } else {
            return s;
        }
    }

    protected Map<String, Object> mapParams(MetadataHolder fromMeta, MetadataHolder toMeta, ColumnMappings cmap,
            Map<String, Object> params, boolean forward) {
        Map<String, Object> ret = new TreeMap<>();

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String fromCol = entry.getKey();
            String toCol = forward ? cmap.getForwardMapping(fromCol) : cmap.getReverseMapping(fromCol);
            Assert.notNull(toCol, "Param cannot be null");

            Object val = entry.getValue();

            if (val != null && val.getClass() == String.class) {
                Integer fromColSize = forward ? fromMeta.columnSizes.get(fromCol) : toMeta.columnSizes.get(toCol);
                Integer toColSize = forward ? toMeta.columnSizes.get(toCol) : fromMeta.columnSizes.get(fromCol);

                if (fromColSize != null && toColSize != null && fromColSize != toColSize) {
                    String sval = (String) val;
                    val = adjustPadding(toColSize, sval);
                    LOG.debug("adjusted padding: fromCol={}, toCol={}, fromColSize={}, toColSize={}, sval={}, val={}", fromCol,
                            toCol, fromColSize, toColSize, sval, val);
                }
            }

            ret.put(toCol, val);
        }

        return ret;
    }

    protected static Set<String> mapColumnNames(String table, Set<String> cols, ColumnMappings cmap, boolean forward,
            boolean allowMissing) {
        Set<String> ret = new LinkedHashSet<>();

        for (String col : cols) {
            String mcol = forward ? cmap.getForwardMapping(col) : cmap.getReverseMapping(col);

            if (mcol == null) {
                if (!allowMissing) {
                    throw new IllegalArgumentException("Column mapping for column " + col + " table " + table
                            + " is required but missing");
                }
            }

            if (mcol != null) {
                ret.add(mcol);
            }
        }

        return ret;
    }

    protected static void addColumnsForUpdate(Collection<String> cols, StringBuilder buf) {
        for (Iterator<String> iter = cols.iterator(); iter.hasNext();) {
            String col = iter.next();
            buf.append(col).append("=?");

            if (iter.hasNext()) {
                buf.append(',');
            }
        }
    }

    protected static void addColumnsList(Collection<String> cols, StringBuilder buf) {
        for (Iterator<String> iter = cols.iterator(); iter.hasNext();) {
            String col = iter.next();
            buf.append(col);

            if (iter.hasNext()) {
                buf.append(',');
            }
        }
    }

    protected static void addColumnsForOrderBy(Collection<String> cols, String descOrAsc, StringBuilder buf) {
        for (Iterator<String> iter = cols.iterator(); iter.hasNext();) {
            String col = iter.next();
            buf.append(col);

            if (StringUtils.hasText(descOrAsc)) {
                buf.append(' ').append(descOrAsc);
            }

            if (iter.hasNext()) {
                buf.append(',');
            }
        }
    }

    protected static void addColumnsForWhere(Collection<String> cols, StringBuilder buf) {
        for (Iterator<String> iter = cols.iterator(); iter.hasNext();) {
            String col = iter.next();
            buf.append(col).append("=?");

            if (iter.hasNext()) {
                buf.append(" and ");
            }
        }
    }
}
