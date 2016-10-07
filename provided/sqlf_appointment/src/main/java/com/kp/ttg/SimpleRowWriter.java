package com.kp.ttg;

import java.lang.management.ManagementFactory;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.DynamicMBean;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanServer;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import javax.management.ObjectName;
import javax.management.ReflectionException;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.vmware.sqlfire.callbacks.AsyncEventListener;
import com.vmware.sqlfire.callbacks.Event;
import com.vmware.sqlfire.callbacks.Event.Type;

public class SimpleRowWriter extends WriterBase implements AsyncEventListener {

    private int consecutiveErrorCount = 0;
    private int totalErrorCount = 0;
    private int maxConsecutiveErrorCount = 0;
    private int maxTotalErrorCount = 0;
    private boolean disabled = false;
    private Set<Integer> ignoredErrorCodes = new HashSet<>();
    private boolean ignoreNextError = false;
    private boolean ignoreAllErrors = false;

    private SimpleRowWriterMonitor monitor = new SimpleRowWriterMonitor();
    private long notificationSequence = 0L;

    private DisabledNotificationThread notifThread = new DisabledNotificationThread();
    private long disabledThreadWaitTime = 60 * 1000L; // 1 minute

    @Override
    public void close() {
    }

    @Override
    public void init(String initStr) {
        doInit(initStr);

        maxConsecutiveErrorCount = Integer.parseInt(properties.getProperty("maxConsecutiveErrorCount",
                System.getProperty("org.kp.simple-row-writer.max-consecutive-error-count", "10")));
        maxTotalErrorCount = Integer.parseInt(properties.getProperty("maxTotalErrorCount",
                System.getProperty("org.kp.simple-row-writer.max-total-error-count", "1000")));

        LOG.info("maxConsecutiveErrorCount={}, maxTotalErrorCount={}", maxConsecutiveErrorCount, maxTotalErrorCount);

        String ignErrCds = properties.getProperty("ignoredErrorCodes",
                System.getProperty("org.kp.simple-row-writer.ignored-error-codes"));

        if (StringUtils.hasText(ignErrCds)) {
            String[] ss = ignErrCds.split(",");
            for (String s : ss) {
                ignoredErrorCodes.add(Integer.parseInt(s));
            }
        }

        LOG.info("ignoredErrorCodes={}", ignoredErrorCodes);

        try {
            MBeanServer server = ManagementFactory.getPlatformMBeanServer();
            ObjectName on = new ObjectName("org.kp.writer:service=SimpleRowWriter");
            server.registerMBean(monitor, on);
        } catch (Exception x) {
            throw new IllegalArgumentException(x.toString(), x);
        }

        notifThread.setPriority(Thread.NORM_PRIORITY - 2);
        notifThread.start();
    }

    @Override
    public boolean processEvents(List<Event> events) {
        if (disabled) {
            LOG.debug("disabled due to errors");
            return false;
        } else {
            if (disableDelete && disableInsert && disableUpdate) {
                LOG.warn("disableDelete, disableInsert, and disableUpdate are all true - no processing will occur");
                return true;
            }

            try {
                for (Event event : events) {
                    process(event);
                }

                consecutiveErrorCount = 0;
            } catch (Exception x) {
                LOG.error(x.toString(), x);
                process(x);
                return false;
            }

            return true;
        }
    }

    private void process(Exception x) {
        if (++consecutiveErrorCount > maxConsecutiveErrorCount) {
            LOG.error("Max consecutive error count {} exceeded - SimpleRowWriter is disabled", maxConsecutiveErrorCount);
            disabled = true;

            Notification notif = new Notification("org.kp.writer.error", "SimpleRowWriter", ++notificationSequence,
                    System.currentTimeMillis(), "maxConsecutiveErrorCount has been exceeded");

            Map<String, Object> data = new HashMap<>();

            data.put("maxConsecutiveErrorCount", maxConsecutiveErrorCount);
            data.put("consecutiveErrorCount", consecutiveErrorCount);

            notif.setUserData(data);

            monitor.sendNotification(notif);

            notifThread.startNotif();
        }

        if (++totalErrorCount > maxTotalErrorCount) {
            LOG.error("Max total error count {} exceeded - SimpleRowWriter is disabled", maxTotalErrorCount);
            disabled = true;

            Notification notif = new Notification("org.kp.writer.error", "SimpleRowWriter", ++notificationSequence,
                    System.currentTimeMillis(), "maxTotalErrorCount has been exceeded");

            Map<String, Object> data = new HashMap<>();

            data.put("maxTotalErrorCount", maxTotalErrorCount);
            data.put("totalErrorCount", totalErrorCount);

            notif.setUserData(data);

            monitor.sendNotification(notif);

            notifThread.startNotif();
        }
    }

