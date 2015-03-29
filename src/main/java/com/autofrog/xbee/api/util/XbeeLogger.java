package com.autofrog.xbee.api.util;

import com.autofrog.xbee.api.cache.XbeeNodeCache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * A simple loggerName for internal components.
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
public class XbeeLogger {

    private final static ConcurrentHashMap<String, XbeeLogger> loggers =
            new ConcurrentHashMap<String, XbeeLogger>();

    private final static CopyOnWriteArrayList<XbeeLogListener> listeners
            = new CopyOnWriteArrayList<XbeeLogListener>();

    private final String loggerName;


    public static XbeeLogger getLogger(String name) {
        if(loggers.containsKey(name) == false) {
            loggers.put(name, new XbeeLogger(name));
        }
        return loggers.get(name);
    }


    public static XbeeLogger getLogger(Class clazz) {
        return getLogger(clazz.getCanonicalName());
    }

    public static XbeeLogger getLogger(Object o) {
        return getLogger(o.getClass());
    }


    private XbeeLogger(String name) {
        this.loggerName = name;
    }

    public boolean addListener(XbeeLogListener listener)  {
        return listeners.add(listener);
    }

    public boolean removeListener(XbeeLogListener listener) {
         return listeners.remove(listener);
    }

    public void log(XbeeLogListener.Level level, String message) {
        log(level, message, null);
    }

    public void log(XbeeLogListener.Level level,
                    String message,
                    Throwable exception) {
        for(XbeeLogListener listener : listeners) {
            listener.xbeeLog(loggerName, level, message, exception);
        }
    }


}
