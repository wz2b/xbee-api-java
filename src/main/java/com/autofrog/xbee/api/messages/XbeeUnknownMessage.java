package com.autofrog.xbee.api.messages;

/**
 * A message whose type we don't understand
 */
public class XbeeUnknownMessage extends XbeeMessageBase {
    public XbeeUnknownMessage(byte rawFrameType, byte[] rawData) {
        super(rawFrameType, rawData);
    }
}
