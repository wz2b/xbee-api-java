package com.autofrog.xbee.api;

import com.autofrog.xbee.api.exceptions.XbeeChecksumException;
import com.autofrog.xbee.api.exceptions.XbeeEmptyMessageException;
import com.autofrog.xbee.api.exceptions.XbeeException;
import com.autofrog.xbee.api.listeners.XbeeMessageListener;
import com.autofrog.xbee.api.listeners.XbeeParsingExceptionListener;
import com.autofrog.xbee.api.messages.XbeeMessageBase;
import com.autofrog.xbee.api.messages.XbeeTransmitATCommand;
import com.autofrog.xbee.api.parsers.XbeeRootParser;
import com.autofrog.xbee.api.protocol.XbeeApiConstants;
import com.autofrog.xbee.api.protocol.XbeeMessageType;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Main entry point for the Xbee API parser.
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
    private boolean transmissionPausedXonXoff = false;
    private final Map<XbeeMessageType, List<XbeeMessageListener>> frameTypeSpecificListeners;
    private final List<XbeeMessageListener> allFrameTypesListeners;
    private final List<XbeeParsingExceptionListener> errorListeners;
    private final XbeeRootParser rootParser;
    private final XbeeTransmitter sender;
    private final ScheduledThreadPoolExecutor scheduler;

    /**
     * Convenience factory to create a new read-only XbeeMessageParser using the default message
     * types.  Unlike the constructor, this won't throw an exception.
     *
     * @return parser, or null if one could not be created
     */
    public static XbeeMessageParser createDefaultMessageParser() {
        try {
            return new XbeeMessageParser();
        } catch (XbeeException e) {
            return null;
        }
    }

    /**
     * Convenience factory to create a new read-write XbeeMessageParser using the default message
     * types.  Unlike the constructor, this won't throw an exception.
     *
     * @return parser, or null if one could not be created
     */
    public static XbeeMessageParser createDefaultMessageParser(XbeeTransmitter sender) {
        try {
            return new XbeeMessageParser(new XbeeRootParser(), sender);
        } catch (XbeeException e) {
            return null;
        }
    }

    /**
     * Create a read-only parser using the default message types
     *
     * @throws XbeeException
     */
    public XbeeMessageParser() throws XbeeException {
        this(new XbeeRootParser(), null);
    }

    /**
     * Create a read-write parser using the default message types
     *
     * @param sender
     * @throws XbeeException
     */
    public XbeeMessageParser(XbeeTransmitter sender) throws XbeeException {
        this(new XbeeRootParser(), sender);
    }

    /**
     * Create a read-only message parser using a custom root message processor, which allows users
     * to define their own messages.
     *
     * @param rootParser
     */
    public XbeeMessageParser(XbeeRootParser rootParser) {
        this(rootParser, null);
    }

    /**
     * Create a read-write message parser using a custom root message processor, which allows
     * users to define their own messages.
     *
     * @param rootParser
     * @param sender
     */
    public XbeeMessageParser(XbeeRootParser rootParser, XbeeTransmitter sender) {
        frameTypeSpecificListeners = new ConcurrentHashMap<XbeeMessageType, List<XbeeMessageListener>>();
        allFrameTypesListeners = new CopyOnWriteArrayList<XbeeMessageListener>();
        errorListeners = new CopyOnWriteArrayList<XbeeParsingExceptionListener>();
        this.rootParser = rootParser;
        this.sender = sender;
        this.scheduler = new ScheduledThreadPoolExecutor(1);
        scheduler.setMaximumPoolSize(1);
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

        byte frameTypeCode = msg.getRawFrameType();
        XbeeMessageType frameType = XbeeMessageType.getMessageClassFromFrameType(frameTypeCode);
        if(frameType != null) {
            List<XbeeMessageListener> specificListeners = frameTypeSpecificListeners.get(frameType);
            if (specificListeners != null) {
                for (XbeeMessageListener specificListener : specificListeners) {
                    specificListener.onXbeeMessage(this, msg);
                }
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
     *
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
                transmissionPausedXonXoff = false;
                break;
            case XbeeApiConstants.API_XOFF:
                transmissionPausedXonXoff = true;
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
                        if (count >= (len - 1)) {
                            rxState = RxState.WAITING_FOR_CHECKSUM;
                        }
                        break;

                    case WAITING_FOR_CHECKSUM:
                        byte expected = (byte) (0xFF - checksum);
                        if (expected == b) {

                            try {
                                return rootParser.parse(frameType, buf);
                            } catch (Exception e) {
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


    public void start() {
        scheduler.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                XbeeTransmitATCommand command = new XbeeTransmitATCommand("ND");
                if (sender != null) {
                    sender.send(command);
                }
            }
        }, 0, 5, TimeUnit.MINUTES);
    }

    public void stop() throws InterruptedException {
        scheduler.shutdown();
        scheduler.awaitTermination(30, TimeUnit.SECONDS);
        if (sender != null) {
            sender.shutdown(30, TimeUnit.SECONDS);
        }
    }

    public String dumpParserState() {
        return String.format("state=%s len=%d count=%d escaped=%s",
                rxState, len, count, escaped);
    }
}
