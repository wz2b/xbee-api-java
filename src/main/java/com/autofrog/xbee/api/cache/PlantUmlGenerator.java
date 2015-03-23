package com.autofrog.xbee.api.cache;

import com.autofrog.xbee.api.messages.XbeeNodeDiscovery;

import java.util.Map;

/**
 * Generates diagrams and reports from a node database
 *
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

    private final XbeeConcurrentDeviceAddressMap addressMap;
    private final Map<Integer, XbeeNodeDiscovery> discoveries;

    public enum PlantUmlOpts {
        SHOW_ALL,
        SHOW_NAME,
        SHOW_PROFILE,
        SHOW_MANUFACTURER,
        SHOW_TYPE
    }

    public PlantUmlGenerator(XbeeConcurrentDeviceAddressMap addressMap, Map<Integer, XbeeNodeDiscovery> discoveries) {
        this.addressMap = addressMap;
        this.discoveries = discoveries;
    }


    public String generatePlantUML(PlantUmlOpts... options) {

    /*
     * Example PlantUML Output
     *
     * @startuml
     *
     * skinparam state {
     *    BackgroundColor<<coordinator>> lightblue
     *    BackgroundColor<<router>> red
     *    BackgroundColor<<endDevice>> yellow
     * }
     * state 01E3 as "Router 01E3" <<coordinator>> {
     * 01E3: Coordinator
     * 01E3: 0x00A01020340303FD
     * 01E3: Manufacturer: 0x0001
     * 01E3: Profile ID: 0x0001
     * 01E3: Name: xxxHere is its name
     * }
     * state 01E2 as "Router 01E3" <<router>> {
     * 01E2 : Coordinator
     * 01E2 : Router
     * 01E2 : Manufacturer: 0x0001
     * 01E2 : Profile ID: 0x0001
     * 01E2 : Name: Here is its name
     * }
     *
     * 01E3 --> 01E2
     *
     *   @enduml
     */


        StringBuilder sb = new StringBuilder();

        for (int id : discoveries.keySet()) {
            XbeeNodeDiscovery d = discoveries.get(id);

            String name;

            if (d.getDeviceName() != null) {
                name = d.getDeviceName();
            } else {
                name = String.format("%04X", d.getAddress());
            }

            sb.append(name);
        }


        return sb.toString();
    }

}
