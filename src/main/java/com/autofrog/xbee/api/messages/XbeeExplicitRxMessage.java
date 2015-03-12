package com.autofrog.xbee.api.messages;

import com.autofrog.xbee.api.protocol.XbeeMessageType;

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

    public XbeeExplicitRxMessage(long sourceAddress,
                                 short sourceNetworkAddress,
                                 byte sourceEndpoint,
                                 byte destEndpoint,
                                 short profileId,
                                 short clusterId,
                                 byte rxOpts,
                                 byte[] data) {
        super(XbeeMessageType.EXPLICIT_RX.getFrameType(), data);
        this.sourceAddress = sourceAddress;
        this.sourceNetworkAddress = sourceNetworkAddress;
        this.sourceEndpoint = sourceEndpoint;
        this.destEndpoint = destEndpoint;
        this.profileId = profileId;
        this.clusterId = clusterId;
        this.rxOpts = rxOpts;
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

