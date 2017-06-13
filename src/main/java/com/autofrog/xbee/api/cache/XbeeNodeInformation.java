package com.autofrog.xbee.api.cache;

import com.autofrog.xbee.api.protocol.XbeeDeviceId;

import java.util.Arrays;

/**
 * A mutable description of one node on the network
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
    private XbeeDeviceTypeEnum deviceType;
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

    public XbeeDeviceTypeEnum getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(XbeeDeviceTypeEnum deviceType) {
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


    public boolean isCoordinator() {
        System.err.println(String.format("Is this the coordinator?  Address %04X = %s", address, deviceType));
        if (address == 0) {
            System.err.println(String.format("This is the coordinator because its address is %04X (%s)", address, deviceType));
            return true;
        } else {
            boolean maybe = deviceType == XbeeDeviceTypeEnum.COORDINATOR;
            System.err.println(String.format("%04X is %s the coordinator because its type (%s)",
                    address,
                    maybe ? "" : "NOT",
                    deviceType));
            return maybe;
        }
    }

    public boolean isRouter() {
        return deviceType == XbeeDeviceTypeEnum.ROUTER;
    }

    public boolean isEndDevice() {
        return deviceType == XbeeDeviceTypeEnum.END_DEVICE;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("XbeeNodeInformation{");
        sb.append("deviceId=").append(deviceId);
        sb.append(", address=").append(address);
        sb.append(", deviceName='").append(deviceName).append('\'');
        sb.append(", parentDeviceAddress=").append(parentDeviceAddress);
        sb.append(", deviceType=").append(deviceType);
        sb.append(", profileId=").append(profileId);
        sb.append(", manufacturerId=").append(manufacturerId);
        sb.append(", route=").append(Arrays.toString(route));
        sb.append('}');
        return sb.toString();
    }
}
