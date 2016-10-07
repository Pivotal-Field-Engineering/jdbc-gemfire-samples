package com.kp.ttg;

import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.gemstone.gemfire.management.internal.cli.util.spring.Assert;

public class Timer implements Constants {

    private StatementType type;
    private String schemaName;
    private String tableName;
    private long[] thresholds;

    private BlockingQueue<Long> queue = new LinkedBlockingQueue<Long>(1000000);
    private TimerMonitor monitor;

    private MBeanServer server;

    private Logger log;
    private static final Logger LOG = LoggerFactory.getLogger(Timer.class);

    private static Map<Key, Timer> timers = null;

    private static long[] defaultThresholds;
    private static final ThreadLocal<Long> start = new ThreadLocal<Long>();

    private static final class Key {

        public StatementType type;
        public String schemaName;
        public String tableName;

        public Key(StatementType type, String schemaName, String tableName) {
            this.type = type;
            this.schemaName = schemaName;
            this.tableName = tableName;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((schemaName == null) ? 0 : schemaName.hashCode());
            result = prime * result + ((tableName == null) ? 0 : tableName.hashCode());
            result = prime * result + ((type == null) ? 0 : type.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            Key other = (Key) obj;
            if (schemaName == null) {
                if (other.schemaName != null) {
                    return false;
                }
            } else if (!schemaName.equals(other.schemaName)) {
                return false;
            }
            if (tableName == null) {
                if (other.tableName != null) {
                    return false;
                }
            } else if (!tableName.equals(other.tableName)) {
                return false;
            }
            if (type != other.type) {
                return false;
            }
            return true;
        }

    }

    private static synchronized void init() {
        if (timers == null) {
            LOG.info("Initializing Timer");
            timers = new HashMap<>();

            try {
                InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("timers.properties");

                if (is == null) {
                    LOG.info("timer.properties not found - using defaults");
                    defaultThresholds = new long[] { TEN_MILLISECONDS, HUNDRED_MILLISECONDS };
                } else {
                    LOG.info("timer.properties found - loading");
                    Properties props = new Properties();
                    props.load(is);

                    LOG.debug("props={}", props);

                    String defThList = (String) props.remove("default-thresholds");
                    if (StringUtils.hasText(defThList)) {
                        String[] ss = defThList.split(",");
                        defaultThresholds = new long[ss.length];
                        for (int i = 0; i < ss.length; ++i) {
                            defaultThresholds[i] = Long.parseLong(ss[i]);
                        }
                    }

                    LOG.debug("defaultThresholds={}", Arrays.toString(defaultThresholds));

                    for (Map.Entry<Object, Object> entry : props.entrySet()) {
                        String key = (String) entry.getKey();
                        String value = (String) entry.getValue();

                        String[] keyList = key.split("\\.");
                        Assert.isTrue(keyList.length == 3,
                                "Bad format in timers.properties: <type>.<schema>.<table>=[thresholds]; actual " + key);

                        StatementType type = StatementType.valueOf(keyList[0]);
                        String schemaName = keyList[1];
                        String tableName = keyList[2];

                        String[] threshList = value.split(",");
                        long[] thresholds = new long[threshList.length];

                        for (int i = 0; i < threshList.length; ++i) {
                            thresholds[i] = Long.parseLong(threshList[i]);
                        }

                        Key k = new Key(type, schemaName, tableName);
                        Timer t = new Timer(type, schemaName, tableName, thresholds);
                        timers.put(k, t);

                        LOG.debug("timer={}", t);
                    }
                }
            } catch (IllegalArgumentException x) {
                throw x;
            } catch (Exception x) {
                throw new IllegalArgumentException(x.toString(), x);
            }
        } else {
            LOG.debug("Timer already init");
        }
    }

    public static synchronized Timer getTimer(StatementType type, String schemaName, String tableName) {
        init();

        LOG.debug("type={}, schemaName={}, tableName={}", type, schemaName, tableName);

        Key key = new Key(type, schemaName, tableName);

        Timer t = timers.get(key);

        if (t != null) {
            LOG.debug("timer already exists: timer={}", t);
            return t;
        }

        t = new Timer(type, schemaName, tableName, defaultThresholds);
        timers.put(key, t);

        LOG.info("created new timer: t={}", t);
        return t;
    }

    private Timer(StatementType type, String schemaName, String tableName, long[] thresh) {
        this.type = type;
        this.schemaName = schemaName;
        this.tableName = tableName;
        this.thresholds = thresh;
        Arrays.sort(thresholds);
        
        monitor = new TimerMonitor(this.thresholds.length);

        log = LoggerFactory.getLogger("Timer-" + type.name() + "-" + schemaName + "-" + tableName);

        initJmx();

        MonitorThread th = new MonitorThread("Timer-MonitorThread-" + type.name() + "-" + schemaName + "-" + tableName);
        th.setPriority(Thread.NORM_PRIORITY - 2);
        th.setDaemon(true);
        th.start();
    }

