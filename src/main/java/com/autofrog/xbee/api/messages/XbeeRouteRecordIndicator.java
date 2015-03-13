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

    /**
     * Get the route
     *
     * @return array of hops, excluding the source and destination.  Will be empty if
     * the route is direct.
     */
    public short[] getRoute() {
        return route;
    }

    /**
     * Get the number of hops in this route
     *
     * @return number of addresses in the route, excluding the source and destination
     */
    public int getNumHops() {
        return route.length;
    }

    public boolean isDirectToCoordinator() {
        return route.length == 0;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("XbeeRouteRecordIndicator {")
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
                .append(", route=");

        if (route.length == 0) {
            sb.append("direct");
        } else {
            boolean first = true;
            for (short r : route) {
                if (!first) {
                    sb.append(",");
                } else {
                    first = false;
                }

                if(r == 0x0000) {
                    sb.append(String.format("coordinator"));
                } else {
                    sb.append(String.format("0x%04X", r));
                }
            }
        }

        sb.append(" }");

        return sb.toString();
    }
}
