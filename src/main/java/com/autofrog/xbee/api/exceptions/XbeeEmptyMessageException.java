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
public class XbeeEmptyMessageException extends XbeeException {
    public XbeeEmptyMessageException() {
        this("Empty message");
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