    private void initJmx() {
        try {
            server = ManagementFactory.getPlatformMBeanServer();
            ObjectName on = new ObjectName("org.kp.timer:service=Timer,statementType=" + type.name() + ",schemaName=" + schemaName
                    + ",tableName=" + tableName);
            server.registerMBean(monitor, on);
        } catch (Exception x) {
            throw new IllegalArgumentException(x.toString(), x);
        }
    }

    public void start() {
        start.set(System.nanoTime());
    }

    public long end() {
        Long strt = start.get();

        if (strt == null) {
            throw new IllegalStateException("Timer for statement type " + type.name() + " schema " + schemaName + " table "
                    + tableName + " not started");
        }

        long delta = (System.nanoTime() - strt) / 1000L; // convert to microseconds

        queue.offer(delta);

        return delta;
    }

    private synchronized void calc(long delta) {
        try {
            monitor.calc(delta);
        } catch (Exception x) {
            log.error(x.toString(), x);
        }
    }

    @Override
    public String toString() {
        return "Timer [type=" + type + ", schemaName=" + schemaName + ", tableName=" + tableName + ", thresholds="
                + Arrays.toString(thresholds) + "]";
    }

    private class MonitorThread extends Thread {

        public MonitorThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            try {
                while (true) {
                    long delta = queue.take();
                    calc(delta);
                }
            } catch (Exception x) {
                log.error(x.toString(), x);
            }
        }
    }

    public class TimerMonitor extends NotificationBroadcasterSupport implements DynamicMBean {

        private long startTime = System.nanoTime();
        private long minimum = -1L;
        private long maximum = -1L;
        private long count = 0L;
        private int total = 0;
        private long average = 0L;
        private long rate = 0L;

        private long rollingMinimum = -1L;
        private long rollingMaximum = -1L;
        private int rollingCount = 0;
        private long rollingAverage = 0L;
        private long[] rollingTimings = new long[100];
        private int rollingIndex = 0;

        private int[] thresholdCounts;
        private int notificationSequence = 0;

        private MBeanInfo info;

        public TimerMonitor(int threshLen) {
            this.thresholdCounts = new int[threshLen];
            Arrays.fill(thresholdCounts, 0);

            info = new MBeanInfo("TimerMonitor-" + type.name() + "-" + schemaName + "." + tableName, "Timer " + type.name()
                    + " for performance monitoring table " + schemaName + "." + tableName, getAttrs(), null, getOpers(), null);
        }

        public synchronized void calc(long delta) {
            long now = System.nanoTime();

            ++count;

            if (count > 1) {
                rate = 1000000000L * count / (now - startTime); // loads/second
            }

            if (minimum < 0L || delta < minimum) {
                minimum = delta;
            }

            if (maximum < 0L || delta > maximum) {
                maximum = delta;
            }

            total += delta;
            average = total / count;

            if (rollingIndex >= rollingTimings.length) {
                rollingIndex = 0;
            }

            if (rollingCount < rollingTimings.length) {
                ++rollingCount;
            }

            rollingTimings[rollingIndex++] = delta;

            rollingMinimum = -1L;
            rollingMaximum = -1L;
            rollingAverage = 0L;
            long rtot = 0L;

            for (int i = 0; i < rollingCount; ++i) {
                long rt = rollingTimings[i];
                rtot += rt;

                if (rollingMinimum < 0 || rt < rollingMinimum) {
                    rollingMinimum = rt;
                }

                if (rollingMaximum < 0 || rt > rollingMaximum) {
                    rollingMaximum = rt;
                }
            }

            rollingAverage = rtot / rollingCount;

            if (log.isDebugEnabled()) {
                log.debug(
                        "type={}, schemaName={}, tableName={}, delta={}, count={}, minimum={}, maximum={}, average={}, rollingCount={}, rollingMinimum={}, rollingMaximum={}, rollingAverage={}, rate={}",
                        type.name(), schemaName, tableName, delta, count, minimum, maximum, average, rollingCount, rollingMinimum,
                        rollingMaximum, rollingAverage, rate);
            }

            for (int i = 0; i < thresholds.length; ++i) {
                long thresh = thresholds[i];

                if (delta >= thresh) {
                    long thc = ++thresholdCounts[i];

                    log.warn("threshold {} has been exceeded, count {}", thresh, thc);

                    Notification notif = new Notification("org.kp.timer.threshold", "Timer-" + type.name() + "-" + schemaName + "."
                            + tableName, ++notificationSequence, System.currentTimeMillis(), "threshold " + thresh
                            + " has been exceeded, count " + thc);

                    Map<String, Object> data = new HashMap<>();

                    data.put("statementType", type.name());
                    data.put("schemaName", schemaName);
                    data.put("tableName", tableName);
                    data.put("threshold", thresh);
                    data.put("thresholdCount", thc);

                    notif.setUserData(data);

                    this.sendNotification(notif);
                }
            }
        }

