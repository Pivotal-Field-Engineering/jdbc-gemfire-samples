package com.kp.ttg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Iterator;
import java.util.Set;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kp.ttg.MetadataHelper.MetadataHolder;
import com.vmware.sqlfire.procedure.ProcedureExecutionContext;

public class BulkLoader extends DSBase implements Constants {

    private static final Logger LOG = LoggerFactory.getLogger(BulkLoader.class);

    public static void load(String schemaName, String tableName, ProcedureExecutionContext ctx) throws Exception {
        try {
            long start = System.currentTimeMillis();
            LOG.info("schemaName={}, tableName={}", schemaName, tableName);

            TableMapping tmap = TableMapping.getMapping(schemaName, tableName);
            LOG.info("schemaName={}, tableName={}, tmap={}", schemaName, tableName, tmap);

            String backendDataSourceName = tmap.getBackendDataSourceName();
            String backendTableName = tmap.getBackendTableName();
            String backendCatalogName = tmap.getBackendCatalogName();
            String backendSchemaName = tmap.getBackendSchemaName();

            DataSourceInfo backendDSInfo = DataSourceInfo.getInfo(backendDataSourceName);
            LOG.info("schemaName={}, tableName={}, backendDSInfo={}", schemaName, tableName, backendDSInfo);

            CaseSensitivity backendCS = backendDSInfo.getCaseSensitivity();

            DataSource backendDS = DataSourceHelper.getSource(backendDataSourceName);

            MetadataHolder sqlfMeta = MetadataHelper.getMetadata(EMBEDDED_DATA_SOURCE_NAME, null, schemaName, tableName,
                    CaseSensitivity.none);
            Set<String> cols = sqlfMeta.columnNames;

            MetadataHolder backendMeta = MetadataHelper.getMetadata(backendDataSourceName, backendCatalogName, backendSchemaName,
                    backendTableName, backendCS);
            Set<String> backendSelectCols = backendMeta.columnNames;

            String selectSQL = buildSelect(backendCatalogName, backendSchemaName, backendTableName, backendSelectCols, null);
            LOG.info("schemaName={}, tableName={}, selectSQL={}", schemaName, tableName, selectSQL);

            String insertSQL = buildInsert(null, schemaName, tableName, cols);
            LOG.info("schemaName={}, tableName={}, insertSQL={}", schemaName, tableName, insertSQL);

            LOG.info("insert sqlfCols={}, tmap.getColumnMappings()={} ", cols, tmap.getColumnMappings());

            Connection sfc = ctx.getConnection();
            Connection conn = backendDS.getConnection();

            int count = 0;

            try {
                PreparedStatement bstmt = sfc.prepareStatement(insertSQL);
                PreparedStatement stmt = conn.prepareStatement(selectSQL);
                ResultSet res = stmt.executeQuery();

                try {
                    int bc = 0;
                    while (res.next()) {
                        ++count;
                        insert(res, cols, tmap.getColumnMappings(), bstmt);

                        if (++bc >= 1000) {
                            bc = 0;
                            bstmt.executeBatch();
                            LOG.info("schemaName={}, tableName={}, count={} - executing batch insert", schemaName, tableName, count);
                        }
                    }

                    // remainder
                    if (bc > 0) {
                        bstmt.executeBatch();
                        LOG.info("schemaName={}, tableName={}, count={} - executing final batch insert", schemaName, tableName, count);
                    }
                } finally {
                    res.close();
                    stmt.close();
                    bstmt.close();
                }
            } finally {
                conn.close();
            }

            long end = System.currentTimeMillis();
            long delta = end - start;

            if (delta > 0) {
                long rate = 1000L * count / delta; // records/second
                String unit = "milliseconds";

                if (delta > ONE_MINUTE) {
                    delta /= ONE_MINUTE;
                    unit = "minutes";
                } else if (delta > ONE_SECOND) {
                    delta /= ONE_SECOND;
                    unit = "seconds";
                }

                LOG.info("inserted {} records in {} {} ({} records/second): schemaName={}, tableName={}", count, delta, unit, rate,
                        schemaName, tableName);
            } else {
                LOG.info("inserted {} records: schemaName={}, tableName={}", count, schemaName, tableName);
            }
        } catch (Exception x) {
            LOG.error(x.toString(), x);
            throw x;
        }
    }

    private static void insert(ResultSet res, Set<String> sqlfCols, ColumnMappings cmap, PreparedStatement bstmt) throws Exception {
     	int i = 1;
        for (Iterator<String> iter = sqlfCols.iterator(); iter.hasNext();) {
            String sqlfCol = iter.next();
            String backendCol = cmap.getForwardMapping(sqlfCol);
            Object o = res.getObject(backendCol);
            bstmt.setObject(i++, o);
        }

        bstmt.addBatch();
    }
}
