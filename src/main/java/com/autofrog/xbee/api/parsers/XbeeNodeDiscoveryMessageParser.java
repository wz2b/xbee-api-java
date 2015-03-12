package com.autofrog.xbee.api.parsers;

import com.autofrog.xbee.api.messages.XbeeNodeDiscovery;

import java.nio.ByteBuffer;

import com.autofrog.xbee.api.messages.XbeeNodeDiscovery.*;

/**
 * Created by chrisp on 012 3/12/2015.
 */
public class XbeeNodeDiscoveryMessageParser extends XbeeMessageParserBase {

    @Override
    public XbeeNodeDiscovery doParse(ByteBuffer bb) {

        int networkAddress = bb.getShort() & 0xFFFF;
        byte[] deviceId = new byte[8];
        bb.get(deviceId);

        StringBuilder sb = new StringBuilder();
        char ch;
        do {
            ch = (char) bb.get();
            if (ch != 0) {
                sb.append(ch);
            }

        } while (ch != 0);
        String deviceName = sb.toString();


        int parentDeviceAddress = bb.getShort() & 0xFFFF;

        byte deviceTypeByte = bb.get();
        XbeeNodeDiscovery.DeviceType type = null;
        switch (deviceTypeByte) {
            case 0:
                type = DeviceType.COORDINATOR;
                break;
            case 1:
                type = DeviceType.ROUTER;
                break;
            case 2:
                type = DeviceType.END_DEVICE;
                break;
        }

        byte eventTypeByte = bb.get();
        EventType event = null;
        switch (eventTypeByte) {
            case 0:
                event = EventType.NONE;
                break;
            case 1:
                event = EventType.PUSHBUTTON;
                break;
            case 2:
                event = EventType.JOINED;
                break;
            case 3:
                event = EventType.POWER_CYCLE;
                break;
            default:
                event = EventType.NONE;
                break;
        }

        int profileId = bb.getShort() & 0xFFFF;
        int mfgId = bb.getShort() & 0xFFFF;


        return new XbeeNodeDiscovery(networkAddress,
                deviceId,
                deviceName,
                parentDeviceAddress,
                type,
                event,
                profileId,
                mfgId);
    }
}