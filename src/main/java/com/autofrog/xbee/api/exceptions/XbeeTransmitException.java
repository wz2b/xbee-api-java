package com.autofrog.xbee.api.exceptions;

/**
 * Created by chrisp on 013 3/13/2015.
 */
public class XbeeTransmitException extends XbeeException {
    public XbeeTransmitException() {
        this("Xbee Transmit Exception");
    }

    public XbeeTransmitException(String message) {
        super(message);
    }

    public XbeeTransmitException(String message, Throwable cause) {
        super(message, cause);
    }

    public XbeeTransmitException(Throwable cause) {
        super(cause);
    }

    public XbeeTransmitException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
