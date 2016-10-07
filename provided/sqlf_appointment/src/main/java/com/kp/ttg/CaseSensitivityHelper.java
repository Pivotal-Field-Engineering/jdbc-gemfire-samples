package com.kp.ttg;

import org.springframework.util.StringUtils;

public class CaseSensitivityHelper {

    private static final CaseSensitivity csDefault = CaseSensitivity.valueOf(System.getProperty("org.kp.database.case-sensitivity",
            "none"));

    public static final CaseSensitivity getCaseSensitivity(String value) {
        return StringUtils.hasText(value) ? CaseSensitivity.valueOf(value) : csDefault;
    }

    public static final String fixCase(String name, CaseSensitivity cs) {
        if (name == null) {
            return null;
        }
        
        switch (cs) {
            case lower:
                return name.toLowerCase();
            case upper:
                return name.toUpperCase();
            default:
                return name;
        }
    }
}
