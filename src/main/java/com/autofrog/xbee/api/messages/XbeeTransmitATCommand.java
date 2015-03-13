package com.autofrog.xbee.api.messages;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.nio.ByteBuffer;

/**
 * Created by chrisp on 013 3/13/2015.
 */
public class XbeeTransmitATCommand extends XbeeTransmitMessageBase {

    public static final byte frameType = 0x08;

    private final String command;
    private final byte sequence;

    public XbeeTransmitATCommand(String command) {
        this(command, (byte) 0);
    }

    public XbeeTransmitATCommand(String command, byte sequence) {
        this.command = command;
        this.sequence = sequence;
    }

    @Override
    public byte[] getBytes() {
        ByteBuffer buf = ByteBuffer.allocate(2 + command.length());
        buf.put(frameType);
        buf.put(sequence);
        buf.put(command.getBytes());
        return buf.array();
    }
}
