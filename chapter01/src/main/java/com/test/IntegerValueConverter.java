package com.test;

import java.util.Map;

public class IntegerValueConverter implements ValueConverter<Integer> {
    @Override
    public Integer readValue(Object dbValue) {
        if (dbValue == null) {
            return null;
        }
        return Integer.parseInt(String.valueOf(dbValue));
    }

    @Override
    public String toString(Object value, Map<String, String> options) {
        if (value == null) {
            return null;
        }
        if (options != null && !options.isEmpty()) {
            return options.get(value.toString()) == null ? value.toString() : options.get(value.toString());
        }
        return value.toString();
    }
}
