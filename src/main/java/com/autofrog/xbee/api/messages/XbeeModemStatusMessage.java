package com.autofrog.xbee.api.messages;

import com.autofrog.xbee.api.protocol.XbeeMessageType;

/**
 * Modem commandStatus indication.  There is no address in this message, as it represents the
 * commandStatus of the device that is directly connected (usually the coordinator)
 *
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
        sb.append("commandStatus=").append(status);
        sb.append('}');
        return sb.toString();
    }
}
