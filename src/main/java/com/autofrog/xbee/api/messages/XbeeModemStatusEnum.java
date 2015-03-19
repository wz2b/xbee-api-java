package com.autofrog.xbee.api.messages;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

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
public enum XbeeModemStatusEnum {
    HARDWARE_RESET(0),
    WATCHDOG_TIMER_RESET(1),
    JOINED_NETWORK(2),
    DISASSOCIATED(3),
    COORDINATOR_STARTED(6),
    NETWORK_CRYPTO_KEY_UPDATED(7),
    VOLTAGE_SUPPLY_LIMIT_EXCEEDED(0x0D),
    MODEM_CONFIGURATION_CHANGED_WHILE_JOINING(0x11),
    STACK_ERROR(0x80),
    UNKNOWN(-1);


    XbeeModemStatusEnum(int code) {
        this.code = code;
        int asInt = (int) code;
    }

    private final static Map<Integer, XbeeModemStatusEnum> reverseTypeMap = new HashMap<Integer, XbeeModemStatusEnum>();
    static {
        for (XbeeModemStatusEnum that : EnumSet.allOf(XbeeModemStatusEnum.class))
            reverseTypeMap.put((int) that.code, that);
    }

    /* The code is an int so we can use -1 as an unknown value */
    private final int code;

    public static XbeeModemStatusEnum lookup(int value) {
        XbeeModemStatusEnum status = reverseTypeMap.get(value);
        if (status == null)
            status = UNKNOWN;

        return status;
    }
}

