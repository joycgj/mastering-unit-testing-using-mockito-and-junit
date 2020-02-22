package com.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Test {
    public static void main(String[] args) {
//        List<Person> list = new ArrayList<>();
//        list.add(new Person(1, "aaaa"));
//        list.add(new Person(2, "bbbb"));
//        list.add(new Person(3, "cccc"));
//
//        Map<Integer, Person> m = list.stream().collect(Collectors.toMap(Person::getId, Function.identity()));
//        System.out.println(m);
//        System.out.println(m.get(1).getName());
//
//        Map<Integer, String> m2 = list.stream().collect(Collectors.toMap(Person::getId, Person::getName));
//        System.out.println(m2);
//
//        Map<Integer, String> m3 = list.stream().collect(Collectors.toMap(Person::getId, Person::getName));
//        System.out.println(m3);


        new Test().f();

    }


    private void f() {
        List<Column<?>> columns = new ArrayList<>();
        columns.add(new Column("id", new IntegerValueConverter()));
        columns.add(new Column("name", new StringValueConverter()));
        Row row = new Row("idColumnValue1", "u", "payment", "user", "gtid1", 1000L, columns);

        Map<String, Column<?>> m = row.getColumns().stream().collect(Collectors.toMap(new Function<Column<?>, String>() {
            @Override
            public String apply(Column<?> column) {
                return column.getColumnName();
            }
        }, new Function<Column<?>, Column<?>>() {
            @Override
            public Column<?> apply(Column<?> column) {
                return column;
            }
        }, new BinaryOperator<Column<?>>() {
            @Override
            public Column<?> apply(Column<?> oldValue, Column<?> newValue) {
                return newValue;
            }
        }));
        System.out.println(m);
    }
}
