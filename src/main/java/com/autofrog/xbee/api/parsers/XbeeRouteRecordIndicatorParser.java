 package com.autofrog.xbee.api.parsers;

import com.autofrog.xbee.api.exceptions.XbeeException;
import com.autofrog.xbee.api.messages.XbeeMessageBase;
import com.autofrog.xbee.api.messages.XbeeRouteRecordIndicator;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

 /**
 * Created by chrisp on 012 3/12/2015.
 */
public class XbeeRouteRecordIndicatorParser extends XbeeMessageParserBase {


    @Override
    protected XbeeMessageBase doParse(ByteBuffer buffer) throws XbeeException {
        long address = buffer.getLong();
        short shortAddress = buffer.getShort();
        byte rxOpts = buffer.get();
        boolean isAck = (rxOpts & 0x01) != 0;
        boolean isBroadcast = (rxOpts & 0x02) != 0;
        byte numAddresses = buffer.get();

        short [] route = new short[numAddresses];
        for(int i=0; i < numAddresses; i++) {

        }

        return new XbeeRouteRecordIndicator(address, shortAddress, isAck, isBroadcast, route);
    }
}
