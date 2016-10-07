package com.kp.ttg;

import java.sql.Connection;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;
import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.kp.ttg.MetadataHelper.MetadataHolder;

public class UpdateAppt extends DSBase {

    private static boolean failOnDuplicates = false;
    private static boolean failOnMissing = false;
    private static boolean failOnOldTimestamp = false;

    // absolute success
    private static final int success = 0;

    // warnings
    private static final int missingOnUpdate = 10;
    private static final int duplicateOnInsert = 11;
    private static final int oldTimestamp = 12;

    // errors
    private static final int missingOnUpdateIgnored = 20;
    private static final int duplicateOnInsertIgnored = 21;
    private static final int oldTimestampIgnored = 22;

    // general error
    private static final int generalError = 99;

    private static final String checked_timestamp_column_name = "event_datetime";
    private static final String CREATE_SYSTIMESTAMP = "create_timestamp";
    private static final String UPDATE_SYSTIMESTAMP = "update_timestamp";

    private static final UpdateAppt instance = new UpdateAppt();
    private static final Logger LOG = LoggerFactory.getLogger(UpdateAppt.class);

    static {
        failOnDuplicates = Boolean.parseBoolean(System.getProperty("org.kp.update-appt.fail-on-duplicates", "false"));
        failOnMissing = Boolean.parseBoolean(System.getProperty("org.kp.update-appt.fail-on-missing", "false"));
        failOnOldTimestamp = Boolean.parseBoolean(System.getProperty("org.kp.update-appt.fail-on-old-timestamp", "false"));
    }

    public static void updateAppointment(String oper, String data, int[] retCode, String[] retMsg) throws Exception {
        instance.update(oper, data, retCode, retMsg);
    }

    private void update(String oper, String data, int[] retCode, String[] retMsg) throws Exception {
        try {
            LOG.debug("failOnDuplicates={}, failOnMissing={}, failOnOldTimestamp={}", failOnDuplicates, failOnMissing,
                    failOnOldTimestamp);
            LOG.debug("oper={}, data={}", oper, data);
            boolean update = !"new".equalsIgnoreCase(oper);

            MetadataHolder meta = MetadataHelper.getMetadata(EMBEDDED_DATA_SOURCE_NAME, null, "dbo", "appointment",
                    CaseSensitivity.upper);

            Set<String> cols = meta.columnNames;
            Map<String, Integer> colTypes = meta.columnTypes;

            Map<String, String> rawParams = parseData(data, cols);
            LOG.debug("rawParams={}", rawParams);

            Map<String, Object> params = convertParams(rawParams, colTypes);
            LOG.debug("params={}", params);

            String apptId = (String) params.get("appointment_id");
            Assert.notNull(apptId, "Missing column appointment_id");
            LOG.debug("apptId={}", apptId);

            Timestamp newTs = (Timestamp) params.get(checked_timestamp_column_name);
            Assert.notNull(newTs, "Missing column " + checked_timestamp_column_name);
            LOG.debug("apptId={}, newTs={}", apptId, newTs);

            Map<String, Object> keyParams = new HashMap<>();
            keyParams.put("appointment_id", apptId);

            DataSource source = DataSourceHelper.getSource(EMBEDDED_DATA_SOURCE_NAME);
            Connection conn = source.getConnection();

            try {
            	Map<String,Timestamp> mapUpdateTS =getUpdateTS(conn, colTypes, apptId);
                Timestamp currTs = mapUpdateTS.get(checked_timestamp_column_name);
                Timestamp create_sysTime= mapUpdateTS.get(CREATE_SYSTIMESTAMP);
                LOG.debug("apptId={}, currTs={}", apptId, currTs);
                Object currentSysTime = new java.sql.Timestamp(Calendar.getInstance().getTimeInMillis());
                if (update) {
                    LOG.debug("updating: apptId={}", apptId);
                    params.put(CREATE_SYSTIMESTAMP, create_sysTime==null? currentSysTime : create_sysTime);
                    params.put(UPDATE_SYSTIMESTAMP, currentSysTime);
                    if (currTs == null) {
                        String msg = String.format("Missing record on update for appointment %s", apptId);
                        LOG.error(msg);

                        if (failOnMissing) {
                            LOG.debug("updating, not replacing record: apptId={}", apptId);
                            retCode[0] = missingOnUpdate;
                            retMsg[0] = msg;
                        } else {
                            LOG.debug("updating, inserting new record: apptId={}", apptId);
                            executeInsert(conn, null, "dbo", "appointment", params, colTypes);
                            retCode[0] = missingOnUpdateIgnored;
                            retMsg[0] = msg;
                        }
                    } else if (newTs.after(currTs)) {
                        LOG.debug("updating, replacing existing record: apptId={}", apptId);
                        executeDelete(conn, null, "dbo", "appointment", keyParams, colTypes);
                        executeInsert(conn, null, "dbo", "appointment", params, colTypes);
                        retCode[0] = success;
                        retMsg[0] = String.format("Update to appointment %s succeeded", apptId);
                    } else {
                        String msg = String
                                .format("Trying to update record with older update timestamp for appointment %s", apptId);
                        LOG.error(msg);

                        if (failOnOldTimestamp) {
                            LOG.debug("updating, not replacing existing newer record: apptId={}", apptId);
                            retCode[0] = oldTimestamp;
                            retMsg[0] = msg;
                        } else {
                            LOG.debug("updating, replacing existing newer record: apptId={}", apptId);
                            executeDelete(conn, null, "dbo", "appointment", keyParams, colTypes);
                            executeInsert(conn, null, "dbo", "appointment", params, colTypes);
                            retCode[0] = oldTimestampIgnored;
                            retMsg[0] = msg;
                        }
                    }
                } else {
                    LOG.debug("creating: apptId={}", apptId);
                    params.put(CREATE_SYSTIMESTAMP, currentSysTime);
                    if (currTs == null) {
                        LOG.debug("creating, no existing record: apptId={}", apptId);
                        executeInsert(conn, null, "dbo", "appointment", params, colTypes);
                        retCode[0] = success;
                        retMsg[0] = String.format("Created appointment %s successfully", apptId);
                    } else {
                        String msg = String.format("Attempt to insert record for existing appointment %s", apptId);
                        LOG.error(msg);

                        if (failOnDuplicates) {
                            LOG.debug("creating, not replacing record: apptId={}", apptId);
                            retCode[0] = duplicateOnInsert;
                            retMsg[0] = msg;
                        } else {
                            LOG.debug("creating, replacing existing record: apptId={}", apptId);
                            executeDelete(conn, null, "dbo", "appointment", keyParams, colTypes);
                            executeInsert(conn, null, "dbo", "appointment", params, colTypes);
                            retCode[0] = duplicateOnInsertIgnored;
                            retMsg[0] = msg;
                        }
                    }
                }
            } finally {
                conn.close();
            }
        } catch (Exception x) {
            LOG.error(x.toString(), x);
            retCode[0] = generalError;
            retMsg[0] = x.getMessage();
        }

        LOG.debug("returning: retCode={}, retMsg={}", retCode[0], retMsg[0]);
    }

