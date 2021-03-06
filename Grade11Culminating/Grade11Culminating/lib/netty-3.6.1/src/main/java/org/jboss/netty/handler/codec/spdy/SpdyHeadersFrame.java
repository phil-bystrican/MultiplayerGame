/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package org.jboss.netty.handler.codec.spdy;

/**
 * A SPDY Protocol HEADERS Control Frame
 */
public interface SpdyHeadersFrame extends SpdyHeaderBlock {

    /**
     * @deprecated Use {@link #getStreamId()} instead.
     */
    @Deprecated
    int getStreamID();

    /**
     * Returns the Stream-ID of this frame.
     */
    int getStreamId();

    /**
     * @deprecated Use {@link #setStreamId(int)} instead.
     */
    @Deprecated
    void setStreamID(int streamId);

    /**
     * Sets the Stream-ID of this frame.  The Stream-ID must be positive.
     */
    void setStreamId(int streamId);

    /**
     * Returns {@code true} if this frame is the last frame to be transmitted
     * on the stream.
     */
    boolean isLast();

    /**
     * Sets if this frame is the last frame to be transmitted on the stream.
     */
    void setLast(boolean last);
}