    private void process(Event event) throws Exception {
        try {
            LOG.debug("event={}", event);

            Type type = event.getType();

            switch (type) {
                case AFTER_DELETE: {
                    if (disableDelete) {
                        LOG.debug("delete is disabled");
                        return;
                    }
                    break;
                }
                case AFTER_INSERT: {
                    if (disableInsert) {
                        LOG.debug("insert is disabled");
                        return;
                    }
                    break;
                }
                case AFTER_UPDATE: {
                    if (disableUpdate) {
                        LOG.debug("update is disabled");
                        return;
                    }
                    break;
                }
                default: {
                    LOG.warn("not handling event type {}", type);
                    return;
                }
            }

            if (event.isPossibleDuplicate()) {
                LOG.warn("possible duplicate: event {}", event);
            }

            String schemaName = event.getSchemaName();
            String tableName = parseTableName(event.getTableName());

            LOG.debug("schemaName={}, tableName={}, type={}", schemaName, tableName, type);

            switch (type) {
                case AFTER_DELETE: {
                    Map<String, Object> keys = getKeys(event);
                    processDelete(schemaName, tableName, keys);
                    break;
                }
                case AFTER_INSERT: {
                    Map<String, Object> cols = getCols(event);
                    processInsert(schemaName, tableName, cols);
                    break;
                }
                case AFTER_UPDATE: {
                    Map<String, Object> keys = getKeys(event);
                    Map<String, Object> cols = getCols(event);
                    processUpdate(schemaName, tableName, keys, cols);
                    break;
                }
                default: {
                    // no-op, just here to silence compiler warning
                }
            }
        } catch (SQLException x) {
            int code = x.getErrorCode();
            LOG.error("SQLException: errorCode={}, state={}, message={}", code, x.getSQLState(), x.toString());

            if (ignoreNextError) {
                ignoreNextError = false;
                LOG.debug("ignoring this error; errorCode={}, state={}, message={}", code, x.getSQLState(), x.toString());
            } else if (ignoredErrorCodes.contains(code)) {
                LOG.debug("ignoring error based on code: errorCode={}, state={}, message={}", code, x.getSQLState(), x.toString());
            } else if (ignoreAllErrors) {
                LOG.warn("ignoring all errors: errorCode={}, state={}, message={}", code, x.getSQLState(), x.toString());
            } else {
                throw x;
            }
        }
    }

    private Map<String, Object> getKeys(Event event) throws Exception {
        return getCols(event.getPrimaryKeysAsResultSet());
    }

    private Map<String, Object> getCols(Event event) throws Exception {
        return getCols(event.getNewRowsAsResultSet());
    }

    private Map<String, Object> getCols(ResultSet res) throws Exception {
        Map<String, Object> m = new TreeMap<>();

        ResultSetMetaData resMeta = res.getMetaData();

        for (int i = 1; i <= resMeta.getColumnCount(); ++i) {
            String colName = resMeta.getColumnName(i);
            Object val = res.getObject(i);
            m.put(colName, val);
        }

        return m;
    }

    @Override
    public void start() {
    }

    private synchronized void reset() {
        consecutiveErrorCount = 0;
        totalErrorCount = 0;
        disabled = false;
        notifThread.endNotify();
    }

    private class DisabledNotificationThread extends Thread {

        private Object enableSync = new Object();
        private Object waitSync = new Object();

        public void startNotif() {
            synchronized (enableSync) {
                enableSync.notify();
            }
        }

        public void endNotify() {
            synchronized (waitSync) {
                waitSync.notify();
            }
        }

        @Override
        public void run() {
            try {
                while (true) {
                    synchronized (enableSync) {
                        enableSync.wait();
                    }

                    while (disabled) {
                        synchronized (waitSync) {
                            waitSync.wait(disabledThreadWaitTime);
                        }

                        if (disabled) {
                            LOG.error("SimpleRowWriter is disabled due to errors");
                            Notification notif = new Notification("org.kp.writer.disabled", "SimpleRowWriter",
                                    ++notificationSequence, System.currentTimeMillis(), "writer is disabled due to errors");

                            Map<String, Object> data = new HashMap<>();
                            notif.setUserData(data);

                            monitor.sendNotification(notif);
                        }
                    }
                }
            } catch (Exception x) {
                LOG.error(x.toString(), x);
            }
        }
    }

    public class SimpleRowWriterMonitor extends NotificationBroadcasterSupport implements DynamicMBean {

        private MBeanInfo info;

        public SimpleRowWriterMonitor() {
            info = new MBeanInfo("SimpleRowWriterMonitor", "Monitor for SimpleRowWriter", getAttrs(), null, getOpers(), null);
        }

