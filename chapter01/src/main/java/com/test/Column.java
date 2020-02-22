package com.test;

import com.google.common.base.MoreObjects;

import java.util.Map;

public class Column<T> {
    private final String columnName;
    private final ValueConverter<T> valueConverter;
    private Object dbOldValue;
    private Object dbNewValue;

    private T oldValue;
    private T newValue;

    public Column(String columnName, ValueConverter<T> valueConverter) {
        this.columnName = columnName;
        this.valueConverter = valueConverter;
    }

    public T getOldValue() {
        return oldValue;
    }

    public void setOldValue(Object oldValue) {
        this.dbOldValue = oldValue;
        this.oldValue = valueConverter.readValue(oldValue);
    }

    public T getNewValue() {
        return newValue;
    }

    public void setNewValue(Object newValue) {
        this.dbNewValue = newValue;
        this.newValue = valueConverter.readValue(newValue);
    }

    public String stringOldValue(Map<String, String> options) {
        return this.valueConverter.toString(oldValue, options);
    }

    public String stringNewValue(Map<String, String> options) {
        return this.valueConverter.toString(newValue, options);
    }

    public String getColumnName() {
        return columnName;
    }

    public Object getDbOldValue() {
        return dbOldValue;
    }

    public Object getDbNewValue() {
        return dbNewValue;
    }

    public ValueConverter<T> getValueConverter() {
        return valueConverter;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("columnName", columnName)
                .add("valueConverter", valueConverter.getClass().getSimpleName())
                .add("oldValue", oldValue)
                .add("newValue", newValue)
                .toString();
    }
}
