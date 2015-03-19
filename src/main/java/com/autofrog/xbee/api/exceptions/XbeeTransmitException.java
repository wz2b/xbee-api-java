package com.autofrog.xbee.api.exceptions;

/**
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
