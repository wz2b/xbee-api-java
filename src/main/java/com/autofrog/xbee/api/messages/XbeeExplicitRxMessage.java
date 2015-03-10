package com.autofrog.xbee.api.messages;

import com.autofrog.xbee.api.protocol.XbeeMessageType;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by chrisp on 009 3/9/2015.
 */
public class XbeeExplicitRxMessage extends XbeeMessageBase {

    private long sourceAddress;
    private short sourceNetworkAddress;
    private byte sourceEndpoint;
    private byte destEndpoint;
    private short profileId;
    private short clusterId;
    private byte rxOpts;


    private XbeeExplicitRxMessage(byte[] data) {
        super.rawFrameType = XbeeMessageType.EXPLICIT_RX.valueOf();
        super.rawData = data;
    }

    /**
     * Helper method to parse messages.  This builds a new byte buffer every time, so
     * it's more efficient to use the alternative methodt hat takes a byte buffer in
     * as the parameter.
     *
     * @param bytes
     * @return an explicit message, or null if there isn't one
     * @throws Exception if something goes wrong (e.g. IO Exception)
     */
    public static XbeeExplicitRxMessage create(byte[] bytes) throws IOException {
        return create(ByteBuffer.wrap(bytes));
    }

    /**
     * Create this message type from a byte buffer.
     *
     * @param bb
     * @return an XbeeExplicitRxMessage, or null if this byte buffer doesn't match the criteria
     */
    public static XbeeExplicitRxMessage create(ByteBuffer bb) {

        /* Preserve the current byte ordering on the buffer */
        ByteOrder oldOrder = bb.order();

        bb.order(ByteOrder.BIG_ENDIAN);
        XbeeExplicitRxMessage o = new XbeeExplicitRxMessage(bb.array());

        o.sourceAddress = bb.getLong();
        o.sourceNetworkAddress = bb.getShort();

        o.sourceEndpoint = bb.get();
        o.destEndpoint = bb.get();

        o.clusterId = bb.getShort();
        o.profileId = bb.getShort();
        o.rxOpts = bb.get();

        o.rawData = new byte[bb.remaining()];
        bb.get(o.rawData);
        return o;


    }

    public short getClusterId() {
        return clusterId;
    }

    public byte getDestEndpoint() {
        return destEndpoint;
    }

    public short getProfileId() {
        return profileId;
    }

    public byte getRxOpts() {
        return rxOpts;
    }

    public long getSourceAddress() {
        return sourceAddress;
    }

    public byte getSourceEndpoint() {
        return sourceEndpoint;
    }

    public short getSourceNetworkAddress() {
        return sourceNetworkAddress;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("XbeeExplicitRxMessage { ").append(String.format("sourceAddress=0x%016X", sourceAddress)).append(String.format(", sourceNeworkAddress=0x%04X", sourceNetworkAddress)).append(String.format(", sourceEndpoint=0x%02X", sourceEndpoint)).append(String.format(", destEndpoint=0x%02X", destEndpoint)).append(String.format(", profileId=0x%04X", profileId)).append(String.format(", clusterId=0x%04X", clusterId)).append(String.format(", rxOpts=0x%02X", rxOpts)).append(" }");
        return sb.toString();

    }
}

