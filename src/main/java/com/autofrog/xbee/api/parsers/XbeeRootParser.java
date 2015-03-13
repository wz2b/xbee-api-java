package com.autofrog.xbee.api.parsers;

import com.autofrog.xbee.api.XbeeUtilities;
import com.autofrog.xbee.api.exceptions.XbeeException;
import com.autofrog.xbee.api.messages.XbeeMessageBase;
import com.autofrog.xbee.api.messages.XbeeUnknownMessage;
import com.autofrog.xbee.api.protocol.XbeeMessageType;

import java.io.IOException;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chrisp on 012 3/12/2015.
 */
public class XbeeRootParser {

    private final Map<Byte, XbeeMessageParserBase> parsers;

    public XbeeRootParser() throws XbeeException {
        parsers = new HashMap<Byte, XbeeMessageParserBase>();

        for (XbeeMessageType msgType : EnumSet.allOf(XbeeMessageType.class)) {
            XbeeMessageParserBase parser = null;
            try {
                parser = msgType.getParserClass().newInstance();
                parsers.put(msgType.frameType, parser);
            } catch (InstantiationException e) {
                throw new XbeeException("Unable to instantiate parser for frame type "
                        + msgType.name()
                        + ".  Is there a default constructor?", e);
            } catch (IllegalAccessException e) {
                throw new XbeeException("Unable to instantiate parser for frame type "
                        + msgType.name()
                        + " due to illegal access type. Check constructor visibility.", e);
            }
        }
    }

    public final XbeeMessageBase parse(byte frameType, byte[] bytes) throws XbeeException, IOException {

        XbeeMessageParserBase parser = parsers.get(frameType);
        if(parser != null) {
            return parser.parse(bytes);
        } else {
            return new XbeeUnknownMessage(frameType, bytes);
        }
    }
}
