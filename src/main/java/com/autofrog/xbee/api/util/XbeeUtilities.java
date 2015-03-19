package com.autofrog.xbee.api.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * A few conversion/convenience utility methods for dealing with binary data
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
public class XbeeUtilities {


    public static int toUnsignedIntLittleEndien(byte[] bytes) {
        int v = 0;
        for (int i = 0; i < bytes.length; i++) {
            v = v + ((bytes[i] & 0xFF) << (8 * i));
        }
        return v;
    }

    public static int toUnsignedIntBigEndien(byte[] bytes) {
        int v = 0;
        for (int i = 0; i < bytes.length; i++) {
            v = (v << (8 * i)) + (bytes[i] & 0xFF);
        }
        return v;
    }


    public static String toHex(byte ... arr) {
        StringBuffer buf = new StringBuffer(arr.length >> 1);

        for (byte b : arr) {
            buf.append(String.format("%02X", b));
        }

        return buf.toString();
    }



}
