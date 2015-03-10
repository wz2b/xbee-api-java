package com.autofrog.xbee.api.exceptions;

/**
 * Created by chrisp on 009 3/9/2015.
 */
public class XbeeException extends Exception {

    public XbeeException() {
    }

    public XbeeException(String message) {
        super(message);
    }

    public XbeeException(String message, Throwable cause) {
        super(message, cause);
    }

    public XbeeException(Throwable cause) {
        super(cause);
    }

    public XbeeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
