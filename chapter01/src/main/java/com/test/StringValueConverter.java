package com.test;

import java.util.Map;

public class StringValueConverter implements ValueConverter<String> {

    @Override
    public String readValue(Object dbValue) {
        if (dbValue == null) {
            return null;
        }
        return String.valueOf(dbValue);
    }

    @Override
    public String toString(Object value, Map<String, String> options) {
        if (value == null) {
            return null;
        }
        String valueStr = String.valueOf(value);
        if (options != null && !options.isEmpty()) {
            return options.get(valueStr) == null ? valueStr : options.get(valueStr);
        }
        return valueStr;
    }
}
