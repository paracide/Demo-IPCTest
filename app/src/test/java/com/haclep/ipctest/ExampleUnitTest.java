package com.haclep.ipctest;

import android.test.InstrumentationTestCase;

;

public class ExampleUnitTest extends InstrumentationTestCase {

    public void test() throws Exception {
        final int expected = 1;
        final int reality = 1;
        assertEquals(expected, reality);
    }
}