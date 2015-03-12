package com.autofrog.xbee.api;

import com.autofrog.xbee.api.exceptions.XbeeChecksumException;
import com.autofrog.xbee.api.exceptions.XbeeEmptyMessageException;
import com.autofrog.xbee.api.exceptions.XbeeException;
import com.autofrog.xbee.api.listeners.XbeeMessageListener;
import com.autofrog.xbee.api.listeners.XbeeParsingExceptionListener;
import com.autofrog.xbee.api.messages.XbeeMessageBase;
import com.autofrog.xbee.api.parsers.XbeeRootParser;
import com.autofrog.xbee.api.protocol.XbeeApiConstants;
import com.autofrog.xbee.api.protocol.XbeeMessageType;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Main entry point for the Xbee API parser.
 */
public class XbeeMessageParser {

    /**
     * States the parser may be in on a byte-by-byte basis
     */
    public enum RxState {
        WAITING_FOR_FLAG,
        WAITING_FOR_LEN1,
        WAITING_FOR_LEN2,
        WAIT_FOR_FRAME_TYPE,
        COUNTING,
        WAITING_FOR_CHECKSUM
    }

    protected RxState rxState = RxState.WAITING_FOR_FLAG;
    private byte frameType;
    private int count;
    private int len;
    private byte checksum;
    private byte[] buf = null;
    private boolean escaped = false;
    private boolean transmissionPaused = false;
    private final Map<XbeeMessageType, List<XbeeMessageListener>> frameTypeSpecificListeners;
    private final List<XbeeMessageListener> allFrameTypesListeners;
    private final List<XbeeParsingExceptionListener> errorListeners;
    private final XbeeRootParser rootParser;

    public static XbeeMessageParser createDefaultMessageParser() {
        try {
            return new XbeeMessageParser();
        } catch (XbeeException e) {
            return null;
        }
    }

    public XbeeMessageParser() throws XbeeException {
        this(new XbeeRootParser());
    }


    public XbeeMessageParser(XbeeRootParser rootParser) {
        frameTypeSpecificListeners = new ConcurrentHashMap<XbeeMessageType, List<XbeeMessageListener>>();
        allFrameTypesListeners = new CopyOnWriteArrayList<XbeeMessageListener>();
        errorListeners = new CopyOnWriteArrayList<XbeeParsingExceptionListener>();
        this.rootParser =rootParser;
    }


    /**
     * Add a listener for ALL xbee messages
     *
     * @param newListener
     */
    public void addListener(XbeeMessageListener newListener) {
        allFrameTypesListeners.add(newListener);
    }

    /**
     * Add a listener for a specific xbee message type
     *
     * @param frameType
     * @param newListener
     */
    public void addListener(XbeeMessageType frameType, XbeeMessageListener newListener) {

        if (frameTypeSpecificListeners.containsKey(frameType) == false) {
            frameTypeSpecificListeners.put(frameType, new CopyOnWriteArrayList<XbeeMessageListener>());
        }

        frameTypeSpecificListeners.get(frameType).add(newListener);
    }

    /**
     * Remove a listener from everything it's subscribed to
     *
     * @param listenerToRemove
     */
    public void removeListener(XbeeMessageListener listenerToRemove) {
        allFrameTypesListeners.remove(listenerToRemove);

        for (List<XbeeMessageListener> list : frameTypeSpecificListeners.values()) {
            list.remove(listenerToRemove);
        }
    }


    public void addXbeeExceptionListener(XbeeParsingExceptionListener listener) {
        errorListeners.add(listener);
    }

    public void removeXbeeExceptionListener(XbeeParsingExceptionListener listener) {
        errorListeners.remove(listener);
    }

    protected void notifyListenersOfException(XbeeException e) {
        for (XbeeParsingExceptionListener listener : errorListeners) {
            listener.xbeeParsingError(this, e);
        }
    }

    protected void notifyListeners(XbeeMessageBase msg) {
        for (XbeeMessageListener l : allFrameTypesListeners) {
            l.onXbeeMessage(this, msg);
        }

        List<XbeeMessageListener> specificListeners = frameTypeSpecificListeners.get( XbeeMessageType.getMessageClassFromFrameType(msg.getRawFrameType()) );
        if (specificListeners != null) {
            for (XbeeMessageListener specificListener : specificListeners) {
                specificListener.onXbeeMessage(this, msg);
            }
        }
    }

    public void byteIn(byte b) {
        XbeeMessageBase msg = processByte(b);
        if (msg != null) {
            notifyListeners(msg);
        }
    }

    public void bytesIn(byte[] bytes) {
        for (byte b : bytes) {
            byteIn(b);
        }
    }

    public void bytesIn(byte[] bytes, int start, int len) {
        int end = start + len;
        for (int i = start; i < end; i++) {
            byteIn(bytes[i]);
        }
    }

    /**
     * The main state machine
     * @param b
     * @return
     */
    private XbeeMessageBase processByte(byte b) {
        switch (b) {
            case XbeeApiConstants.API_FLAG:
                if (rxState != RxState.WAITING_FOR_FLAG) {
                    /* We did not expect two flags in a row */
                    notifyListenersOfException(new XbeeEmptyMessageException());
                }

                rxState = RxState.WAITING_FOR_LEN1;
                break;
            case XbeeApiConstants.API_ESCAPE:
                escaped = true;
                break;
            case XbeeApiConstants.API_XON:
                transmissionPaused = false;
                break;
            case XbeeApiConstants.API_XOFF:
                transmissionPaused = true;
                break;

            default:
                if (escaped) {
                    b = (byte) (b ^ 0x20);
                    escaped = false;
                }

                switch (rxState) {
                    case WAITING_FOR_FLAG:
                        break;

                    case WAITING_FOR_LEN1:
                        len = b << 8;
                        rxState = RxState.WAITING_FOR_LEN2;

                        break;

                    case WAITING_FOR_LEN2:
                        len = len | b;
                        buf = new byte[len - 1];
                        count = 0;
                        rxState = RxState.WAIT_FOR_FRAME_TYPE;
                        break;

                    case WAIT_FOR_FRAME_TYPE:
                        frameType = b;
                        checksum = frameType;
                        rxState = RxState.COUNTING;
                        break;

                    case COUNTING:
                        buf[count++] = b;
                        checksum = (byte) ((checksum + b) & 0xFF);
                        /* This is len-1 to skip the frame type */
                        if (count >= (len-1)) {
                            rxState = RxState.WAITING_FOR_CHECKSUM;
                        }
                        break;

                    case WAITING_FOR_CHECKSUM:
                        byte expected = (byte) (0xFF - checksum);
                        if (expected == b) {

                            try {
                                return rootParser.parse(frameType, buf);
                            } catch ( Exception e) {
                                /* Notify of some kind of parsing error */
                                notifyListenersOfException(new XbeeException());

                                return null;
                            } finally {
                                rxState = RxState.WAITING_FOR_FLAG;
                            }

                        } else {
                            /* Notify of a checksum error */
                            notifyListenersOfException(new XbeeChecksumException());
                            rxState = RxState.WAITING_FOR_FLAG;
                        }

                }
        }
        return null;
    }

    /**
     * Get the RX state on a byte by byte basis.  Only really useful for debugging.
     *
     * @return
     */
    public RxState getRxState() {
        return rxState;
    }

    public String dumpParserState() {
        return String.format("state=%s len=%d count=%d escaped=%s",
                rxState, len, count, escaped);
    }
}
