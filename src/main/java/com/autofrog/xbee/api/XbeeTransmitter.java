package com.autofrog.xbee.api;

import com.autofrog.xbee.api.exceptions.XbeeEmptyMessageException;
import com.autofrog.xbee.api.exceptions.XbeeException;
import com.autofrog.xbee.api.exceptions.XbeeTransmitException;
import com.autofrog.xbee.api.messages.XbeeTransmitMessageBase;
import com.autofrog.xbee.api.protocol.XbeeApiConstants;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Xbee serial port transmit adapter.
 * <p/>
 * This class holds an output stream.  The only synchronization that it performs is that it only
 * uses a single thread to write.  If the user's application sends to the port outside of this
 * instance then they should make their own OutputStream that provides proper synchronization.
 * This probably won't be the case most of the time.
 *
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
public class XbeeTransmitter {
    private final OutputStream output;
    private final ScheduledThreadPoolExecutor threadPool;
    private boolean flush = true;

    public XbeeTransmitter(OutputStream output) {
        this.output = output;
        this.threadPool = new ScheduledThreadPoolExecutor(1);
        threadPool.setMaximumPoolSize(1);
    }

    public void shutdown() throws InterruptedException {
        shutdown(30, TimeUnit.SECONDS);
    }


    public void shutdown(long time, TimeUnit units) throws InterruptedException {
        threadPool.shutdownNow();
        threadPool.awaitTermination(time, units);
    }

    public SendResult send(final XbeeTransmitMessageBase msg) {
        try {
            return threadPool.submit(new SendTask(msg)).get();
        } catch (InterruptedException e) {
            if (threadPool.isShutdown()) {
                return new SendResult(0, new XbeeTransmitException("Thread pool is shut down", e));
            } else if (threadPool.isTerminating()) {
                return new SendResult(0, new XbeeTransmitException("Thread pool is terminating", e));
            } else if (threadPool.isTerminated()) {
                return new SendResult(0, new XbeeTransmitException("TThread pool is terminated", e));
            } else {
                return new SendResult(0, new XbeeTransmitException("Task interrupted", e));
            }
        } catch (ExecutionException e) {
            return new SendResult(0, new XbeeTransmitException("Task could not be executed", e));
        }
    }

    public SendResult sendNoWait(final XbeeTransmitMessageBase msg) {
        SendTask task = new SendTask(msg);
        threadPool.submit(task);
        return new SendResult(task.getMessageLength(), null);
    }

    public static final class SendResult {
        final XbeeException exception;
        final int count;

        public SendResult(int count, XbeeException exception) {
            this.count = count;
            this.exception = exception;
        }

        public int getCount() {
            return count;
        }

        public Exception getException() {
            return exception;
        }
    }

    private class SendTask implements Callable<SendResult> {
        private final byte[] bytes;

        public SendTask(XbeeTransmitMessageBase message) {
            byte[] messageBytes = message.getBytes();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.write((byte) 0x7E);

            putShortWithEscapes(baos, (short) messageBytes.length);
            byte checksum = 0;

            for(byte b : messageBytes) {
                checksum += (putWithEscapes(baos, b)) & 0xFF;
            }
            putWithEscapes(baos, (byte)(0xFF - checksum));
            bytes = baos.toByteArray();
        }


        /**
         * Helper method to write bytes escapes
         *
         * @param output
         * @param bytes
         */
        private byte putWithEscapes(ByteArrayOutputStream output, byte ... bytes) {
            byte checksum = 0;

            for(byte b : bytes) {
                switch (b) {
                    case XbeeApiConstants.API_ESCAPE:
                    case XbeeApiConstants.API_FLAG:
                    case XbeeApiConstants.API_XOFF:
                    case XbeeApiConstants.API_XON:
                        output.write(XbeeApiConstants.API_ESCAPE);
                        output.write(b & 0x20);
                        break;
                    default:
                        output.write(b);
                }
                checksum += b;
            }

            return checksum;
        }

        /**
         * Helper method to write escaped shorts
         *
         * @param output
         * @param value
         */
        private byte putShortWithEscapes(ByteArrayOutputStream output, short value) {
            return putWithEscapes(output, ByteBuffer.allocate(2)
                    .order(ByteOrder.BIG_ENDIAN)
                    .putShort(value).array());
        }

        /**
         * Helper method to write escaped ints
         *
         * @param output
         * @param value
         */
        private byte putIntWithEscapes(ByteArrayOutputStream output, int value) {
            return putWithEscapes(output, ByteBuffer.allocate(2)
                    .order(ByteOrder.BIG_ENDIAN)
                    .putInt(value).array());
        }

        @Override
        public SendResult call() throws Exception {
            if (bytes == null || bytes.length == 0) {
                return new SendResult(0, new XbeeEmptyMessageException());
            } else {
                try {
                    output.write(bytes);
                } catch (Exception e) {
                    return new SendResult(0, new XbeeTransmitException("Write failed", e));
                }

                if (flush) {
                    try {
                        output.flush();
                    } catch (IOException e) {
                        return new SendResult(0, new XbeeTransmitException("flush() failed", e));
                    }
                }

                return new SendResult(bytes.length, null);
            }
        }

        public byte[] getBytes() {
            return bytes;
        }

        public int getMessageLength() {
            return bytes.length;
        }
    }


}