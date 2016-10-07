package com.kp.ttg.test;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;

public class PGSQLMetadataTest {

    public static void main(String[] args) throws Exception {
        Class.forName("org.postgresql.Driver");
        Connection c = DriverManager.getConnection("jdbc:postgresql://server1.dev:5432/tpmg", "tpmg", "tpmg");
        DatabaseMetaData meta = c.getMetaData();
        ResultSet res = meta.getTables(null, "TPMG", null, null);
        while (res.next()) {
            String tn = res.getString("TABLE_NAME");
            System.out.println(tn);
        }
    }
}
