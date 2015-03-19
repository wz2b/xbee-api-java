package com.autofrog.xbee.api.parsers;

import com.autofrog.xbee.api.exceptions.XbeeException;
import com.autofrog.xbee.api.messages.XbeeMessageBase;
import com.autofrog.xbee.api.messages.XbeeModemStatusEnum;
import com.autofrog.xbee.api.messages.XbeeModemStatusMessage;

import java.nio.ByteBuffer;

/**
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
public class XbeeModemStatusMessageParser extends XbeeMessageParserBase {

    @Override
    protected XbeeMessageBase doParse(ByteBuffer buffer) throws XbeeException {
        byte code = buffer.get();
        XbeeModemStatusEnum status = XbeeModemStatusEnum.lookup((int)(code & 0xFF));
        return new XbeeModemStatusMessage(status);
    }
}
