package com.autofrog.xbee.api.messages;

import com.autofrog.xbee.api.XbeeUtilities;

import java.util.Arrays;

/**
 * A message whose type we don't understand
 */
public class XbeeUnknownMessage extends XbeeMessageBase {
    private final byte[] data;

    public XbeeUnknownMessage(byte rawFrameType, byte[] data) {
        super(rawFrameType);
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }

    @Override
    public String toString() {
        return "XbeeUnknownMessage{" +
                "data=" + XbeeUtilities.toHex(data) +
                '}';
    }
}
