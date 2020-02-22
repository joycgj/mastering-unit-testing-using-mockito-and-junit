package com.test;

import java.util.Map;

public interface ValueConverter<T> {

    T readValue(Object dbValue);

    String toString(Object value, Map<String, String> options);
}
