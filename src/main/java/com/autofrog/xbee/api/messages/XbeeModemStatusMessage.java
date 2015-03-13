package com.autofrog.xbee.api.messages;

import com.autofrog.xbee.api.protocol.XbeeMessageType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chrisp on 012 3/12/2015.
 */
public class XbeeModemStatusMessage extends XbeeMessageBase {



    private final XbeeModemStatus status;

    public XbeeModemStatusMessage(XbeeModemStatus status) {
        super(XbeeMessageType.MODEM_STATUS.frameType);
        this.status = status;
    }


    public XbeeModemStatus getStatus() {
        return status;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("XbeeModemStatusMessage{");
        sb.append("status=").append(status);
        sb.append('}');
        return sb.toString();
    }
}
