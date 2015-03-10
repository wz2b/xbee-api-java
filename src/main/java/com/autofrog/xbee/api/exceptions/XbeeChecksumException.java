package com.autofrog.xbee.api.exceptions;

/**
 * Created by chrisp on 009 3/9/2015.
 */
public class XbeeChecksumException extends XbeeException {
    public XbeeChecksumException() {
    }

    public XbeeChecksumException(String message) {
        super(message);
    }

    public XbeeChecksumException(String message, Throwable cause) {
        super(message, cause);
    }

    public XbeeChecksumException(Throwable cause) {
        super(cause);
    }

    public XbeeChecksumException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
