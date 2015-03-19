package com.autofrog.xbee.api.messages;

import com.autofrog.xbee.api.util.XbeeUtilities;

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
public class XbeeUnknownMessage extends XbeeMessageBase {
    private final byte[] data;

    public XbeeUnknownMessage(byte rawFrameType, byte[] data) {
        super(rawFrameType);
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }

    @Override
    public String toString() {
        return "XbeeUnknownMessage{" +
                "frameType=" + String.format("0x%02x", rawFrameType) +
                ", data=0x" + XbeeUtilities.toHex(data) +
                '}';
    }
}