    private Map<String,Timestamp> getUpdateTS(Connection conn, Map<String, Integer> colTypes, String apptId) throws Exception {
        LOG.debug("apptId={}", apptId);
        Map<String, Object> keyParams = new HashMap<>();
        keyParams.put("appointment_id", apptId);

        Set<String> selCols = new HashSet<>();
        selCols.add(checked_timestamp_column_name);
        selCols.add(CREATE_SYSTIMESTAMP);

        List<Map<String, Object>> ret = queryTable(conn, null, "dbo", "appointment", selCols, keyParams, colTypes);
        Map<String, Timestamp> map = new HashMap<String, Timestamp>();
        map.put(checked_timestamp_column_name,ret == null || ret.isEmpty() ? null : (Timestamp) ret.get(0).get(checked_timestamp_column_name));
        map.put(CREATE_SYSTIMESTAMP,ret == null || ret.isEmpty() ? null : (Timestamp) ret.get(0).get(CREATE_SYSTIMESTAMP));
        return map;
        
    }

    private static Map<String, Object> convertParams(Map<String, String> rawParams, Map<String, Integer> colTypes) throws Exception {
        Map<String, Object> params = new HashMap<>();

        for (Map.Entry<String, String> rawParam : rawParams.entrySet()) {
            String col = rawParam.getKey();
            String sval = rawParam.getValue();
            if(col.equalsIgnoreCase("APPT_REASON_DESC") && sval != null){
            	sval=sval.replace("&#61;", "=");
            	LOG.debug("Decoded value=="+sval);
            }
            Integer colType = colTypes.get(col);
            Object value = null;

            switch (colType) {
                case Types.CHAR:
                case Types.VARCHAR: {
                    value = sval;
                    break;
                }
                case Types.TIMESTAMP: {
                    long t = DatatypeConverter.parseDateTime(sval).getTimeInMillis();
                    value = new java.sql.Timestamp(t);
                    break;
                }
                case Types.BIGINT: {
                    value = new Long(sval);
                    break;
                }
                default:
                    throw new IllegalArgumentException("Cannot handle sql type " + colType);
            }

            params.put(col, value);
        }

        return params;
    }

    private static Map<String, String> parseData(String data, Set<String> cols) {
        Map<String, String> map = new HashMap<>();

        String[] ss1 = data.split("\\|");
        Assert.isTrue(ss1.length > 0, "Missing parameters in data string");

        for (String ss : ss1) {
            String[] ss2 = ss.split("=");
            Assert.isTrue(ss2.length == 2, "Missing value in data string");

            String cn = ss2[0].trim().toLowerCase();
            Assert.isTrue(cols.contains(cn), "Unknown column " + cn);

            String cv = ss2[1];
            cv = cv.trim();
            if (cv.charAt(0) == '\"') {
                cv = cv.substring(1);
            }
            if (cv.charAt(cv.length() - 1) == '\"') {
                cv = cv.substring(0,cv.length() - 1);
            }
            map.put(cn, cv);
        }

        return map;
    }
}