        public synchronized void reset() {
            minimum = -1L;
            maximum = -1L;
            count = 0L;
            average = 0L;
            total = 0;
            rate = 0L;
            startTime = System.nanoTime();
            notificationSequence = 0;
            Arrays.fill(thresholdCounts, 0);
            rollingMinimum = -1L;
            rollingMaximum = -1L;
            rollingCount = 0;
            rollingAverage = 0L;
            rollingIndex = 0;
            Arrays.fill(rollingTimings, 0L);
        }

        private MBeanAttributeInfo[] getAttrs() {
            List<MBeanAttributeInfo> l = new ArrayList<>();

            l.add(new MBeanAttributeInfo("minimum", "long", "The minimum value", true, false, false));
            l.add(new MBeanAttributeInfo("maximum", "long", "The maximum value", true, false, false));
            l.add(new MBeanAttributeInfo("average", "long", "The average value", true, false, false));
            l.add(new MBeanAttributeInfo("count", "int", "The count", true, false, false));
            l.add(new MBeanAttributeInfo("rate", "long", "The rate", true, false, false));
            l.add(new MBeanAttributeInfo("rollingMinimum", "long", "The rolling minimum value", true, false, false));
            l.add(new MBeanAttributeInfo("rollingMaximum", "long", "The rolling maximum value", true, false, false));
            l.add(new MBeanAttributeInfo("rollingAverage", "long", "The rolling average value", true, false, false));
            l.add(new MBeanAttributeInfo("rollingCount", "int", "The rolling count", true, false, false));

            for (long thr : thresholds) {
                l.add(new MBeanAttributeInfo("thresholdCount_" + thr, "int", "The count for threshold " + thr, true, false, false));
            }

            return l.toArray(new MBeanAttributeInfo[l.size()]);
        }

        private MBeanOperationInfo[] getOpers() {
            List<MBeanOperationInfo> l = new ArrayList<>();

            l.add(new MBeanOperationInfo("reset", "Reset the statistics", null, "void", MBeanOperationInfo.ACTION));

            return l.toArray(new MBeanOperationInfo[l.size()]);
        }

        @Override
        public Object getAttribute(String attribute) throws AttributeNotFoundException, MBeanException, ReflectionException {
            if ("minimum".equals(attribute)) {
                return minimum;
            } else if ("maximum".equals(attribute)) {
                return maximum;
            } else if ("average".equals(attribute)) {
                return average;
            } else if ("count".equals(attribute)) {
                return count;
            } else if ("rate".equals(attribute)) {
                return rate;
            } else if ("rollingMinimum".equals(attribute)) {
                return rollingMinimum;
            } else if ("rollingMaximum".equals(attribute)) {
                return rollingMaximum;
            } else if ("rollingAverage".equals(attribute)) {
                return rollingAverage;
            } else if ("rollingCount".equals(attribute)) {
                return rollingCount;
            } else {
                for (int i = 0; i < thresholds.length; ++i) {
                    long thr = thresholds[i];
                    String s = "thresholdCount_" + thr;

                    if (s.equals(attribute)) {
                        return thresholdCounts[i];
                    }
                }

                throw new AttributeNotFoundException("Unknown attribute " + attribute);
            }
        }

        @Override
        public void setAttribute(Attribute attribute) throws AttributeNotFoundException, InvalidAttributeValueException,
                MBeanException, ReflectionException {
            throw new InvalidAttributeValueException("Attribute " + attribute.getName() + " is read-only");
        }

        @Override
        public AttributeList getAttributes(String[] attributes) {
            AttributeList list = new AttributeList();

            for (String attr : attributes) {
                try {
                    Object val = getAttribute(attr);
                    list.add(new Attribute(attr, val));
                } catch (Exception x) {
                    log.error(x.toString(), x);
                }
            }

            return list;
        }

        @Override
        public AttributeList setAttributes(AttributeList attributes) {
            return null;
        }

        @Override
        public Object invoke(String actionName, Object[] params, String[] signature) throws MBeanException, ReflectionException {
            if ("reset".equals(actionName) && signature.length == 0) {
                reset();
            } else {
                log.error("Unknown action " + actionName);
            }

            return null;
        }

        @Override
        public MBeanInfo getMBeanInfo() {
            return info;
        }
    }
}
