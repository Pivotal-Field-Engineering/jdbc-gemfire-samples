package com.kp.ttg;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WriterBase extends DSBase {

    protected boolean disableInsert = false;
    protected boolean disableUpdate = false;
    protected boolean disableDelete = false;

    protected final Logger LOG = LoggerFactory.getLogger(getClass());

    @Override
    protected void doInit(String initStr) {
        super.doInit(initStr);

        disableInsert = Boolean.parseBoolean(properties.getProperty("disableInsert",
                System.getProperty("org.kp.writer.disable-insert")));
        disableUpdate = Boolean.parseBoolean(properties.getProperty("disableUpdate",
                System.getProperty("org.kp.writer.disable-update")));
        disableDelete = Boolean.parseBoolean(properties.getProperty("disableDelete",
                System.getProperty("org.kp.writer.disable-delete")));

        LOG.info("disableInsert={}, disableUpdate={}, disableDelete={}", disableInsert, disableUpdate, disableDelete);

        if (disableInsert && disableUpdate && disableDelete) {
            throw new IllegalStateException("disableInsert, disableUpdate, and disableDelete cannot all be true");
        }
    }

    protected void processDelete(String schemaName, String tableName, Map<String, Object> keys) throws Exception {
        LOG.debug("schemaName={}, tableName={}, keys={}", schemaName, tableName, keys);
        executeDeleteMapped(schemaName, tableName, keys);
    }

    protected void processUpdate(String schemaName, String tableName, Map<String, Object> keys, Map<String, Object> cols)
            throws Exception {
        LOG.debug("schemaName={}, tableName={}, keys={}, cols={}", schemaName, tableName, keys, cols);
        executeUpdateMapped(schemaName, tableName, cols, keys);
    }

    protected void processInsert(String schemaName, String tableName, Map<String, Object> cols) throws Exception {
        LOG.debug("schemaName={}, tableName={}, cols={}", schemaName, tableName, cols);
        executeInsertMapped(schemaName, tableName, cols);
    }
}
