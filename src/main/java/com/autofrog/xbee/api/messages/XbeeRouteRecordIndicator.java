package com.autofrog.xbee.api.messages;

import com.autofrog.xbee.api.protocol.XbeeMessageType;

import java.util.Arrays;

/**
 * Created by chrisp on 012 3/12/2015.
 */
public class XbeeRouteRecordIndicator extends XbeeMessageBase {

    private final long address;
    private final short shortAddress;
    private final boolean isAck;
    private final boolean isBroadcast;
    private final short[] route;


    public XbeeRouteRecordIndicator(long address,
                                    short shortAddress,
                                    boolean isAck,
                                    boolean isBroadcast,
                                    short[] route) {
        super(XbeeMessageType.ROUTE_RECORD_INDICATOR.frameType);
        this.address = address;
        this.shortAddress = shortAddress;
        this.isAck = isAck;
        this.isBroadcast = isBroadcast;
        this.route = route;
    }

    public long getAddress() {
        return address;
    }

    public short getShortAddress() {
        return shortAddress;
    }

    public boolean isAck() {
        return isAck;
    }

    public boolean isBroadcast() {
        return isBroadcast;
    }

    public short[] getRoute() {
        return route;
    }

    public boolean isDirectToCoordinator() {
        return route.length == 0;
    }

    @Override
    public String toString() {
        return new StringBuilder("XbeeRouteRecordIndicator{")
                .append("address=")
                .append(String.format("0x%08X", address))
                .append(", shortAddress=")
                .append(String.format("0x%04X", shortAddress))
                .append(", isAck=")
                .append(isAck)
                .append(", isBroadcast=")
                .append(isBroadcast)
                .append(", isDirect=")
                .append(isDirectToCoordinator())
                .append(", route=")
                .append(Arrays.toString(route))
                .append('}')
                .toString();
    }
}
