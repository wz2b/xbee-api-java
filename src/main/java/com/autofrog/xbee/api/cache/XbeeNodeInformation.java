package com.autofrog.xbee.api.cache;

import com.autofrog.xbee.api.protocol.XbeeDeviceId;

/**
 * A mutable description of one node on the network
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
public class XbeeNodeInformation {

    private final XbeeDeviceId deviceId;
    private int address;
    private String deviceName;
    private int parentDeviceAddress;
    private XbeeDeviceType deviceType;
    private int profileId;
    private int manufacturerId;
    private int[] route;

    public XbeeNodeInformation(XbeeDeviceId deviceId) {
        this.deviceId = deviceId;
    }


    public XbeeDeviceId getDeviceId() {
        return deviceId;
    }

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public int getParentDeviceAddress() {
        return parentDeviceAddress;
    }

    public void setParentDeviceAddress(int parentDeviceAddress) {
        this.parentDeviceAddress = parentDeviceAddress;
    }

    public XbeeDeviceType getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(XbeeDeviceType deviceType) {
        this.deviceType = deviceType;
    }

    public int getProfileId() {
        return profileId;
    }

    public void setProfileId(int profileId) {
        this.profileId = profileId;
    }

    public int getManufacturerId() {
        return manufacturerId;
    }

    public void setManufacturerId(int manufacturerId) {
        this.manufacturerId = manufacturerId;
    }

    public int[] getRoute() {
        return route;
    }

    public void setRoute(int[] route) {
        this.route = route;
    }
}
