package com.kp.ttg.test;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.sql.DataSource;
import javax.xml.bind.DatatypeConverter;

import com.kp.ttg.CaseSensitivity;
import com.kp.ttg.DSBase;
import com.kp.ttg.DataSourceHelper;
import com.kp.ttg.MetadataHelper;
import com.kp.ttg.MetadataHelper.MetadataHolder;

public class GenerateUpdateApptCall extends DSBase {

    public static void main(String[] args) throws Exception {

        MetadataHolder meta = MetadataHelper.getMetadata(EMBEDDED_DATA_SOURCE_NAME, null, "dbo", "appointment",
                CaseSensitivity.upper);

        Set<String> cols = meta.columnNames;
        Map<String, Integer> colTypes = meta.columnTypes;
        Map<String, Integer> colSizes = meta.columnSizes;
        Random random = new Random();

        StringBuilder buf = new StringBuilder();

        Iterator<String> iter = cols.iterator();

        while (iter.hasNext()) {
            String col = iter.next();
            buf.append(col);
            buf.append("=");
            Integer type = colTypes.get(col);
            String value = null;

            switch (type) {
                case Types.BIGINT: {
                    Integer l = 20;
                    value = l.toString();
                    break;
                }
                case Types.VARCHAR:
                case Types.CHAR: {
                    Integer size = colSizes.get(col);
                    StringBuilder sb = new StringBuilder("\"");
                    for (int t = 0; t < size; ++t) {
                        char c = (char) ('A' + random.nextInt('Z' - 'A'));
                        sb.append(c);
                    }
                    sb.append('\"');
                    value = sb.toString();
                    break;
                }
                case Types.DATE:
                    value = DatatypeConverter.printDate(Calendar.getInstance());
                    break;
                case Types.TIME:
                    value = DatatypeConverter.printTime(Calendar.getInstance());
                    break;
                case Types.TIMESTAMP: {
                    value = DatatypeConverter.printDateTime(Calendar.getInstance());
                    break;
                }
            }

            if (value != null) {
                buf.append(value);
            }

            if (iter.hasNext()) {
                buf.append(',');
            }
        }

        String data = buf.toString();

        DataSource src = DataSourceHelper.getSource(EMBEDDED_DATA_SOURCE_NAME);
        Connection conn = src.getConnection();

        try {
            CallableStatement stmt = conn.prepareCall("call dbo.updateappt('new',?,?,?)");

            try {
                stmt.setString(1, data);
                stmt.registerOutParameter(2, Types.INTEGER);
                stmt.registerOutParameter(3, Types.VARCHAR);

                stmt.execute();

                int code = stmt.getInt(2);
                String msg = stmt.getString(3);

                System.out.println("code=" + code + ", msg=" + msg);
            } finally {
                stmt.close();
            }
        } finally {
            conn.close();
        }
    }
}
