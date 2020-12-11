package com.happs.ximand.ringcontrol.viewmodel.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TimeHelperTest {

    @Test
    public void simplifyTimeTest() {
        simplifyTimeTest("00:00:00", "0:00");
        simplifyTimeTest("11:00:01", "11:00:01");
        simplifyTimeTest("11:00:00", "11:00");
    }

    private void simplifyTimeTest(String time, String expected) {
        String actual = TimeHelper.getPreviewTime(time);
        assertEquals(expected, actual);
    }
}
