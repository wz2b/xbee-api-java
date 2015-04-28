package com.autofrog.xbee.api.parsers;

import com.autofrog.xbee.api.cache.XbeeNodeCache;
import com.autofrog.xbee.api.exceptions.XbeeException;
import com.autofrog.xbee.api.messages.XbeeAddressableMessage;
import com.autofrog.xbee.api.messages.XbeeMessageBase;
import com.autofrog.xbee.api.messages.XbeeNodeDiscovery;
import com.autofrog.xbee.api.messages.XbeeUnknownMessage;
import com.autofrog.xbee.api.messages_AT.XbeeAtCommandResponse_ND;
import com.autofrog.xbee.api.protocol.XbeeMessageType;

import java.io.IOException;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

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
 */
public class XbeeRootParser {

    private final Map<Byte, XbeeMessageParserBase> parsers;
    private final XbeeNodeCache cache;

    public XbeeRootParser() throws XbeeException {
        cache = new XbeeNodeCache();
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
        System.err.println(String.format("%02x", frameType & 0xFF));
        XbeeMessageParserBase parser = parsers.get(frameType);


        if (parser != null) {
            XbeeMessageBase message = parser.parse(bytes);
            if (XbeeAddressableMessage.class.isAssignableFrom(message.getClass())) {
                message = cache.filter((XbeeAddressableMessage) message);
            } else if (message instanceof XbeeNodeDiscovery) {
                XbeeNodeDiscovery nd = (XbeeNodeDiscovery) message ;
                cache.filter(nd);
            } else if(message instanceof XbeeAtCommandResponse_ND) {
                cache.filter((XbeeAtCommandResponse_ND) message);
            }

            return message;
        } else {
            return new XbeeUnknownMessage(frameType, bytes);
        }
    }

    public XbeeNodeCache getCache() {
        return cache;
    }
}
