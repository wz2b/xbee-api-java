package com.autofrog.xbee.api.parsers;

import com.autofrog.xbee.api.cache.XbeeDeviceTypeEnum;
import com.autofrog.xbee.api.messages.XbeeNodeDiscovery;
import com.autofrog.xbee.api.messages.XbeeNodeDiscovery.EventType;
import com.autofrog.xbee.api.protocol.XbeeDeviceId;

import java.nio.ByteBuffer;



/**
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
public class XbeeNodeDiscoveryMessageParser extends XbeeMessageParserBase {

    @Override
    public XbeeNodeDiscovery doParse(ByteBuffer bb) {

        int networkAddress = bb.getShort() & 0xFFFF;
        byte[] deviceIdBuffer = new byte[8];
        bb.get(deviceIdBuffer);

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
        XbeeDeviceTypeEnum type = null;
        switch (deviceTypeByte) {
            case 0:
                type = XbeeDeviceTypeEnum.COORDINATOR;
                break;
            case 1:
                type = XbeeDeviceTypeEnum.ROUTER;
                break;
            case 2:
                type = XbeeDeviceTypeEnum.END_DEVICE;
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
                new XbeeDeviceId(deviceIdBuffer),
                deviceName,
                parentDeviceAddress,
                type,
                event,
                profileId,
                mfgId);
    }
}
