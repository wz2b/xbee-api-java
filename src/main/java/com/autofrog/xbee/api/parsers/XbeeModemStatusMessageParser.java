package com.autofrog.xbee.api.parsers;

import com.autofrog.xbee.api.exceptions.XbeeException;
import com.autofrog.xbee.api.messages.XbeeMessageBase;
import com.autofrog.xbee.api.messages.XbeeModemStatusEnum;
import com.autofrog.xbee.api.messages.XbeeModemStatusMessage;

import java.nio.ByteBuffer;

/**
 * Created by chrisp on 012 3/12/2015.
 */
public class XbeeModemStatusMessageParser extends XbeeMessageParserBase {

    @Override
    protected XbeeMessageBase doParse(ByteBuffer buffer) throws XbeeException {
        byte code = buffer.get();
        XbeeModemStatusEnum status = XbeeModemStatusEnum.lookup((int)(code & 0xFF));
        return new XbeeModemStatusMessage(status);
    }
}
