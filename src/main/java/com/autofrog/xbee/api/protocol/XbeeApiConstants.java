package com.autofrog.xbee.api.protocol;

/**
 * Constants used by the Xbee API protocol
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
    public final static long ADDR_BROADCAST = 0xFFFF;


    /**
     * Digi's default profile id, used for many of their messages
     */
    public static final short DIGI_PROFILE_ID = (short) (0xC105);
}
