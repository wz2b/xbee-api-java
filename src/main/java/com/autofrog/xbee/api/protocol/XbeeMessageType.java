package com.autofrog.xbee.api.protocol;

import com.autofrog.xbee.api.messages.*;
import com.autofrog.xbee.api.parsers.*;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * List of message types and their associated objects and parsers.
 *
 * @note eventually this class will go away, but it will change the java language level
 *   requirements (so not right now)
 */
public enum XbeeMessageType {
    MODEM_STATUS((byte) 0x8A, XbeeModemStatusMessage.class, XbeeModemStatusMessageParser.class),
    EXPLICIT_RX((byte) 0x91, XbeeExplicitRxMessage.class, XbeeExplicitRxMessageMessageParser.class),
    NODE_DISCOVERY((byte) 0x95, XbeeNodeDiscovery.class, XbeeNodeDiscoveryMessageParser.class),
    ROUTE_RECORD_INDICATOR((byte) 0xA1, XbeeRouteRecordIndicator.class, XbeeRouteRecordIndicatorParser.class);


    public final byte frameType;
    private final Class<? extends XbeeMessageBase> messageClass;
    private final Class<? extends XbeeMessageParserBase> parser;

    private final static Map<Byte, XbeeMessageType> reverseTypeMap = new HashMap<Byte, XbeeMessageType>();

    static {
        for (XbeeMessageType thisMessageType : EnumSet.allOf(XbeeMessageType.class))
            reverseTypeMap.put(thisMessageType.frameType, thisMessageType);
    }

    XbeeMessageType(byte value,
                    Class<? extends XbeeMessageBase> messageType,
                    Class<? extends XbeeMessageParserBase> parser) {
        this.frameType = value;
        this.messageClass = messageType;
        this.parser = parser;
    }

    public Class<? extends XbeeMessageParserBase> getParserClass() {
        return parser;
    }

    public static XbeeMessageType getMessageClassFromFrameType(byte frameType) {
        return reverseTypeMap.get(frameType);
    }
}
