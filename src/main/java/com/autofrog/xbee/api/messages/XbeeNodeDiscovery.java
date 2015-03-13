package com.autofrog.xbee.api.messages;

import com.autofrog.xbee.api.XbeeUtilities;
import com.autofrog.xbee.api.protocol.XbeeMessageType;

/**
 * Message recei=ved when a node joins the network, or possibly when one is queried
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


    private int networkAddress;
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
        this.networkAddress = networkAddress;
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.parentDeviceAddress = parentDeviceAddress;
        this.deviceType = deviceType;
        this.event = event;
        this.profileId = profileId;
        this.manufacturerId = manufacturerId;
    }

    @Override
    public String toString() {
        return "XbeeNodeDiscovery{" +
                "networkAddress=" + String.format("0x%04X", networkAddress) +
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
