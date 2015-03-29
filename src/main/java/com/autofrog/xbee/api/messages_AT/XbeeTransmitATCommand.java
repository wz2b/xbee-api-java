package com.autofrog.xbee.api.messages_AT;

import com.autofrog.xbee.api.messages.XbeeTransmitMessageBase;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
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
public class XbeeTransmitATCommand extends XbeeTransmitMessageBase {

    public static final byte frameType = 0x08;

    private final String command;
    private final byte sequence;

    public XbeeTransmitATCommand(String command) {
        this(command, (byte) 0);
    }

    public XbeeTransmitATCommand(String command, byte sequence) {
        this.command = command;
        this.sequence = sequence;
    }

    @Override
    public byte[] getBytes() {
        ByteBuffer buf = ByteBuffer.allocate(2 + command.length());
        buf.put(frameType);
        buf.put(sequence);
        buf.put(command.getBytes());
        return buf.array();
    }
}
