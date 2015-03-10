package com.autofrog.xbee.api.exceptions;

/**
 * Created by chrisp on 009 3/9/2015.
 */
public class XbeeEmptyMessageException extends XbeeException {
    public XbeeEmptyMessageException() {
    }

    public XbeeEmptyMessageException(String message) {
        super(message);
    }

    public XbeeEmptyMessageException(String message, Throwable cause) {
        super(message, cause);
    }

    public XbeeEmptyMessageException(Throwable cause) {
        super(cause);
    }

    public XbeeEmptyMessageException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
