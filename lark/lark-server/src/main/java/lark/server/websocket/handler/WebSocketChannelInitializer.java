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
package lark.server.websocket.handler;

import java.util.concurrent.TimeUnit;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.timeout.IdleStateHandler;

/**
 */
public class WebSocketChannelInitializer extends ChannelInitializer<SocketChannel> {

    private static final String WEBSOCKET_PATH = "/websocket";

    private SslContext sslCtx;

    public WebSocketChannelInitializer(SslContext sslCtx) {
        this.sslCtx = sslCtx;
    }
    
    public WebSocketChannelInitializer() {
    }


    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline channelPipeline = ch.pipeline();
        channelPipeline.addLast(new IdleStateHandler(0, 0, 60, TimeUnit.SECONDS));
        if (sslCtx != null) {
            channelPipeline.addLast(sslCtx.newHandler(ch.alloc()));
        }
        channelPipeline.addLast(new HttpServerCodec());
        channelPipeline.addLast(new HttpObjectAggregator(65536));
        channelPipeline.addLast(new WebSocketServerCompressionHandler());
        channelPipeline.addLast(new WebSocketServerProtocolHandler(WEBSOCKET_PATH, null, true));
        //pipeline.addLast(new WebSocketIndexPageHandler(WEBSOCKET_PATH));
        //pipeline.addLast(new WebSocketMessageInboundHandler0());
        channelPipeline.addLast(WebSocketMessageInboundHandler.getInstance());
        //pipeline.addLast(new WebSocketMessageInboundHandler1());
        channelPipeline.addLast(WebSocketMessageOutboundHandler.getInstance());
    }
}
