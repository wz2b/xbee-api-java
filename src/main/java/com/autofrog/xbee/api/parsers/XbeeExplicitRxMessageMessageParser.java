package com.autofrog.xbee.api.parsers;

import com.autofrog.xbee.api.messages.XbeeExplicitRxMessage;

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
public class XbeeExplicitRxMessageMessageParser extends XbeeMessageParserBase {

    @Override
    public XbeeExplicitRxMessage doParse(ByteBuffer bb) {
        byte [] deviceId = new byte[8];
        bb.get(deviceId);
        short address = bb.getShort();

        byte sourceEndpoint = bb.get();
        byte destEndpoint = bb.get();

        short clusterId = bb.getShort();
        short profileId = bb.getShort();
        byte rxOpts = bb.get();
          boolean isAck = (rxOpts & 0x01) != 0;
          boolean isBroadcast= (rxOpts & 0x02) != 0;;
          boolean isEncrypted= (rxOpts & 0x20) != 0;;
          boolean isEndDevice= (rxOpts & 0x40) != 0;;
        byte [] rawData = new byte[bb.remaining()];
        bb.get(rawData);

        return new XbeeExplicitRxMessage(deviceId,
                address,
                sourceEndpoint,
                destEndpoint,
                profileId,
                clusterId,
                isAck,
                isBroadcast,
                isEncrypted,
                isEndDevice,
                rawData);

    }
}
