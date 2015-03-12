package com.autofrog.xbee.api.protocol;

import com.autofrog.xbee.api.messages.XbeeExplicitRxMessage;
import com.autofrog.xbee.api.messages.XbeeMessageBase;
import com.autofrog.xbee.api.messages.XbeeNodeDiscovery;
import com.autofrog.xbee.api.parsers.XbeeExplicitRxMessageMessageParser;
import com.autofrog.xbee.api.parsers.XbeeNodeDiscoveryMessageParser;
import com.autofrog.xbee.api.parsers.XbeeMessageParserBase;

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
    //    TRANSMIT((byte) 0x10),
    EXPLICIT_RX((byte) 0x91, XbeeExplicitRxMessage.class, XbeeExplicitRxMessageMessageParser.class),
//    RX_IO_SAMPLE((byte) 0x92),
    NODE_DISCOVERY((byte) 0x95, XbeeNodeDiscovery.class, XbeeNodeDiscoveryMessageParser.class);

    private final byte frameType;
    private final Class<? extends XbeeMessageBase> messageClass;
    private final Class<? extends XbeeMessageParserBase> parser;

    private final static Map<Byte, XbeeMessageType> reverseTypeMap = new HashMap<Byte, XbeeMessageType>();

    static {
        for (XbeeMessageType thisMessageType : EnumSet.allOf(XbeeMessageType.class))
            reverseTypeMap.put(thisMessageType.getFrameType(), thisMessageType);
    }

    XbeeMessageType(byte value,
                    Class<? extends XbeeMessageBase> messageType,
                    Class<? extends XbeeMessageParserBase> parser) {
        this.frameType = value;
        this.messageClass = messageType;
        this.parser = parser;
    }

    public byte getFrameType() {
        return frameType;
    }

    public Class<? extends XbeeMessageParserBase> getParserClass() {
        return parser;
    }

    public static XbeeMessageType getMessageClassFromFrameType(byte frameType) {
        return reverseTypeMap.get(frameType);
    }
}
