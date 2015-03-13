package com.autofrog.xbee.api.messages_AT;

import com.autofrog.xbee.api.XbeeUtilities;
import com.autofrog.xbee.api.messages.XbeeAtCommandResponse;

import java.util.Arrays;

/**
 * Created by chrisp on 013 3/13/2015.
 */
public class XbeeAtCommandResponse_ND extends XbeeAtCommandResponse {
    private static final int START_MY = 0;
    private static final int START_ADDRESS = 2;
    private static final int START_NI = 10;
    private final int len_NI;
    private static final int START_PARENT_NETWORK_ADDRESS_AFTER_NI = 0;
    private static final int START_DEVICE_TYPE_AFTER_NI = 2;
    private static final int START_STATUS_AFTER_NI = 3;
    private static final int START_PROFILE_ID_AFTER_NI = 5;
    private static final int START_MFG_ID_AFTER_NI = 7;
    private static final int START_MODULE_TYPE_AFTER_NI = 9;
    private static final int START_PRODUCT_TYPE_AFTER_NI = 11;

    public XbeeAtCommandResponse_ND(String command, byte statusCode, AtCommandStatus status, byte[] data, byte sequence) {
        super(command, statusCode, status, data, sequence);

        StringBuilder sb = new StringBuilder();
        int i = START_NI;
        while (data[i] != 0) {
            i++;
        }
        len_NI = i - START_NI;
    }

    public byte[] getShortAddress() {
        return Arrays.copyOfRange(data, START_MY, START_ADDRESS);
    }

    public byte[] getAddress() {
        return Arrays.copyOfRange(data, START_ADDRESS, START_NI);
    }

    public String getName() {
        return new String(
                Arrays.copyOfRange(data, START_NI, START_NI+len_NI));
    }

    public byte[] getProfileId() {
        return getSliceAfterNI(START_PROFILE_ID_AFTER_NI, 2);
    }

    public byte[] getMfgId() {
        return getSliceAfterNI(START_MFG_ID_AFTER_NI, 2);
    }


    public byte [] getParentAddress() {
        return getSliceAfterNI(START_PARENT_NETWORK_ADDRESS_AFTER_NI, 2);
    }

    public byte [] getDeviceType() {
        return getSliceAfterNI(START_DEVICE_TYPE_AFTER_NI, 2);
    }

    public byte[] getDeviceStatus() {
        return getSliceAfterNI(START_STATUS_AFTER_NI, 2);
    }

    private byte[] getSliceAfterNI(int start, int len) {
        start = start + START_NI + len_NI;
        return Arrays.copyOfRange(data, start, start  + len);
    }

    public AtCommandStatus getCommandStatus() {
        return commandStatus;
    }

    public byte [] getModuleType() {
        if(data.length > START_NI + len_NI + START_MODULE_TYPE_AFTER_NI ) {
            return getSliceAfterNI(START_MODULE_TYPE_AFTER_NI, 2);
        } else {
            return null;
        }
    }

    public byte[] getProductType() {
        if(data.length > START_NI + len_NI + START_PRODUCT_TYPE_AFTER_NI ) {
            return getSliceAfterNI(START_PRODUCT_TYPE_AFTER_NI, 2);
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("XbeeAtCommandResponse_ND {");
        sb.append("command='").append(command).append('\'');
        sb.append(", commandStatus=").append(getCommandStatus());
        sb.append(", statusCode=").append(statusCode);
        sb.append(", sequence=").append(sequence);
        sb.append(", address=0x").append(XbeeUtilities.toHex(getAddress()));
        sb.append(", parent=0x").append(XbeeUtilities.toHex(getParentAddress()));
        sb.append(", deviceType=0x").append(XbeeUtilities.toHex(getDeviceType()));
        sb.append(", deviceStatus=0x").append(XbeeUtilities.toHex(getDeviceStatus()));
        sb.append(", networkAddress=0x").append(XbeeUtilities.toHex(getShortAddress()));
        sb.append(", name='").append(getName()).append('\'');
        sb.append(", profile=0x").append(XbeeUtilities.toHex(getProfileId()));
        sb.append(", mfgId=0x").append(XbeeUtilities.toHex(getMfgId()));

        if(getModuleType() != null) {
            sb.append(", moduleType=0x").append(XbeeUtilities.toHex(getModuleType()));
        } else {
            sb.append(", moduleType=UNKNOWN");
        }

        if(getProductType() != null) {
            sb.append(", productType=0x").append(XbeeUtilities.toHex(getProductType()));
        } else {
            sb.append(", productType=UNKNOWN");
        }

        sb.append(" }");
        return sb.toString();
    }
}
