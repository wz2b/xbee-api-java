package com.autofrog.xbee.api.parsers;

import com.autofrog.xbee.api.messages.XbeeExplicitRxMessage;

import java.nio.ByteBuffer;

/**
 * Created by chrisp on 012 3/12/2015.
 */
public class XbeeExplicitRxMessageMessageParser extends XbeeMessageParserBase {

    @Override
    public XbeeExplicitRxMessage doParse(ByteBuffer bb) {
        long sourceAddress = bb.getLong();
        short sourceNetworkAddress = bb.getShort();

        byte sourceEndpoint = bb.get();
        byte destEndpoint = bb.get();

        short clusterId = bb.getShort();
        short profileId = bb.getShort();
        byte rxOpts = bb.get();
          boolean isAck = (rxOpts & 0x01) != 0;
          boolean isBroadcast= (rxOpts & 0x02) != 0;;
          boolean isEncrypted= (rxOpts & 0x20) != 0;;
          boolean isEndDevice= (rxOpts & 0x40) != 0;;
        byte [] rawData = new byte[bb.remaining()];
        bb.get(rawData);

        return new XbeeExplicitRxMessage(sourceAddress,
                sourceNetworkAddress,
                sourceEndpoint,
                destEndpoint,
                profileId,
                clusterId,
                isAck,
                isBroadcast,
                isEncrypted,
                isEndDevice,
                rawData);

    }
}
