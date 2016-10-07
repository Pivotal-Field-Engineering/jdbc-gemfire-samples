package com.kp.ttg;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ColumnMappings {

    private Map<String, String> forwardMappings = new HashMap<>();
    private Map<String, String> reverseMappings = new HashMap<>();

    public ColumnMappings() {
    }

    public ColumnMappings(Properties props) {
        for (Map.Entry<Object, Object> entry : props.entrySet()) {
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            forwardMappings.put(key, value);
            reverseMappings.put(value, key);
        }
    }

    public String getForwardMapping(String col) {
        String c = forwardMappings.get(col);
        return c == null ? col : c;
    }

    public String getReverseMapping(String col) {
        String c = reverseMappings.get(col);
        return c == null ? col : c;
    }

    @Override
    public String toString() {
        return "ColumnMappings [forwardMappings=" + forwardMappings + ", reverseMappings=" + reverseMappings + "]";
    }
}