package com.autofrog.xbee.api.protocol;

import com.autofrog.xbee.api.util.XbeeUtilities;

import java.util.Arrays;

/**
 * Created by chrisp on 028 3/28/2015.
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
public class XbeeDeviceId implements Comparable<XbeeDeviceId> {

    private final byte[] deviceIdBytes;

    public XbeeDeviceId(byte[] deviceId) {
        this.deviceIdBytes = deviceId;
    }

    public byte[] getBytes() {
        return deviceIdBytes;
    }

    public String toString() {
        if (deviceIdBytes == null) {
            return "null";
        } else {
            return "0x" + XbeeUtilities.toHex(deviceIdBytes);
        }
    }

    @Override
    public boolean equals(Object compareTo) {
        if (this == compareTo)
            return true;

        if (!(compareTo instanceof XbeeDeviceId))
            return false;

        XbeeDeviceId that = (XbeeDeviceId) compareTo;
        return Arrays.equals(deviceIdBytes, that.deviceIdBytes);
    }

    @Override
    public int hashCode() {
        return deviceIdBytes != null ? Arrays.hashCode(deviceIdBytes) : 0;
    }


    public int compareTo(XbeeDeviceId that) {
        if (this.equals(that))
            return 0;

        if (that == null)
            return -1;

        if(deviceIdBytes.length != that.deviceIdBytes.length) {
            return deviceIdBytes.length - that.deviceIdBytes.length;
        }

        for(int i = 0; i < deviceIdBytes.length; i++) {
            if(deviceIdBytes[i] != that.deviceIdBytes[i]) {
                return deviceIdBytes[i] - that.deviceIdBytes[i];
            }
        }

        return 0;
    }

    public boolean isUnknown() {
        return Arrays.equals(deviceIdBytes, XbeeApiConstants.UNKNOWN_DEVICE_ID);
    }

    public boolean isBroadcast() {
        return Arrays.equals(deviceIdBytes, XbeeApiConstants.BROADCAST_DEVICE_ID);
    }
}
