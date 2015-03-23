package com.autofrog.xbee.api.cache;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A thread-safe address map that tracks 16-but network addresses to physical
 * device IDs (OUI or MAC addresses).
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
public class XbeeConcurrentDeviceAddressMap {
    private final static Map<Integer, byte[]> forward = new HashMap<Integer, byte[]>();
    private final static Map<byte[], Integer> reverse = new HashMap<byte[], Integer>();

    public synchronized byte[] get(int address) {
        return forward.get(address);
    }

    public synchronized Integer get(byte[] deviceId) {
        return reverse.get(deviceId);
    }

    public synchronized boolean containsKey(int key) {
        return forward.containsKey(key);
    }

    public synchronized boolean containsKey(byte [] deviceId) {
        return reverse.containsKey(deviceId);
    }

    public synchronized void clear() {
        forward.clear();
        reverse.clear();
    }


    /**
     * Provide a copy of the map at this snapshot in time.  This allows the user to
     * iterate through the map without having to be concerned that an address may
     * have changed, causing him to see the same device at two network addresses.
     *
     * @return copy of this table
     */
    public synchronized Map<Integer, byte[]> copyMap() {
        Map<Integer, byte[]> temp = new HashMap<Integer, byte[]>();
        for(int key : forward.keySet()) {
            temp.put(key, forward.get(key));
        }

        return temp;
    }

    /**
     * Add or replace a network (short) address to device id mapping
     * @param newAddress the new address of the device
     * @param deviceId the device id being mapped
     * @return the previous address, if one exists, or null if not.  If the return
     *  value is different than the new address being provided this is an indication
     *  that the address has changed.
     */
    public synchronized Integer replace(byte[] deviceId, int newAddress) {
        Integer oldAddress = null;

        if (deviceId != null) {
            byte[] oldFwd = forward.get(newAddress);
            oldAddress = reverse.get(deviceId);

            if (oldFwd == null) {
                forward.put(newAddress, deviceId);
            } else if (!Arrays.equals(oldFwd, deviceId)) {
                /* The address has changed */
                forward.remove(newAddress);
            } else {
                /* Do nothing - they are the same */
            }

            if(oldAddress == null) {
                /*
                 * There wasn't an old record, so just put the new one
                 */
                reverse.put(deviceId, newAddress);
            } else if(oldAddress != newAddress) {
                /*
                 * There was an old record - remove it
                 */
                reverse.remove(oldAddress);
                reverse.put(deviceId, newAddress);
            } else {
                /* Do nothing - they are the same. */
            }
        }

        return oldAddress;
    }


}
