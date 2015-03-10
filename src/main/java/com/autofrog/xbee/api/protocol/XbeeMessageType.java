package com.autofrog.xbee.api.protocol;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chrisp on 009 3/9/2015.
 */
public enum XbeeMessageType {
    TRANSMIT((byte) 0x10),
    EXPLICIT_RX((byte) 0x91),
    RX_IO_SAMPLE((byte) 0x92),
    NODE_DISCOVERY((byte) 0x95);

    private final byte value;
    private final static Map<Byte, XbeeMessageType> reverse = new HashMap<Byte, XbeeMessageType>();

    static {
        for(XbeeMessageType s : EnumSet.allOf(XbeeMessageType.class))
            reverse.put(s.valueOf(), s);
    }

    XbeeMessageType(byte value) {
        this.value = value;
    }

    public byte valueOf() {
        return value;
    }

    public static XbeeMessageType lookup(byte value) {
        return reverse.get(value);
    }
}
