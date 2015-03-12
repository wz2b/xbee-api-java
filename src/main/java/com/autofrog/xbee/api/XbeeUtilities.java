package com.autofrog.xbee.api;

/**
 * A few conversion/convenience utility methods for dealing with binary data
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


    public static String toHex(byte[] arr) {
        StringBuffer buf = new StringBuffer(arr.length >> 1);

        for (byte b : arr) {
            buf.append(String.format("%02X", b));
        }

        return buf.toString();
    }
}
