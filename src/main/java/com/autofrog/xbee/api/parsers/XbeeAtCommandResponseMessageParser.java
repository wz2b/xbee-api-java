package com.autofrog.xbee.api.parsers;

import com.autofrog.xbee.api.exceptions.XbeeException;
import com.autofrog.xbee.api.messages.XbeeAtCommandResponse;
import com.autofrog.xbee.api.messages.XbeeMessageBase;
import com.autofrog.xbee.api.messages.XbeeRouteRecordIndicator;
import com.autofrog.xbee.api.messages_AT.XbeeAtCommandResponse_ND;

import java.nio.ByteBuffer;

/**
 * Created by chrisp on 013 3/13/2015.
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
