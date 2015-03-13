package com.autofrog.xbee.api.messages;

import com.autofrog.xbee.api.XbeeUtilities;
import com.autofrog.xbee.api.protocol.XbeeMessageType;

/**
 * Created by chrisp on 009 3/9/2015.
 */
public class XbeeExplicitRxMessage extends XbeeMessageBase {

    private final long sourceAddress;
    private final short sourceNetworkAddress;
    private final byte sourceEndpoint;
    private final byte destEndpoint;
    private final short profileId;
    private final short clusterId;
    private final boolean isAck;
    private final boolean isBroadcast;
    private final boolean isEncrypted;
    private final boolean isEndDevice;
    private final byte[] payload;

    public XbeeExplicitRxMessage(long sourceAddress,
                                 short sourceNetworkAddress,
                                 byte sourceEndpoint,
                                 byte destEndpoint,
                                 short profileId,
                                 short clusterId,
                                 boolean isAck,
                                 boolean isBroadcast,
                                 boolean isEncrypted,
                                 boolean isEndDevice,
                                 byte[] payload) {
        super(XbeeMessageType.EXPLICIT_RX.frameType);
        this.sourceAddress = sourceAddress;
        this.sourceNetworkAddress = sourceNetworkAddress;
        this.sourceEndpoint = sourceEndpoint;
        this.destEndpoint = destEndpoint;
        this.profileId = profileId;
        this.clusterId = clusterId;
        this.isAck = isAck;
        this.isBroadcast = isBroadcast;
        this.isEncrypted = isEncrypted;
        this.isEndDevice = isEndDevice;
        this.payload = payload;
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

    public boolean isAck() {
        return isAck;
    }

    public boolean isBroadcast() {
        return isBroadcast;
    }

    public boolean isEncrypted() {
        return isEncrypted;
    }

    public boolean isEndDevice() {
        return isEndDevice;
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

    public byte[] getPayload() {
        return payload;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("XbeeExplicitRxMessage { "
        ).append(String.format("sourceAddress=0x%016X", sourceAddress))
                .append(String.format(", sourceNeworkAddress=0x%04X", sourceNetworkAddress))
                .append(String.format(", sourceEndpoint=0x%02X", sourceEndpoint))
                .append(String.format(", destEndpoint=0x%02X", destEndpoint))
                .append(String.format(", profileId=0x%04X", profileId))
                .append(String.format(", clusterId=0x%04X", clusterId))
                .append(", isAck=")
                .append(isAck)
                .append(", isBroadcast=")
                .append(isBroadcast)
                .append(", isEncrypted=")
                .append(isEncrypted)
                .append(", isEndDevice=")
                .append(isEndDevice)
                .append(", payload=")
                .append(payload == null ? "null" : "0x" + XbeeUtilities.toHex(payload))
                .append(" }");
        return sb.toString();

    }
}

