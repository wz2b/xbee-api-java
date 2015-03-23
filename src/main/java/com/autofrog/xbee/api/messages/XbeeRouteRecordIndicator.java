package com.autofrog.xbee.api.messages;

import com.autofrog.xbee.api.protocol.XbeeMessageType;

import java.util.Arrays;

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
public class XbeeRouteRecordIndicator extends XbeeAddressableMessage {

    private final boolean isAck;
    private final boolean isBroadcast;
    private final int[] route;


    public XbeeRouteRecordIndicator(byte[] deviceId,
                                    int address,
                                    boolean isAck,
                                    boolean isBroadcast,
                                    int[] route) {
        super(XbeeMessageType.ROUTE_RECORD_INDICATOR.frameType, deviceId, address);
        this.isAck = isAck;
        this.isBroadcast = isBroadcast;
        this.route = route;
    }

    public boolean isAck() {
        return isAck;
    }

    public boolean isBroadcast() {
        return isBroadcast;
    }

    /**
     * Get the route.
     *
     * @return array of hops, excluding the source and destination.  Will be empty if
     * the route is direct.
     * @note The route is a list of 16 bit network address of devices.  As with other messages,
     * this address is not static.  It can change under certain conditions,
     * such as an address conflict or when a device leaves then re-joins the network.
     * To properly identify a device use its full deviceId.
     */
    public int[] getRoute() {
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
                .append(String.format("0x%04X", address))
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
            for (int r : route) {
                if (!first) {
                    sb.append(",");
                } else {
                    first = false;
                }

                if (r == 0x0000) {
                    sb.append(String.format("coordinator"));
                } else {
                    sb.append(String.format("0x%04X", r));
                }
            }
        }

        sb.append(" }");

        return sb.toString();
    }

    @Override
    protected XbeeRouteRecordIndicator doCloneWithNewDeviceId(byte[] newDeviceId) {
        return new XbeeRouteRecordIndicator(
                newDeviceId,
                this.address,
                this.isAck,
                this.isBroadcast,
                this.route
        );
    }
}
