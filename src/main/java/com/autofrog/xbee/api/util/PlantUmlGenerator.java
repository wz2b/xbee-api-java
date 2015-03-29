package com.autofrog.xbee.api.util;

import com.autofrog.xbee.api.cache.XbeeDeviceType;
import com.autofrog.xbee.api.cache.XbeeNodeCache;
import com.autofrog.xbee.api.cache.XbeeNodeInformation;
import com.autofrog.xbee.api.protocol.XbeeDeviceId;

import java.util.Map;

/**
 * Generates diagrams and reports from a node database
 * <p/>
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
public class PlantUmlGenerator {

    private final XbeeNodeCache cache;
    /**
     * Our logger
     */
    private final static XbeeLogger log = XbeeLogger.getLogger(PlantUmlGenerator.class);

    public enum PlantUmlOpts {
        SHOW_ALL,
        SHOW_NAME,
        SHOW_PROFILE,
        SHOW_MANUFACTURER,
        SHOW_TYPE
    }

    public PlantUmlGenerator(XbeeNodeCache cache) {
        this.cache = cache;
    }


    public String generatePlantUML(PlantUmlOpts... options) {
        StringBuilder sb = new StringBuilder();

        sb.append("@startuml\n"
                + "skinparam state {\n"
                + "    BackgroundColor<<coordinator>> red\n"
                + "    BackgroundColor<<router>> lightblue\n"
                + "    BackgroundColor<<endDevice>> white\n"
                + "}\n");

        final Map<XbeeDeviceId, XbeeNodeInformation> discoveryMap = cache.getDiscoveryMap();


        /*
         * Try to find a coordinator
         */
        XbeeDeviceId coordinator = cache.findCoordinatorDeviceId();
        if (coordinator != null) {
            dumpDevice(sb, cache.getDiscoveryMap().get(coordinator));
        }

        for (XbeeDeviceId deviceId : discoveryMap.keySet()) {

            XbeeNodeInformation d = discoveryMap.get(deviceId);
            if (coordinator != null && d.getDeviceType() != XbeeDeviceType.COORDINATOR) {
                try {
                    sb = dumpDevice(sb, d);
                } catch (Throwable t) {
                    log.log(XbeeLogListener.Level.ERROR, "Failed to describe device", t);
                }
            }
        }
        sb.append("@enduml\n");
        return sb.toString();
    }

    private StringBuilder dumpDevice(StringBuilder sb, XbeeNodeInformation info) {
        String name = info.getDeviceName();
        if (info == null) {
            name = String.format("Device %04X\n", info.getAddress());
        }

        int address = info.getAddress();

        String deviceTypeString = "Router";
        if (address == 0)
            deviceTypeString = "Coordinator";

        XbeeDeviceType dt = info.getDeviceType();
        if (dt == null) {
            sb.append(String.format("state %04X as \"%s\" <<%s>> {\n",
                    address,
                    "unknown", "unknown"));
        } else if (dt == XbeeDeviceType.COORDINATOR) {
            /* Don't show address on coordinator */
            sb.append(String.format("state %04X as \"%s\" <<%s>> {\n",
                    address,
                    deviceTypeString,
                    deviceTypeString.toLowerCase()));
        } else {
            sb.append(String.format("state %04X as \"%s %04X\" <<%s>> {\n",
                    address,
                    deviceTypeString,
                    address,
                    deviceTypeString.toLowerCase()));
        }

        sb.append(String.format("    %04X: %s\n", address, deviceTypeString));
        sb.append(String.format("    %04X: Manufacturer: %04X\n", address, info.getManufacturerId()));
        sb.append(String.format("    %04X: Profile: %04X\n", address, info.getProfileId()));
        sb.append(String.format("    %04X: Parent: %04X\n", address, info.getParentDeviceAddress()));


        sb.append(String.format("    %04X: %s\n", address, dt));
        sb.append(String.format("    %04X: Name: %s\n", address, name));
        sb.append("}\n");

        /*
         * Dump routes
         */
        if (info.getRoute() == null || info.getRoute().length == 0) {
            /*
             * If it either has no route or else the route length is zero it is
             * probably talking directly to the coordinator
             */
            sb.append(String.format("%04X --> coordinator\n", address));
        } else {
            /*
             * It has a route of at least one hop
             */
            sb.append(String.format("%04X", address));
            for (Integer hop : info.getRoute()) {
                sb.append(String.format("%04X", hop));

                if (cache.isCoordinator(hop)) {
                    sb.append(" --> coordinator");
                } else {
                    sb.append(String.format(" --> %04X", hop));
                }
            }
            sb.append("\n");
        }
        return sb;
    }
}