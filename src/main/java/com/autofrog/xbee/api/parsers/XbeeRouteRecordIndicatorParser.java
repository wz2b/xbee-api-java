 package com.autofrog.xbee.api.parsers;

import com.autofrog.xbee.api.exceptions.XbeeException;
import com.autofrog.xbee.api.messages.XbeeMessageBase;
import com.autofrog.xbee.api.messages.XbeeRouteRecordIndicator;
import com.autofrog.xbee.api.protocol.XbeeDeviceId;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

 /**
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
public class XbeeRouteRecordIndicatorParser extends XbeeMessageParserBase {


    @Override
    protected XbeeMessageBase doParse(ByteBuffer buffer) throws XbeeException {

        byte [] deviceIdBuffer = new byte[8];
        buffer.get(deviceIdBuffer);
        int shortAddress = (int) (buffer.getShort() & 0xFFFF);
        byte rxOpts = buffer.get();
        boolean isAck = (rxOpts & 0x01) != 0;
        boolean isBroadcast = (rxOpts & 0x02) != 0;
        byte numAddresses = buffer.get();

        int [] route = new int[numAddresses];
        for(int i=0; i < numAddresses; i++) {
            int addr = (int) (buffer.getShort() & 0xFFFF);
            route[i] = addr;
        }

        return new XbeeRouteRecordIndicator(new XbeeDeviceId(deviceIdBuffer), shortAddress, isAck, isBroadcast, route);
    }
}
