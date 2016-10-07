package com.kp.ttg;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vmware.sqlfire.callbacks.AsyncEventListener;
import com.vmware.sqlfire.callbacks.Event;

public class SimpleRowWriterWrapper implements AsyncEventListener {

    private static SimpleRowWriter delegate = null;

    private static final Logger LOG = LoggerFactory.getLogger(SimpleRowWriterWrapper.class);

    @Override
    public void init(String initStr) {
        LOG.info("initStr={}", initStr);
        
        synchronized (SimpleRowWriterWrapper.class) {
            if (delegate == null) {
                LOG.info("delegate does not exist - creating: initStr={}", initStr);
                delegate = new SimpleRowWriter();
                delegate.init(initStr);
            } else {
                LOG.info("delegate exists: initStr={}", initStr);
            }
        }
    }

    @Override
    public boolean processEvents(List<Event> events) {
        return delegate.processEvents(events);
    }

    @Override
    public void start() {
    }

    @Override
    public void close() {
    }

}
