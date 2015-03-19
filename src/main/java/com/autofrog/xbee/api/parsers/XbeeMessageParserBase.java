package com.autofrog.xbee.api.parsers;

import com.autofrog.xbee.api.exceptions.XbeeException;
import com.autofrog.xbee.api.messages.XbeeMessageBase;
import com.autofrog.xbee.api.protocol.XbeeMessageType;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
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
 * </pre>
 */
public abstract class XbeeMessageParserBase {



    /**
     * Create an xbee message from an array of bytes
     *
     * @param bytes
     * @return
     */
    final public XbeeMessageBase parse(byte[] bytes) throws XbeeException {
        ByteBuffer buffer = ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN);
        return doParse(buffer);
    }

    /**
     * Create an xbee message from a byte buffer.  Since this may be a user-supplied
     * byte buffer, the ordering will be preserved.  This will allow the user to reuse,
     * reset, or otherwise continue using the buffer as needed.
     *
     * @param buffer byte buffer.
     * @return
     */
    final public XbeeMessageBase parse(ByteBuffer buffer) throws XbeeException {

        /* Preserve the original byte ordering*/
        ByteOrder originalOrdering = buffer.order();

        /* Set the required xbee byte ordering */
        buffer.order(ByteOrder.BIG_ENDIAN);

        /* Try to parse a message */
        XbeeMessageBase message = parse(buffer);

        /* Restore the original byte ordering to the buffer, in case user wants to use it again */
        buffer.order(originalOrdering);

        /* return the resulting xbee message */
        return message;
    }

    /**
     * Parse a message.  Must be implemented by message-specific parsers.
     *
     * @param buffer
     * @return
     */
    protected abstract XbeeMessageBase doParse(ByteBuffer buffer) throws XbeeException;


}
