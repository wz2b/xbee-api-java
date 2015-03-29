package com.autofrog.xbee.api.messages_AT;

import com.autofrog.xbee.api.messages.XbeeTransmitMessageBase;
import com.autofrog.xbee.api.protocol.XbeeDeviceId;
import com.autofrog.xbee.api.util.XbeeUtilities;

import java.nio.ByteBuffer;
import java.util.Arrays;

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
public class XbeeTransmitRemoteATCommand extends XbeeTransmitMessageBase {

    public static final byte frameType = 0x17;
    private final static byte[] DEFAULT_DEVICE_ID = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte) 0xFF, (byte) 0xFF};
    private final static int DEFAULT_NETWORK_ADDRESS = 0xFFFE;

    private final byte sequence;
    private final XbeeDeviceId deviceId;
    private final int netwokAddress;
    private final String command;
    private final byte opts;

    public static enum Option {
        NONE,
        DISABLE_RETRIES,
        APPLY_CHANGES,
        AENABLE_APS,
        USE_EXTENDED_TIMEOUT
    }


    public XbeeTransmitRemoteATCommand(XbeeDeviceId deviceId,
                                       int netwokAddress,
                                       String command,
                                       byte sequence,
                                       Option... options) {
        this.deviceId = deviceId;
        this.netwokAddress = netwokAddress;
        this.command = command;
        this.sequence = sequence;

        byte optsTemp = 0;
        for(Option option : options) {
            switch (option) {
                case DISABLE_RETRIES:
                    optsTemp = (byte)(optsTemp | 0x01);
                    break;
                case APPLY_CHANGES:
                    optsTemp = (byte)(optsTemp | 0x02);
                    break;
                case AENABLE_APS:
                    optsTemp = (byte)(optsTemp | 0x20);
                    break;
                case USE_EXTENDED_TIMEOUT:
                    optsTemp = (byte)(optsTemp | 0x40);
                    break;
                default:
                    break;
            }
        }
        opts= optsTemp;

    }

    @Override
    public byte[] getBytes() {
        ByteBuffer buf = ByteBuffer.allocate(12 + 1 + command.length());
        buf.put(frameType);
        buf.put(sequence);
        buf.put(command.getBytes());
        return buf.array();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("XbeeTransmitRemoteATCommand{");
        sb.append("sequence=").append(sequence);
        sb.append(", deviceId=").append("0x" + deviceId.toString());
        sb.append(", netwokAddress=").append(String.format("%04X", (netwokAddress)));
        sb.append(", command='").append(command).append('\'');
        sb.append(", opts=").append(opts);
        sb.append('}');
        return sb.toString();
    }
}
