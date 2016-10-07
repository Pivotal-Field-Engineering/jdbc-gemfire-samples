package com.kp.ttg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

public class MemberLookupRowLoader extends RowLoaderBase {

    public MemberLookupRowLoader() {
    }

    @Override
    public Object getRow(String schemaName, String tableName, Object[] primaryKey) throws SQLException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("schemaName={}, tableName={}, primaryKey={}", schemaName, tableName, Arrays.toString(primaryKey));
        }

        Assert.isTrue(primaryKey.length == 2, "Invalid number of primary key fields");
        String mrn = primaryKey[0].toString();
        String mrnPrefix = primaryKey[1].toString();

        LOG.debug("schemaName={}, tableName={}, mrn={}, mrnPrefix={}", schemaName, tableName, mrn, mrnPrefix);

        TableMapping tmap = TableMapping.getMapping(schemaName, "member");

        LOG.debug("schemaName={}, tableName={}, tmap={}", schemaName, tableName, tmap);

        String backendDataSourceName = tmap.getBackendDataSourceName();
        DataSource source = DataSourceHelper.getSource(backendDataSourceName);

        String backendCatalogName = tmap.getBackendCatalogName();
        String backendSchemaName = tmap.getBackendSchemaName();
        String backendTableName = tmap.getBackendTableName();

        StringBuilder buf = new StringBuilder();

        buf.append("select member_id from ");

        if (StringUtils.hasText(backendCatalogName)) {
            buf.append(backendCatalogName).append('.');
        }

        if (StringUtils.hasText(backendSchemaName)) {
            buf.append(backendSchemaName).append('.');
        }

        buf.append(backendTableName).append(" where mrn=? and mrn_prefix=?");

        String sql = buf.toString();

        if (LOG.isDebugEnabled()) {
            LOG.debug("schemaName={}, tableName={}, sql={}", schemaName, tableName, sql);
        }

        Timer timer = Timer.getTimer(StatementType.select, backendSchemaName, backendTableName);
        timer.start();

        Connection conn = source.getConnection();

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, mrn);
            stmt.setString(2, mrnPrefix);

            List<Object> data = null;
            ResultSet res = stmt.executeQuery();

            try {
                if (res.next()) {
                    data = new ArrayList<>();
                    long memberId = res.getLong("member_id");

                    data.add(mrn);
                    data.add(mrnPrefix);
                    data.add(memberId);

                    if (res.next()) {
                        throw new SQLException("Multiple member IDs found for MRN " + mrn + ", MRN Prefix " + mrnPrefix);
                    }

                    LOG.debug("schemaName={}, tableName={}, mrn={}, mrnPrefix={}, data={}", schemaName, tableName, mrn, mrnPrefix,
                            data);
                } else if (LOG.isDebugEnabled()) {
                    LOG.debug("no records found: schemaName={}, tableName={}, mrn={}, mrnPrefix={}", schemaName, tableName, mrn,
                            mrnPrefix);
                }

                timer.end();
                return data;
            } finally {
                res.close();
            }
        } finally {
            conn.close();
        }
    }
}
