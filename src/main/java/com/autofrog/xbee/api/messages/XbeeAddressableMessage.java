package com.autofrog.xbee.api.messages;

import com.autofrog.xbee.api.protocol.XbeeDeviceId;

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
public abstract class XbeeAddressableMessage extends XbeeMessageBase {
    protected final XbeeDeviceId deviceId;
    protected final int address;


    public XbeeAddressableMessage(byte rawFrameType, XbeeDeviceId deviceId, int address) {
        super(rawFrameType);
        this.deviceId = deviceId;
        this.address = address;
    }

    /**
     * Get the network (16 bit) address of the device.
     * @note The 16 bit address is not static.  It can change under certain conditions,
     * such as an address conflict or when a device leaves then re-joins the network.
     * To properly identify a device use its full deviceId.
     *
     * @return
     */
    public final int getAddress() {
        return address;
    }

    public XbeeDeviceId  getDeviceId() {
        return deviceId;
    }


    /**
     * Messages must implement this - essentially a clone of the original message with
     * a new network (16-bit) address.
     * @param newDeviceId new address
     * @return
     */
    protected abstract XbeeAddressableMessage doCloneWithNewDeviceId(XbeeDeviceId newDeviceId);

    /**
     * Return a copy of the object with a modified network address
     * @param newDeviceId
     * @return
     */
    public XbeeAddressableMessage cloneWithNewDeviceId(XbeeDeviceId newDeviceId) {
        return doCloneWithNewDeviceId(newDeviceId);
    }

}
