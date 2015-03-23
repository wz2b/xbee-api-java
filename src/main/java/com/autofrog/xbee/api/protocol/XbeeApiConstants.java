package com.autofrog.xbee.api.protocol;

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
public class XbeeApiConstants {

    /**
     * Flag constant.  Separates and defines messages.
     */
    public static final byte API_FLAG = 0x7E;

    /**
     * Escape constant.  Escapes special characters.  The escaped API (mode 2) is
     * used by this library because it gives you the best possible chance of detecting
     * and recovering from errors in the stream, for example if there are overflows.
     */
    public static final byte API_ESCAPE = 0x7D;

    /**
     * XON mode, when he Xbee is telling us it's OK to resume sending after an XOFF.
     * Used only when transmitting.
     */
    public static final byte API_XON = 0x11;

    /**
     * XOFF constant, where the Xbee is telling us it's not ready to accept more bytes.
     * Used only when tramsmitting.
     */
    public static final byte API_XOFF = 0x13;


    /**
     * Broadcast address - for sending to all nodes.
     */
    public final static int BROADCAST_ADDR = 0xFFFF;

    /**
     * Network address for unknown
     */
    public final static int UNKNOWN_ADDR = 0x0000;


    /**
     * Device ID used for broadcasts
     */
    public static final byte[] BROADCAST_DEVICE_ID = new byte[]{
            0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, (byte) 0xFF, (byte) 0xFF
    };

    /**
     * Device ID for unknown messages
     */
    public static final byte[] UNKNOWN_DEVICE_ID = new byte[]{
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF
    };

    /**
     * Digi's default profile id, used for many of their messages
     */
    public static final short DIGI_PROFILE_ID = (short) (0xC105);

}
