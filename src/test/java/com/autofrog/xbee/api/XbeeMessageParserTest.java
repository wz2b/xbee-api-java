package com.autofrog.xbee.api;

import com.autofrog.xbee.api.exceptions.XbeeEmptyMessageException;
import com.autofrog.xbee.api.listeners.XbeeMessageListener;
import com.autofrog.xbee.api.listeners.XbeeParsingExceptionListener;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import com.autofrog.xbee.api.messages.XbeeExplicitRxMessage;
import org.easymock.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(EasyMockRunner.class)
public class XbeeMessageParserTest extends EasyMockSupport {
    private final byte[] msg1 = {
            (byte) 0x7e,
            (byte) 0x00,
            (byte) 0x02,
            (byte) 0x23,
            (byte) 0x11,
            (byte) 0xCB
    };

    /* This example is from the xbee user's manual but with escapes added for AP=2 */
    private final byte[] msg2 = {
            (byte) 0x7E,
            (byte) 0x00,
            (byte) 0x18,
            (byte) 0x91,
            (byte) 0x00,
            (byte) 0x7d, 0x33,
            (byte) 0xa2,
            (byte) 0x00,
            (byte) 0x40,
            (byte) 0x52,
            (byte) 0x2b,
            (byte) 0xaa,
            (byte) 0x7d, 0x5D,
            (byte) 0x84,
            (byte) 0xe0,
            (byte) 0xe0,
            (byte) 0x22,
            (byte) 0x7d, 0x31,
            (byte) 0xc1,
            (byte) 0x05,
            (byte) 0x02,
            (byte) 0x52,
            (byte) 0x78,
            (byte) 0x44,
            (byte) 0x61,
            (byte) 0x74,
            (byte) 0x61,
            (byte) 0x52};

    private final byte[] emptyMessage = new byte[]{
            0x7E, 0x7E
    };

    @TestSubject
    private XbeeMessageParser parser = new XbeeMessageParser();
    @Mock
    private XbeeParsingExceptionListener mockExceptionListener;
    @Mock
    private XbeeMessageListener mockMessageListener;

//    @Test
//    public void testEmptyMessageNotification() throws Exception {
//        parser.addXbeeExceptionListener(mockExceptionListener);
//        parser.addListener(mockMessageListener);
//        mockExceptionListener.xbeeParsingError(eq(parser), anyObject(XbeeEmptyMessageException.class));
//        replayAll();
//        parser.bytesIn(emptyMessage);
//        verifyAll();
//    }

    @Test
    public void testSampleExplicitRx() throws Exception {
        parser.addXbeeExceptionListener(mockExceptionListener);
        parser.addListener(mockMessageListener);
        Capture<XbeeExplicitRxMessage> resultCapture = Capture.newInstance(CaptureType.FIRST);
        mockMessageListener.onXbeeMessage(eq(parser), capture(resultCapture));
        replayAll();
        parser.bytesIn(msg2);
        verifyAll();
        XbeeExplicitRxMessage result = resultCapture.getValue();

        assertEquals((long) 0x2211, result.getClusterId());
        assertEquals((byte) 0xE0, (byte) result.getSourceEndpoint());
        assertEquals((byte) 0xE0, (byte) result.getDestEndpoint());
        assertEquals((short)0xC105, (short)result.getProfileId());
        assertEquals((byte) 0x02, (byte) result.getRxOpts());
        assertEquals(0x0013a20040522baaL, result.getSourceAddress());
    }

}