package com.autofrog.xbee.api.messages;

import com.autofrog.xbee.api.util.XbeeUtilities;
import com.autofrog.xbee.api.protocol.XbeeMessageType;

/**
 * <pre>
 * (C) Copyright 2015 Christopher Piggott (cpiggott@gmail.com)
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * </pre>
 */
public class XbeeExplicitRxMessage extends XbeeAddressableMessage {


    private final byte sourceEndpoint;
    private final byte destEndpoint;
    private final short profileId;
    private final short clusterId;
    private final boolean isAck;
    private final boolean isBroadcast;
    private final boolean isEncrypted;
    private final boolean isEndDevice;
    private final byte[] payload;

    public XbeeExplicitRxMessage(byte[] deviceId,
                                 int address,
                                 byte sourceEndpoint,
                                 byte destEndpoint,
                                 short profileId,
                                 short clusterId,
                                 boolean isAck,
                                 boolean isBroadcast,
                                 boolean isEncrypted,
                                 boolean isEndDevice,
                                 byte[] payload) {
        super(XbeeMessageType.EXPLICIT_RX.frameType, deviceId, address);
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

    public XbeeExplicitRxMessage(XbeeExplicitRxMessage orig,
                                 byte[] deviceId) {
        super(XbeeMessageType.EXPLICIT_RX.frameType, deviceId, orig.address);
        this.sourceEndpoint = orig.sourceEndpoint;
        this.destEndpoint = orig.destEndpoint;
        this.profileId = orig.profileId;
        this.clusterId = orig.clusterId;
        this.isAck = orig.isAck;
        this.isBroadcast = orig.isBroadcast;
        this.isEncrypted = orig.isEncrypted;
        this.isEndDevice = orig.isEndDevice;
        this.payload = orig.payload;
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


    public byte getSourceEndpoint() {
        return sourceEndpoint;
    }


    @Override
    protected XbeeExplicitRxMessage doCloneWithNewDeviceId(byte [] newDeviceId) {
        return new XbeeExplicitRxMessage(
                newDeviceId,
                this.address,
                this.sourceEndpoint,
                this.destEndpoint,
                this.profileId,
                this.clusterId,
                this.isAck,
                this.isBroadcast,
                this.isEncrypted,
                this.isEndDevice,
                this.payload);
    }

    public byte[] getPayload() {
        return payload;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("XbeeExplicitRxMessage { "
        ).append(String.format("deviceId=0x%s", XbeeUtilities.toHex(deviceId)))
                .append(String.format(", sourceNeworkAddress=0x%04X", address))
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

