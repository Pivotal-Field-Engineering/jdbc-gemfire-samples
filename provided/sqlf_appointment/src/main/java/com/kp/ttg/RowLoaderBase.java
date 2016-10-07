package com.kp.ttg;

import java.sql.SQLException;

import com.vmware.sqlfire.callbacks.RowLoader;

public abstract class RowLoaderBase extends DSBase implements RowLoader {

    @Override
    public void init(String initStr) throws SQLException {
        doInit(initStr);
    }
}
