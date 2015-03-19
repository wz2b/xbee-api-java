package com.autofrog.xbee.api.messages;

import com.autofrog.xbee.api.protocol.XbeeMessageType;

/**
 * Message recei=ved when a node joins the network, or possibly when one is queried
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
public class XbeeNodeDiscovery extends XbeeMessageBase {

    /**
     * Types of devices that can send node discovery messages
     */
    public static enum DeviceType {
        COORDINATOR,
        ROUTER,
        END_DEVICE;
    }

    /**
     * Types of events that can trigger node discovery messages to be sent
     */
    public static enum EventType {
        NONE,
        PUSHBUTTON,
        JOINED,
        POWER_CYCLE
    }


    private int address;
    private byte[] deviceId;
    private String deviceName;
    private int parentDeviceAddress;
    private DeviceType deviceType;
    private EventType event;
    private int profileId;
    private int manufacturerId;

    public XbeeNodeDiscovery(int networkAddress, byte[] deviceId, String deviceName,
                             int parentDeviceAddress, DeviceType deviceType, EventType event,
                             int profileId, int manufacturerId) {
        super(XbeeMessageType.NODE_DISCOVERY.frameType);
        this.address = networkAddress;
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.parentDeviceAddress = parentDeviceAddress;
        this.deviceType = deviceType;
        this.event = event;
        this.profileId = profileId;
        this.manufacturerId = manufacturerId;
    }


    public int getAddress() {
        return address;
    }

    public byte[] getDeviceId() {
        return deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    /**
     * Get the network (16 bit) address of the parent.
     * @note The 16 bit address is not static.  It can change under certain conditions,
     * such as an address conflict or when a device leaves then re-joins the network.
     * To properly identify a device use its full deviceId.
     *
     * @return
     */
    public int getParentDeviceAddress() {
        return parentDeviceAddress;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public EventType getEvent() {
        return event;
    }

    public int getProfileId() {
        return profileId;
    }

    public int getManufacturerId() {
        return manufacturerId;
    }

    @Override
    public String toString() {
        return "XbeeNodeDiscovery{" +
                "address=" + String.format("0x%04X", address) +
                ", deviceId=" + String.format("0x%04X", deviceId) +
                ", deviceName='" + deviceName + '\'' +
                ", parentDeviceAddress=" + String.format("0x%04X", parentDeviceAddress) +
                ", deviceType=" + deviceType +
                ", event=" + event +
                ", profileId=" + String.format("0x%04X", profileId) +
                ", manufacturerId=" + String.format("0x%04X", manufacturerId) +
                '}';
    }
}