        @Override
        public Object getAttribute(String attribute) throws AttributeNotFoundException, MBeanException, ReflectionException {
            if ("disableInsert".equals(attribute)) {
                return disableInsert;
            } else if ("disableUpdate".equals(attribute)) {
                return disableUpdate;
            } else if ("disableDelete".equals(attribute)) {
                return disableDelete;
            } else if ("consecutiveErrorCount".equals(attribute)) {
                return consecutiveErrorCount;
            } else if ("totalErrorCount".equals(attribute)) {
                return totalErrorCount;
            } else if ("maxConsecutiveErrorCount".equals(attribute)) {
                return maxConsecutiveErrorCount;
            } else if ("maxTotalErrorCount".equals(attribute)) {
                return maxTotalErrorCount;
            } else if ("ignoreNextError".equals(attribute)) {
                return ignoreNextError;
            } else if ("ignoreAllErrors".equals(attribute)) {
                return ignoreAllErrors;
            } else if ("disabled".equals(attribute)) {
                return disabled;
            } else {
                throw new AttributeNotFoundException("Unknown attribute " + attribute);
            }
        }

        @Override
        public void setAttribute(Attribute attribute) throws AttributeNotFoundException, InvalidAttributeValueException,
                MBeanException, ReflectionException {
            String name = attribute.getName();
            if ("disableInsert".equals(name)) {
                disableInsert = (boolean) attribute.getValue();
            } else if ("disableUpdate".equals(name)) {
                disableUpdate = (boolean) attribute.getValue();
            } else if ("disableDelete".equals(name)) {
                disableDelete = (boolean) attribute.getValue();
            } else if ("ignoreAllErrors".equals(name)) {
                ignoreAllErrors = (boolean) attribute.getValue();
            } else if ("maxConsecutiveErrorCount".equals(name)) {
                int c = (int) attribute.getValue();
                Assert.isTrue(c >= 0, "maxConsecutiveErrorCount must be >=0");
                maxConsecutiveErrorCount = c;
            } else if ("maxTotalErrorCount".equals(name)) {
                int c = (int) attribute.getValue();
                Assert.isTrue(c >= 0, "maxTotalErrorCount must be >=0");
                maxTotalErrorCount = c;
            } else {
                throw new AttributeNotFoundException("Unknown attribute " + name);
            }
        }

        @Override
        public AttributeList getAttributes(String[] attributes) {
            AttributeList list = new AttributeList();

            for (String attr : attributes) {
                try {
                    Object val = getAttribute(attr);
                    list.add(new Attribute(attr, val));
                } catch (Exception x) {
                    LOG.error(x.toString(), x);
                }
            }

            return list;
        }

        @Override
        public AttributeList setAttributes(AttributeList attributes) {
            try {
                for (Object attr : attributes) {
                    setAttribute((Attribute) attr);
                }
            } catch (Exception x) {
                LOG.error(x.toString(), x);
            }
            return attributes;
        }

        @Override
        public Object invoke(String actionName, Object[] params, String[] signature) throws MBeanException, ReflectionException {
            if ("reset".equals(actionName) && signature.length == 0) {
                reset();
            } else if ("ignoreNextError".equals(actionName) && signature.length == 0) {
                ignoreNextError = true;
            } else {
                LOG.error("Unknown action {}", actionName);
            }

            return null;
        }

        @Override
        public MBeanInfo getMBeanInfo() {
            return info;
        }

        private MBeanAttributeInfo[] getAttrs() {
            List<MBeanAttributeInfo> l = new ArrayList<>();

            l.add(new MBeanAttributeInfo("disableInsert", "boolean", "Disable inserts", true, true, false));
            l.add(new MBeanAttributeInfo("disableUpdate", "boolean", "Disable updates", true, true, false));
            l.add(new MBeanAttributeInfo("disableDelete", "boolean", "Disable deletes", true, true, false));
            l.add(new MBeanAttributeInfo("consecutiveErrorCount", "int", "The number of consecutive errors", true, false, false));
            l.add(new MBeanAttributeInfo("totalErrorCount", "int", "The total number of errors", true, false, false));
            l.add(new MBeanAttributeInfo("maxConsecutiveErrorCount", "int", "The maximum number of consecutive errors", true, true,
                    false));
            l.add(new MBeanAttributeInfo("maxTotalErrorCount", "int", "The maximum number of total errors", true, true, false));
            l.add(new MBeanAttributeInfo("ignoreNextError", "boolean", "If true the next error will be ignored", true, false, false));
            l.add(new MBeanAttributeInfo("ignoreAllErrors", "boolean", "If true the next error will be ignored", true, true, false));
            l.add(new MBeanAttributeInfo("disabled", "boolean", "Disabled due to errors", true, false, false));

            return l.toArray(new MBeanAttributeInfo[l.size()]);
        }

        private MBeanOperationInfo[] getOpers() {
            List<MBeanOperationInfo> l = new ArrayList<>();

            l.add(new MBeanOperationInfo("reset", "Reset statistics and enable operation", null, "void", MBeanOperationInfo.ACTION));
            l.add(new MBeanOperationInfo("ignoreNextError", "Ignore the next error", null, "void", MBeanOperationInfo.ACTION));

            return l.toArray(new MBeanOperationInfo[l.size()]);
        }

    }
}
