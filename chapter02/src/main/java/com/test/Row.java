package com.test;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Objects;

public class Row {
    private final String idColumnValue;
    private final String op;
    private final String db;
    private final String table;
    private final long changeAt;
    private final String gtid;

    private final List<Column<?>> columns;

    public Row(final String idColumnValue, final String op, final String db, final String table, final String gtid, final long changeAt,
               final List<Column<?>> columns) {
        this.idColumnValue = idColumnValue;
        this.op = op;
        this.db = db;
        this.table = table;
        this.gtid = gtid;
        this.changeAt = changeAt;
        this.columns = columns;
    }

    public String getOp() {
        return op;
    }

    public String getDb() {
        return db;
    }

    public String getTable() {
        return table;
    }

    public long getChangeAt() {
        return changeAt;
    }

    public String getGtid() {
        return gtid;
    }

    public String getIdColumnValue() {
        return idColumnValue;
    }

    public List<Column<?>> getColumns() {
        return Lists.newArrayList(columns);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Row row = (Row) o;
        return changeAt == row.changeAt &&
                op.equals(row.op) &&
                idColumnValue.equals(row.idColumnValue) &&
                db.equals(row.db) &&
                table.equals(row.table) &&
                Objects.equals(gtid, row.gtid) &&
                columns.equals(row.columns);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idColumnValue, op, db, table, changeAt, gtid, columns);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("idColumnValue", idColumnValue)
                .add("op", op)
                .add("db", db)
                .add("table", table)
                .add("changeAt", changeAt)
                .add("gtid", gtid)
                .add("columns", columns)
                .toString();
    }
}
