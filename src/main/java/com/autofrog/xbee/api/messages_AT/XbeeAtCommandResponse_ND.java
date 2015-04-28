package com.autofrog.xbee.api.messages_AT;

import com.autofrog.xbee.api.cache.XbeeDeviceTypeEnum;
import com.autofrog.xbee.api.protocol.XbeeDeviceId;
import com.autofrog.xbee.api.util.XbeeUtilities;

import java.util.Arrays;

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
public class XbeeAtCommandResponse_ND extends XbeeAtCommandResponse {
    private static final int START_MY = 0;
    private static final int START_ADDRESS = 2;
    private static final int START_NI = 10;
    private static final int START_PARENT_NETWORK_ADDRESS_AFTER_NI = 0;
    private static final int START_DEVICE_TYPE_AFTER_NI = 2;
    private static final int START_STATUS_AFTER_NI = 3;
    private static final int START_PROFILE_ID_AFTER_NI = 5;
    private static final int START_MFG_ID_AFTER_NI = 7;
    private static final int START_MODULE_TYPE_AFTER_NI = 9;
    private static final int START_PRODUCT_TYPE_AFTER_NI = 11;
    private final int address;
    private final XbeeDeviceId deviceId;
    private final String name;
    private final int profileId;
    private final int mfgId;
    private final int parentAddress;
    private final XbeeDeviceTypeEnum deviceType;
    private final int deviceStatus;
    private final AtCommandStatus commandStatus;
    private final int moduleType;
    private final int productType;
    private int len_NI;


    public XbeeAtCommandResponse_ND(String command,
                                    byte statusCode,
                                    AtCommandStatus status,
                                    byte[] data,
                                    byte sequence) {

        super(command, statusCode, status, data, sequence);

        StringBuilder sb = new StringBuilder();
        int i = START_NI;
        while (data[i] != 0) {
            i++;
        }
        this.len_NI = i - START_NI;

        this.address = XbeeUtilities.toUnsignedIntLittleEndien(Arrays.copyOfRange(data, START_MY, START_ADDRESS));
        this.deviceId = new XbeeDeviceId(Arrays.copyOfRange(data, START_ADDRESS, START_NI));
        this.name = new String(Arrays.copyOfRange(data, START_NI, START_NI + len_NI));
        this.profileId = XbeeUtilities.toUnsignedIntLittleEndien(getSliceAfterNI(START_PROFILE_ID_AFTER_NI, 2));
        this.mfgId = XbeeUtilities.toUnsignedIntLittleEndien(getSliceAfterNI(START_MFG_ID_AFTER_NI, 2));
        this.parentAddress = XbeeUtilities.toUnsignedIntLittleEndien(getSliceAfterNI(START_PARENT_NETWORK_ADDRESS_AFTER_NI, 2));
        this.deviceStatus = XbeeUtilities.toUnsignedIntBigEndien(getSliceAfterNI(START_STATUS_AFTER_NI, 2));
        this.commandStatus = status;

        int moduleTypeTemp;
        if (data.length > START_NI + len_NI + START_MODULE_TYPE_AFTER_NI) {
            moduleTypeTemp = XbeeUtilities.toUnsignedIntBigEndien(
                    getSliceAfterNI(START_MODULE_TYPE_AFTER_NI, 2));
        } else {
            moduleTypeTemp = 0;
        }
        this.moduleType = moduleTypeTemp;

        int productTypeTemp;
        if (data.length > START_NI + len_NI + START_PRODUCT_TYPE_AFTER_NI) {
            productTypeTemp = XbeeUtilities.toUnsignedIntBigEndien(getSliceAfterNI(START_PRODUCT_TYPE_AFTER_NI, 2));
        } else {
            productTypeTemp = 0;
        }
        this.productType = productTypeTemp;

        int deviceTypeNum = XbeeUtilities.toUnsignedIntBigEndien(
                getSliceAfterNI(START_DEVICE_TYPE_AFTER_NI, 2));
        XbeeDeviceTypeEnum deviceTypeTemp;
        switch (deviceTypeNum & 0x00FF) {
            case 0x00:
                deviceTypeTemp = XbeeDeviceTypeEnum.COORDINATOR;

                break;
            case 0x01:
                deviceTypeTemp = XbeeDeviceTypeEnum.ROUTER;
                break;
            case 0x02:
                deviceTypeTemp = XbeeDeviceTypeEnum.END_DEVICE;
                break;
            default:
                deviceTypeTemp = XbeeDeviceTypeEnum.UNKNOWN;
                break;
        }

        deviceType = deviceTypeTemp;


    }

    public int getAddress() {
        return address;
    }

    public XbeeDeviceId getDeviceId() {
        return deviceId;
    }

    public String getName() {
        return name;
    }

    public int getProfileId() {
        return profileId;
    }

    public int getMfgId() {
        return mfgId;
    }


    public int getParentAddress() {
        return parentAddress;
    }

    public XbeeDeviceTypeEnum getDeviceType() {
        return deviceType;
    }

    public int getDeviceStatus() {
        return deviceStatus;
    }

    private byte[] getSliceAfterNI(int start, int len) {
        start = start + START_NI + len_NI;
        return Arrays.copyOfRange(data, start, start + len);
    }

    public AtCommandStatus getCommandStatus() {
        return commandStatus;
    }

    public int getModuleType() {
        return moduleType;
    }

    public int getProductType() {
        return productType;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("XbeeAtCommandResponse_ND {");
        if (command != null)
            sb.append("command='").append(command).append('\'');

        if (commandStatus != null)
            sb.append(", commandStatus=").append(getCommandStatus());

        sb.append(", statusCode=").append(statusCode);
        sb.append(", sequence=").append(sequence);

        if (deviceId != null)
            sb.append(", address=0x").append(deviceId.toString());

        sb.append(", parent=0x").append(String.format("0x%04X", parentAddress));

        if (deviceType != null)
            sb.append(", deviceType=").append(deviceType);

        sb.append(", deviceStatus=0x").append(String.format("0x%04X", deviceStatus));
        sb.append(", networkAddress=0x").append(String.format("0x%04X", address));
        sb.append(", name='").append(getName()).append('\'');
        sb.append(", profile=0x").append(String.format("0x%04X", profileId));
        sb.append(", mfgId=0x").append(String.format("%04X", mfgId));
        sb.append(", moduleType=0x").append(String.format("0x%04X", moduleType));
        sb.append(", productType=0x").append(String.format("%04X", productType));
        sb.append(" }");
        return sb.toString();
    }
}
