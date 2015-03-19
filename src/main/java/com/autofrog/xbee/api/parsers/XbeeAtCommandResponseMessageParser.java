package com.autofrog.xbee.api.parsers;

import com.autofrog.xbee.api.exceptions.XbeeException;
import com.autofrog.xbee.api.messages.XbeeAtCommandResponse;
import com.autofrog.xbee.api.messages.XbeeMessageBase;
import com.autofrog.xbee.api.messages.XbeeRouteRecordIndicator;
import com.autofrog.xbee.api.messages_AT.XbeeAtCommandResponse_ND;

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
public class XbeeAtCommandResponseMessageParser extends XbeeMessageParserBase {

    @Override
    protected XbeeMessageBase doParse(ByteBuffer buffer) throws XbeeException {
        byte sequence = buffer.get();
        byte[] command = new byte[2];
        buffer.get(command);
        byte statusCode = buffer.get();
        byte[] data = new byte[buffer.remaining()];
        buffer.get(data);

        XbeeAtCommandResponse.AtCommandStatus status;
        switch (statusCode) {
            case 0:
                status = XbeeAtCommandResponse.AtCommandStatus.OK;
                break;
            case 1:
                status = XbeeAtCommandResponse.AtCommandStatus.INVALID_COMMAND;
                break;
            case 3:
                status = XbeeAtCommandResponse.AtCommandStatus.INVALID_PARAM;
                break;
            case 4:
                status = XbeeAtCommandResponse.AtCommandStatus.TX_FAILURE;
                break;
            default:
                status = XbeeAtCommandResponse.AtCommandStatus.UNKNOWN;
                break;
        }

        String cmdString = new String(command);

        if(cmdString.equalsIgnoreCase("ND")) {
            return new XbeeAtCommandResponse_ND(cmdString, statusCode, status, data, sequence);
        } else {
            return new XbeeAtCommandResponse(cmdString, statusCode, status, data, sequence);
        }
    }
}
