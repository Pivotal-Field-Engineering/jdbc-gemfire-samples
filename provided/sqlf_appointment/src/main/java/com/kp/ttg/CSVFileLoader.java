package com.kp.ttg;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.kp.ttg.MetadataHelper.MetadataHolder;

public class CSVFileLoader extends DSBase implements Constants {

    private static final Logger LOG = LoggerFactory.getLogger(CSVFileLoader.class);

  
    public static void load(String filePath, String schemaName, String tableName, String delimiter ) throws Exception {
        LOG.info("filePath={}, schemaName={}, tableName={}", filePath, schemaName, tableName);

        File file = new File(filePath);

        if (!file.exists()) {
            throw new FileNotFoundException(filePath);
        }

        if (!file.canRead()) {
            throw new IllegalArgumentException("File " + filePath + " is not readable");
        }

        if (!file.isFile()) {
            throw new IllegalArgumentException("File " + filePath + " is not a plain file");
        }

        if (file.length() == 0) {
            throw new IllegalArgumentException("File " + filePath + " is empty");
        }

        LOG.info("reading file: filePath={}, schemaName={}, tableName={}", filePath, schemaName, tableName);

        FileReader fr = new FileReader(file);

        try {
            //CSVParser parser = new CSVParser(fr, CSVFormat.RFC4180);
        	CSVParser parser = new CSVParser(fr, CSVFormat.newFormat(delimiter.charAt(0)));
            try {
                Iterator<CSVRecord> iter = parser.iterator();

                CSVRecord header = iter.next();
                List<String> colList = new ArrayList<>();

                for (String col : header) {
                    Assert.notNull(col, "Empty column name in header");
                    colList.add(col);
                }

                LOG.info("filePath={}, schemaName={}, tableName={}, colList={}", filePath, schemaName, tableName, colList);

                String sql = buildInsert(null, schemaName, tableName, colList);
                LOG.debug("filePath={}, schemaName={}, tableName={}, sql={}", filePath, schemaName, tableName, sql);

                int count = 0;
                int lineNo = 1;
                int totalfailed=0;

                DataSource source = DataSourceHelper.getSource(EMBEDDED_DATA_SOURCE_NAME);

                MetadataHolder meta = MetadataHelper.getMetadata(EMBEDDED_DATA_SOURCE_NAME, null, schemaName, tableName,
                        CaseSensitivity.none);
                Map<String, Integer> colTypes = meta.columnTypes;

                LOG.info("getting connection: filePath={}, schemaName={}, tableName={}", filePath, schemaName, tableName);

                Connection conn = source.getConnection();

                try {
                    LOG.info("preparing statement: filePath={}, schemaName={}, tableName={}", filePath, schemaName, tableName);

                    PreparedStatement stmt = conn.prepareStatement(sql);

                    try {
                        long start = System.currentTimeMillis();

                        while (iter.hasNext()) {
                            ++lineNo;
                            CSVRecord record = iter.next();

                            LOG.debug("processing record {} at {}: filePath={}, schemaName={}, tableName={}", record, lineNo,
                                    filePath, schemaName, tableName);
                            process(stmt, schemaName, tableName, record, colList, colTypes, lineNo);
                            try{
                            	stmt.executeUpdate();
                            }catch(SQLException sqlEx){
                            	LOG.warn("sqlEx.getSQLState() =="+sqlEx.getSQLState());
                            	if(sqlEx.getSQLState().startsWith("23")){
                            		LOG.warn("integrity constraint violation: Line no"+lineNo);
                            		totalfailed++;
                            	}
                            	else 
                            		throw sqlEx;
                            }
                            
                        }
                        long delta = System.currentTimeMillis() - start;
                        if (delta > 0) {
                            long rate = 1000L * count / delta; // records/second
                            String unit = "milliseconds";

                            if (delta > ONE_MINUTE) {
                                delta /= ONE_MINUTE;
                                unit = "minutes";
                            } else if (delta > ONE_SECOND) {
                                delta /= ONE_SECOND;
                                unit = "seconds";
                            }

                            LOG.info("inserted {} records in {} {} ({} records/second): filePath={}, schemaName={}, tableName={}",
                                    count, delta, unit, rate, filePath, schemaName, tableName);
                        } else {
                            LOG.info("inserted {} records: filePath={}, schemaName={}, tableName={}", count, filePath, schemaName,
                                    tableName);
                        }
                        LOG.warn("Toatl number of failed record :"+totalfailed);
                    } finally {
                        stmt.close();
                    }
                } finally {
                    conn.close();
                }
            } finally {
                parser.close();
            }
        } finally {
            fr.close();
        }
    }

    private static void process(PreparedStatement stmt, String schemaName, String tableName, CSVRecord record, List<String> cols,
            Map<String, Integer> colTypes, int lineNo) throws Exception {

        Assert.isTrue(record.size() == cols.size(), "Invalid number of values at line " + lineNo);

        for (int i = 0; i < record.size(); ++i) {
            String col = cols.get(i);
            String val = record.get(i);
            Integer colType = colTypes.get(col.toLowerCase());
            Object oval = null;

            Assert.notNull(colType, "Missing column type for column " + col);

            switch (colType) {
                case Types.VARCHAR:
                case Types.CHAR: {
                    oval = val;
                    break;
                }
                case Types.BIGINT: {
                    oval = new Long(val);
                    break;
                }
                case Types.INTEGER: {
                    oval = new Integer(val);
                    break;
                }
                case Types.BOOLEAN: {
                    oval = Boolean.valueOf(val);
                    break;
                }
                case Types.DATE: {
                    long t = DatatypeConverter.parseDate(val).getTimeInMillis();
                    oval = new Date(t);
                    break;
                }
                case Types.TIMESTAMP: {
                    long t = DatatypeConverter.parseDateTime(val).getTimeInMillis();
                    oval = new Timestamp(t);
                    break;
                }
                case Types.TIME: {
                    long t = DatatypeConverter.parseTime(val).getTimeInMillis();
                    oval = new Timestamp(t);
                    break;
                }
                case Types.DECIMAL: {
                    oval = new Double(val);
                    break;
                }
                case Types.SMALLINT: {
                    oval = new Short(val);
                    break;
                }
                default: {
                    throw new IllegalArgumentException("Cannot handle sql type " + colType + " for column " + col);
                }
            }

            stmt.setObject(i + 1, oval, colType);
        }

        
    }
}
