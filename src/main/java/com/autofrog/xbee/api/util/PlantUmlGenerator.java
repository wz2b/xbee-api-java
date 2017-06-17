package com.autofrog.xbee.api.util;

import com.autofrog.xbee.api.cache.XbeeDeviceTypeEnum;
import com.autofrog.xbee.api.cache.XbeeNodeCache;
import com.autofrog.xbee.api.cache.XbeeNodeInformation;
import com.autofrog.xbee.api.protocol.XbeeDeviceId;

import java.util.Map;

/**
 * Generates diagrams and reports from a node database
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

    /**
     * Our logger
     */
    private final static XbeeLogger log = XbeeLogger.getLogger(PlantUmlGenerator.class);
    private final XbeeNodeCache cache;

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
            log.log(XbeeLogListener.Level.INFO, "Dumping the coordinator", null);
            XbeeNodeInformation actualCoordinator = discoveryMap.get(coordinator);
            dumpDevice(sb, actualCoordinator, false);
        } else {
            System.err.println("No coordinator");
        }

        for (XbeeDeviceId deviceId : discoveryMap.keySet()) {
            XbeeNodeInformation d = discoveryMap.get(deviceId);
            if (!d.isCoordinator()) {
                try {
                    sb = dumpDevice(sb, d, true);
                } catch (Throwable t) {
                    log.log(XbeeLogListener.Level.ERROR, "Failed to describe device", t);
                }
            } else {
                System.err.println("Skipping coordinator " + d.getDeviceName());
            }
        }
        sb.append("@enduml\n");
        return sb.toString();
    }

    private StringBuilder dumpDevice(StringBuilder sb, XbeeNodeInformation info, boolean dumpRoutes) {
        String name = info.getDeviceName();
        if (info == null) {
            name = String.format("Device %04X\n", info.getAddress());
        }

        int address = info.getAddress();

        String deviceTypeString;


        XbeeDeviceTypeEnum dt = info.getDeviceType();
        if (dt == null) {
            sb.append(String.format("state %04X as \"%s\" <<%s>> {\n",
                    address,
                    "unknown", "unknown"));
        } else if (dt == XbeeDeviceTypeEnum.COORDINATOR) {
            /* Don't show address on coordinator */
            sb.append(String.format("state %04X as \"%s\" <<%s>> {\n",
                    address,
                    dt,
                    dt.toString().toLowerCase()));
        } else {
            sb.append(String.format("state %04X as \"%s %04X\" <<%s>> {\n",
                    address,
                    dt,
                    address,
                    dt.toString().toLowerCase()));
        }

        sb.append(String.format("    %04X: %s\n", address, name));
        sb.append(String.format("    %04X: %s\n", address, info.getDeviceId().toString()));
        sb.append(String.format("    %04X: %s %04X\n", address, dt, address));
        sb.append(String.format("    %04X: Manufacturer: %04X\n", address, info.getManufacturerId()));
        sb.append(String.format("    %04X: Profile: %04X\n", address, info.getProfileId()));
        sb.append(String.format("    %04X: Parent: %04X\n", address, info.getParentDeviceAddress()));

        sb.append("}\n");

        /*
         * Dump routes
         */
        if (dumpRoutes) {
            if (info.getRoute() == null || info.getRoute().length == 0 || !info.isCoordinator()) {
            /*
             * If it either has no route or else the route length is zero it is
             * probably talking directly to the coordinator
             */
                sb.append(String.format("0000 --> %04X\n", address));
            } else {
            /*
             * It has a route of at least one hop
             */
                sb.append(String.format("%04X", address));
                int[] hops = info.getRoute();
                for (int i = hops.length - 1; i > 0; i--) {

                    sb.append(String.format("%04X", hops[i]));

                    if (cache.isCoordinator(hops[i])) {
                        sb.append(" --> 0000");
                    } else {
                        sb.append(String.format(" --> %04X", hops[i]));
                    }
                }
                sb.append("\n");
            }
        }
        return sb;
    }

    public enum PlantUmlOpts {
        SHOW_ALL,
        SHOW_NAME,
        SHOW_PROFILE,
        SHOW_MANUFACTURER,
        SHOW_TYPE
    }
}
