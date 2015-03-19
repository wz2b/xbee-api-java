package com.autofrog.xbee.api.cache;

import com.autofrog.xbee.api.listeners.XbeeMessageListener;
import com.autofrog.xbee.api.messages.XbeeExplicitRxMessage;
import com.autofrog.xbee.api.messages.XbeeNodeDiscovery;
import com.autofrog.xbee.api.messages.XbeeRouteRecordIndicator;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A cache of doscpvered device information.
 *
 * The purpose if this cache is to be able to collect short to long addresses,
 * routes, and other node information.
 *
 * The Xbee modules (including the coordinator) add device IDs to messages when
 * possible, but because of memory limitations they cannot do it reliably on very
 * large networks.
 *
 * Part of he challenge is that the 16 bit address of a device can change under certain
 * conditions.  This includes address conflicts or if a device leaves and rejoins the
 * network.  Since the 16 bit address is not static, it is not a reliable way to identify
 * a device.
 *
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
     * Map of network (short) to device id (long) addresses
     */
    private final XbeeConcurrentDeviceAddressMap addressMap = new XbeeConcurrentDeviceAddressMap();

    /**
     * Map of network (short) address to their discovery information
     */
    private final Map<Integer, XbeeNodeDiscovery> discoveries = new ConcurrentHashMap<Integer, XbeeNodeDiscovery>();

    /**
     * Map of network (short) address to route
     */
    private final Map<Integer, XbeeRouteRecordIndicator> routes = new ConcurrentHashMap<Integer, XbeeRouteRecordIndicator>();

    /**
     * List of devices (by short address) that we don't know about.  This is a simply
     * synchornized set.
     */
    private final Set<Short> unknownAddresses = Collections.synchronizedSet(new HashSet<Short>());


    /**
     * Listener of node discovery packets
     */
    private final XbeeMessageListener<XbeeNodeDiscovery> discoveryListener = new XbeeMessageListener<XbeeNodeDiscovery>() {
        @Override
        public void onXbeeMessage(Object sender, XbeeNodeDiscovery msg) {
            /* TODO: figure out if the device ID could be invalid in a node discovery packet */
            addressMap.replace(msg.getDeviceId(), msg.getAddress());

            discoveries.put(msg.getAddress(), msg);
        }
    };

    /**
     * Listener of route indication messages
     */
    private final XbeeMessageListener<XbeeRouteRecordIndicator> routeListener = new XbeeMessageListener<XbeeRouteRecordIndicator>() {
        @Override
        public void onXbeeMessage(Object sender, XbeeRouteRecordIndicator route) {
            /* TODO: figure out if the device id can be invalid in a route record indicator.
             * For example, could it be a null address like 0x0000000000000000 or 0xFFFFFFFFFFFFFFFF
             * or a broadcast address like 0x000000000000FFFF?
             */
            addressMap.replace(route.getDeviceId(), route.getAddress());

            /* TODO: this is NOT safe - the network (short) address can change */
            routes.put(route.getAddress(), route);
        }
    };

    /**
     * Listener of route indication messages
     */
    private final XbeeMessageListener<XbeeExplicitRxMessage> explicitMessageListener = new XbeeMessageListener<XbeeExplicitRxMessage>() {
        @Override
        public void onXbeeMessage(Object sender, XbeeExplicitRxMessage msg) {

            /* TODO: update the address map if the device id has changed */
            if(msg.getDeviceId() != null) {
                addressMap.replace(msg.getDeviceId(), msg.getAddress());
            }
        }
    };


    public XbeeNodeDiscovery get(int networkAddress) {
        return discoveries.get(networkAddress);
    }

}
