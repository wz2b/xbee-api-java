package com.autofrog.xbee.api.listeners;

import com.autofrog.xbee.api.exceptions.XbeeEmptyMessageException;
import com.autofrog.xbee.api.exceptions.XbeeException;

/**
 * Created by chrisp on 009 3/9/2015.
 */
public interface XbeeParsingExceptionListener {
    void xbeeParsingError(Object sender, XbeeException exception);
}
