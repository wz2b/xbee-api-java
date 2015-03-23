package com.autofrog.xbee.api.util;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * An interface for receiving log and debug messages from components of the Xbee library
 * that can be adapted to any logger of your choosing.
 *
 * <p/>
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
public interface XbeeLogListener {

    public static enum Level {
       ALL, DEBUG, ERROR, FATAL, INFO, TRACE, WARN, OFF
    }

    public void xbeeLog(String loggerName, Level level, String message, Throwable t);

}
