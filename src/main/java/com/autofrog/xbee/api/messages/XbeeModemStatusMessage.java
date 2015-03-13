package com.autofrog.xbee.api.messages;

import com.autofrog.xbee.api.protocol.XbeeMessageType;

/**
 * Modem status indication.  There is no address in this message, so it represents the
 * status of the device that is directly connected (usually the coordinator)
 */
public class XbeeModemStatusMessage extends XbeeMessageBase {
    private final XbeeModemStatusEnum status;

    public XbeeModemStatusMessage(XbeeModemStatusEnum status) {
        super(XbeeMessageType.MODEM_STATUS.frameType);
        this.status = status;
    }


    public XbeeModemStatusEnum getStatus() {
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
