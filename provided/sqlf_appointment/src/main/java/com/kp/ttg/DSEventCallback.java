package com.kp.ttg;

import java.sql.SQLException;

import com.vmware.sqlfire.callbacks.EventCallback;

public abstract class DSEventCallback extends DSBase implements EventCallback {

    @Override
    public void close() throws SQLException {
    }

    @Override
    public void init(String initStr) throws SQLException {
        doInit(initStr);
    }

}
