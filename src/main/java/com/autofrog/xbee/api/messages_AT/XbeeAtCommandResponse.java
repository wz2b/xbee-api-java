package com.autofrog.xbee.api.messages_AT;

import com.autofrog.xbee.api.messages.XbeeMessageBase;
import com.autofrog.xbee.api.util.XbeeUtilities;
import com.autofrog.xbee.api.protocol.XbeeMessageType;

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
public class XbeeAtCommandResponse extends XbeeMessageBase {
    protected final String command;
    protected final AtCommandStatus commandStatus;
    protected final byte statusCode;
    protected final byte[] data;
    protected final byte sequence;

    public static enum AtCommandStatus {
        OK, ERROR, INVALID_COMMAND, INVALID_PARAM, TX_FAILURE, UNKNOWN;
    }

    public XbeeAtCommandResponse(String command, byte statusCode, AtCommandStatus status, byte[] data, byte sequence) {
        super(XbeeMessageType.AT_COMMAND_RESPONSE.frameType);
        this.command = command;
        this.commandStatus = status;
        this.statusCode = statusCode;
        this.data = data;
        this.sequence = sequence;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("XbeeAtCommandResponse{");
        sb.append("command='").append(command).append('\'');
        sb.append(", commandStatus=").append(commandStatus);
        sb.append(", statusCode=").append(statusCode);
        sb.append(", data=0x").append(XbeeUtilities.toHex(data));
        sb.append(", sequence=").append(sequence);
        sb.append('}');
        return sb.toString();
    }
}

