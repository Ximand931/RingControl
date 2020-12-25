package com.happs.ximand.ringcontrol.model.object.command;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

class ChangeRingDurationCommandTest {

    @Test
    void getCommandTest() {
        ChangeRingDurationCommand changeRingDurationCommand = new ChangeRingDurationCommand(5000);
        byte[] expectedCommand = new byte[]{20, (byte) 0x88, 0x13};
        byte[] actualCommand = changeRingDurationCommand.getCommand();
        Assert.assertArrayEquals(expectedCommand, actualCommand);
    }

}