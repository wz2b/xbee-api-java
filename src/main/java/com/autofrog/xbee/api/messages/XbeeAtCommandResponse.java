package com.autofrog.xbee.api.messages;

import com.autofrog.xbee.api.XbeeUtilities;
import com.autofrog.xbee.api.protocol.XbeeMessageType;

/**
 * Created by chrisp on 013 3/13/2015.
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

