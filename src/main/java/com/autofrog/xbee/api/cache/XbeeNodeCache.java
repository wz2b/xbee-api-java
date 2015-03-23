package com.autofrog.xbee.api.cache;

import com.autofrog.xbee.api.listeners.XbeeMessageListener;
import com.autofrog.xbee.api.messages.XbeeAddressableMessage;
import com.autofrog.xbee.api.messages.XbeeExplicitRxMessage;
import com.autofrog.xbee.api.messages.XbeeNodeDiscovery;
import com.autofrog.xbee.api.messages.XbeeRouteRecordIndicator;
import com.autofrog.xbee.api.protocol.XbeeApiConstants;
import com.autofrog.xbee.api.util.XbeeLogListener;
import com.autofrog.xbee.api.util.XbeeLogger;
import com.autofrog.xbee.api.util.XbeeUtilities;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A cache of discovered device information.
 * <p/>
 * The purpose if this cache is to be able to collect short to long addresses,
 * routes, and other node information.
 * <p/>
 * The Xbee modules (including the coordinator) add device IDs to messages when
 * possible, but because of memory limitations they cannot do it reliably on very
 * large networks.
 * <p/>
 * Part of he challenge is that the 16 bit address of a device can change under certain
 * conditions.  This includes address conflicts or if a device leaves and rejoins the
 * network.  Since the 16 bit address is not static, it is not a reliable way to identify
 * a device.
 * <p/>
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


    private final static XbeeLogger log = XbeeLogger.getLogger(XbeeNodeCache.class);

    public XbeeAddressableMessage filter(XbeeAddressableMessage input) {

        final int address = input.getAddress();
        final byte[] deviceId = input.getDeviceId();

        final boolean device_id_is_unknown = (Arrays.equals(XbeeApiConstants.UNKNOWN_DEVICE_ID, deviceId));
        final boolean device_id_is_known = !device_id_is_unknown;
        final boolean device_id_is_broadcast = (Arrays.equals(XbeeApiConstants.BROADCAST_DEVICE_ID, deviceId));

        final boolean network_address_is_known = (address != XbeeApiConstants.UNKNOWN_ADDR);
        final boolean network_address_is_broadcast = (address == XbeeApiConstants.BROADCAST_ADDR);

        final boolean is_broadcast = network_address_is_broadcast || device_id_is_broadcast;
        final boolean is_resolved = device_id_is_known && network_address_is_known;
        /**
         * Do not modify broadcasts.
         */
        if (is_broadcast) {
            log.log(XbeeLogListener.Level.TRACE, "this is a broadcast", null);
            return input;
        } else if (is_resolved) {
            addressMap.replace(deviceId, address);

            if (input instanceof XbeeRouteRecordIndicator) {
                XbeeRouteRecordIndicator route = (XbeeRouteRecordIndicator) input;
                log.log(XbeeLogListener.Level.DEBUG, "Adding " + route, null);
                routes.put(route.getAddress(), route);
            }

            if (input instanceof XbeeNodeDiscovery) {
                XbeeNodeDiscovery discovery = (XbeeNodeDiscovery) input;
                log.log(XbeeLogListener.Level.DEBUG, "Adding " + discovery, null);
                discoveries.put(input.getAddress(), discovery);
            }

            /*
             * Since this packet came in fully resolved there's nothing else to do
             * to it.
             */
            return input;
        } else {
            /*
             * This packet did NOT come in fully resolved.
             */
            byte[] newDeviceId = addressMap.get(address);
            if (newDeviceId != null) {
                log.log(XbeeLogListener.Level.TRACE, "Replacing device id", null);
                return input.cloneWithNewDeviceId(newDeviceId);
            } else {
                /*
                 * We could not find it - nothing to do but return the original message
                 */
                log.log(XbeeLogListener.Level.TRACE, "Doing nothing", null);
                return input;
            }
        }
    }

    /**
     * Listener of route indication messages
     */
    private final XbeeMessageListener<XbeeExplicitRxMessage> explicitMessageListener = new XbeeMessageListener<XbeeExplicitRxMessage>() {
        @Override
        public void onXbeeMessage(Object sender, XbeeExplicitRxMessage msg) {

            /* TODO: update the address map if the device id has changed */
            if (msg.getDeviceId() != null) {
                addressMap.replace(msg.getDeviceId(), msg.getAddress());
            }
        }
    };


    public XbeeNodeDiscovery get(int networkAddress) {
        return discoveries.get(networkAddress);
    }


}
