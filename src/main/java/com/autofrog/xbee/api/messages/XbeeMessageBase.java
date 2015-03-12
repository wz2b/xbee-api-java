package com.autofrog.xbee.api.messages;

/**
 * Created by chrisp on 009 3/9/2015.
 */
public abstract class XbeeMessageBase {
    protected byte[] rawData;
    protected byte rawFrameType;

    protected XbeeMessageBase(byte rawFrameType, byte[] rawData) {
        this.rawData = rawData;
        this.rawFrameType = rawFrameType;
    }

    public byte getRawFrameType() {
        return rawFrameType;
    }

    public byte[] getRawData() {
        return rawData;
    }
}
