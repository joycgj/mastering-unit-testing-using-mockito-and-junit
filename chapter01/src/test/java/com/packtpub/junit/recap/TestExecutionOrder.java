package com.packtpub.junit.recap;

import org.junit.Test;

public class TestExecutionOrder {

    @Test
    public void edit() {
        System.out.println("edit executed");
    }

    @Test
    public void create() {
        System.out.println("create executed");
    }

    @Test
    public void remove() {
        System.out.println("remove executed");
    }
}
