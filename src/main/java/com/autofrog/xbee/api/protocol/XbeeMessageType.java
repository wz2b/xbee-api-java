package com.autofrog.xbee.api.protocol;

import com.autofrog.xbee.api.messages.*;
import com.autofrog.xbee.api.messages_AT.XbeeAtCommandResponse;
import com.autofrog.xbee.api.parsers.*;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * List of message types and their associated objects and parsers.
 *
 * @apiNote  eventually this class will go away, but it will change the java language level
 *   requirements (so not right now)
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
public enum XbeeMessageType {
    AT_COMMAND_RESPONSE((byte) 0x88, XbeeAtCommandResponse.class, XbeeAtCommandResponseMessageParser.class),
    MODEM_STATUS((byte) 0x8A, XbeeModemStatusMessage.class, XbeeModemStatusMessageParser.class),
    EXPLICIT_RX((byte) 0x91, XbeeExplicitRxMessage.class, XbeeExplicitRxMessageMessageParser.class),
    NODE_DISCOVERY((byte) 0x95, XbeeNodeDiscovery.class, XbeeNodeDiscoveryMessageParser.class),
    ROUTE_RECORD_INDICATOR((byte) 0xA1, XbeeRouteRecordIndicator.class, XbeeRouteRecordIndicatorParser.class);


    public final byte frameType;
    private final Class<? extends XbeeMessageBase> messageClass;
    private final Class<? extends XbeeMessageParserBase> parserClass;

    private final static Map<Byte, XbeeMessageType> reverseTypeMap = new HashMap<Byte, XbeeMessageType>();

    static {
        for (XbeeMessageType thisMessageType : EnumSet.allOf(XbeeMessageType.class))
            reverseTypeMap.put(thisMessageType.frameType, thisMessageType);
    }

    XbeeMessageType(byte value,
                    Class<? extends XbeeMessageBase> messageType,
                    Class<? extends XbeeMessageParserBase> parserClass) {
        this.frameType = value;
        this.messageClass = messageType;
        this.parserClass = parserClass;
    }

    public Class<? extends XbeeMessageParserBase> getParserClass() {
        return parserClass;
    }

    public static XbeeMessageType getMessageClassFromFrameType(byte frameType) {
        return reverseTypeMap.get(frameType);
    }
}
