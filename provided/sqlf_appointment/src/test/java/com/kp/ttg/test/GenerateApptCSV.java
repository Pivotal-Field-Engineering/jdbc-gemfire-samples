package com.kp.ttg.test;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Types;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.kp.ttg.CaseSensitivity;
import com.kp.ttg.DSBase;
import com.kp.ttg.MetadataHelper;
import com.kp.ttg.MetadataHelper.MetadataHolder;

public class GenerateApptCSV extends DSBase {

    private static final Logger LOG = LoggerFactory.getLogger(GenerateApptCSV.class);

    public static void main(String[] args) throws Exception {

        Assert.isTrue(args.length == 2, "Wrong number of args");

        String filePath = args[0];
        int count = Integer.parseInt(args[1]);

        LOG.info("filePath={}, count={}", filePath, count);

        MetadataHolder meta = MetadataHelper.getMetadata(EMBEDDED_DATA_SOURCE_NAME, null, "dbo", "appointment",
                CaseSensitivity.upper);

        Set<String> cols = meta.columnNames;
        Map<String, Integer> colTypes = meta.columnTypes;
        Map<String, Integer> colSizes = meta.columnSizes;
        Random random = new Random();

        FileWriter fw = new FileWriter(filePath);
        PrintWriter writer = new PrintWriter(fw);

        try {
            StringBuilder buf = new StringBuilder();
            addColumnsList(cols, buf);
            String line = buf.toString();
            writer.println(line);

            for (int i = 0; i < count; ++i) {
                buf = new StringBuilder();
                Iterator<String> iter = cols.iterator();

                while (iter.hasNext()) {
                    String col = iter.next();
                    Integer type = colTypes.get(col);
                    String value = null;

                    switch (type) {
                        case Types.BIGINT: {
                            if ("appointment_id".equals(col)) {
                                value = Integer.toString(i + 1);
                            } else {
                                Integer l = random.nextInt(100);
                                value = l.toString();
                            }
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

                line = buf.toString();
                writer.println(line);

                if ((i + 1) % 10 == 0) {
                    LOG.info("written {} records", i + 1);
                }
            }

        } finally {
            fw.close();
        }
    }
}
