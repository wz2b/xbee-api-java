package com.autofrog.xbee.api.cache;


import com.autofrog.xbee.api.messages.XbeeAddressableMessage;
import com.autofrog.xbee.api.messages.XbeeNodeDiscovery;
import com.autofrog.xbee.api.messages.XbeeRouteRecordIndicator;
import com.autofrog.xbee.api.messages_AT.XbeeAtCommandResponse;
import com.autofrog.xbee.api.messages_AT.XbeeAtCommandResponse_ND;
import com.autofrog.xbee.api.protocol.XbeeApiConstants;
import com.autofrog.xbee.api.protocol.XbeeDeviceId;
import com.autofrog.xbee.api.util.XbeeLogListener;
import com.autofrog.xbee.api.util.XbeeLogger;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A cache of discovered device information.
 * The purpose if this cache is to be able to collect short to long addresses,
 * routes, and other node information.
 * The Xbee modules (including the coodinatorDeviceId) add device IDs to messages when
 * possible, but because of memory limitations they cannot do it reliably on very
 * large networks.
 * Part of he challenge is that the 16 bit address of a device can change under certain
 * conditions.  This includes address conflicts or if a device leaves and rejoins the
 * network.  Since the 16 bit address is not static, it is not a reliable way to identify
 * a device.
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
public class XbeeNodeCache {

    /**
     * Our logger
     */
    private final static XbeeLogger log = XbeeLogger.getLogger(XbeeNodeCache.class);
    private final ConcurrentHashMap<XbeeDeviceId, XbeeNodeInformation> infos;
    /**
     * List of devices (by short address) that we don't know about.  This is a simply
     * synchornized set.
     */
    private final Set<Short> unknownAddresses;

    public XbeeNodeCache() {
        infos = new ConcurrentHashMap<XbeeDeviceId, XbeeNodeInformation>();
        unknownAddresses = Collections.synchronizedSet(new HashSet<Short>());
    }


    public XbeeAddressableMessage filter(XbeeAddressableMessage input) {

        final int receivedAddress = input.getAddress();
        final XbeeDeviceId receivedDeviceId = input.getDeviceId();

        final boolean device_id_is_unknown = receivedDeviceId.isUnknown();
        final boolean device_id_is_known = !device_id_is_unknown;
        final boolean device_id_is_broadcast = receivedDeviceId.isBroadcast();

        final boolean network_address_is_known = (receivedAddress != XbeeApiConstants.UNKNOWN_ADDR);
        final boolean network_address_is_broadcast = (receivedAddress == XbeeApiConstants.BROADCAST_ADDR);

        final boolean is_broadcast = network_address_is_broadcast || device_id_is_broadcast;
        final boolean is_resolved = device_id_is_known && network_address_is_known;

        /*
         * Do not modify broadcasts.
         */
        if (is_broadcast) {
            log.log(XbeeLogListener.Level.TRACE, "this is a broadcast", null);
            return input;
        } else {
            if (is_resolved) {
                /*
                 * It's a route
                 */
                if (input instanceof XbeeRouteRecordIndicator) {
                    XbeeRouteRecordIndicator route = (XbeeRouteRecordIndicator) input;
                    updateRoute(route);
                    log.log(XbeeLogListener.Level.DEBUG, "Adding " + route, null);
                }

                /*
                 * I's a node discovery package - zigbee flavor
                 */
                if (input instanceof XbeeNodeDiscovery) {
                    XbeeNodeDiscovery discovery = (XbeeNodeDiscovery) input;
                    log.log(XbeeLogListener.Level.DEBUG, "Adding " + discovery, null);
                    updateDiscovery(discovery);
                }

                /*
                 * Since this packet came in fully resolved there's nothing else to do
                 * to it.
                 */
                return input;
            } else {
                /*
                 * This packet did NOT come in fully resolved.  We should know the
                 * Device ID since that can't change (it's in hardware) so it's OK
                 * to trust whatever came in and just swap it.
                 */
                if (receivedDeviceId != null) {
                    log.log(XbeeLogListener.Level.TRACE, "Replacing device id", null);
                    return input.cloneWithNewDeviceId(receivedDeviceId);
                } else {
                /*
                 * We could not find it - nothing to do but return the original message
                 */
                    log.log(XbeeLogListener.Level.TRACE, "Doing nothing", null);
                    return input;
                }
            }
        }
    }


    public XbeeAtCommandResponse filter(XbeeAtCommandResponse input) {

        if (input instanceof XbeeAtCommandResponse_ND) {

            XbeeAtCommandResponse_ND discovery = (XbeeAtCommandResponse_ND) input;

            XbeeDeviceId deviceId = discovery.getDeviceId();

            /*
             * Look up the old record by Device ID
             */
            XbeeNodeInformation info = infos.get(deviceId.toString());
            if (info == null) {
                info = new XbeeNodeInformation(deviceId);
            }

            info.setParentDeviceAddress(discovery.getParentAddress());
            info.setDeviceName(discovery.getName());
            info.setManufacturerId(discovery.getMfgId());


            info.setDeviceType(discovery.getDeviceType());
            info.setAddress(discovery.getAddress());


            put(deviceId, info);
        }

        return input;
    }

    private void put(XbeeDeviceId deviceId, XbeeNodeInformation info) {
        if (infos.containsKey(deviceId.toString())) {
            infos.replace(deviceId, info);
        } else {
            infos.put(deviceId, info);
        }

        System.out.println(String.format("Device map has %d entries", infos.size()));
    }

    private void updateRoute(XbeeRouteRecordIndicator route) {
        XbeeDeviceId deviceId = route.getDeviceId();
            /*
             * Look up the old record by Device ID
             */
        XbeeNodeInformation info = infos.get(deviceId);
        if (info == null) {
            info = new XbeeNodeInformation(deviceId);
        }

        info.setRoute(route.getRoute());
        put(deviceId, info);
    }


    private void updateDiscovery(XbeeNodeDiscovery discovery) {
        XbeeDeviceId deviceId = discovery.getDeviceId();
        XbeeNodeInformation info = infos.get(deviceId);
        if (info == null) {
            info = new XbeeNodeInformation(deviceId);
        }

        info.setParentDeviceAddress(discovery.getParentDeviceAddress());
        info.setDeviceName(discovery.getDeviceName());
        info.setAddress(discovery.getAddress());
        put(deviceId, info);

    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("XbeeConsolidatedNodeCache{");
        sb.append("infos=").append(infos);
        sb.append(", unknownAddresses=").append(unknownAddresses);
        sb.append('}');
        return sb.toString();
    }

    public Map<XbeeDeviceId, XbeeNodeInformation> getDiscoveryMap() {
        return infos;
    }

    public XbeeDeviceId findCoordinatorDeviceId() {
        for (XbeeNodeInformation info : infos.values()) {
            if (isCoordinator(info.getAddress())) {
                return info.getDeviceId();
            }
        }
        return null;
    }

    public boolean isCoordinator(int address) {
        return address == 0x0000;
    }


}
