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

import lark.message.outbound.handler.MessageOutboundHandlerManager;
import lark.message.outbound.handler.provider.MessageOutboundHandlerProvider;
import lark.message.outbound.handler.provider.WebSocketMessageOutboundHandlerProvider;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Echoes uppercase content of text frames.
 */
@Sharable
public class WebSocketMessageOutboundHandler extends ChannelOutboundHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketMessageOutboundHandler.class);
    
    private static WebSocketMessageOutboundHandler instance;

	public static WebSocketMessageOutboundHandler getInstance(){
		if(instance == null){
			instance = new WebSocketMessageOutboundHandler();
		}
		return instance;
	}
	private WebSocketMessageOutboundHandler() {
		MessageOutboundHandlerProvider webSocketMessageHandlerProvider = new WebSocketMessageOutboundHandlerProvider();
		MessageOutboundHandlerManager.registerProvider(webSocketMessageHandlerProvider.getName(), webSocketMessageHandlerProvider);
		logger.info("WebSocketMessageOutboundHandler created");
	}

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		logger.info("WebSocketMessageOutboundHandler write begin");
		if(msg instanceof ByteBuf){
			logger.info("MessageOutboundHandler write begin,msg instanceof ByteBuf");
			TextWebSocketFrame frame = new TextWebSocketFrame((ByteBuf) msg);
			//ctx.channel().writeAndFlush(frame);
			ctx.write(frame);
		}else{
			logger.info("MessageOutboundHandler write begin,msg.type=[{}],msg=[{}]",msg.getClass().getName(),msg);
			ctx.write(msg, promise);
		}
    }
    
    @Override
    public void flush(ChannelHandlerContext ctx) throws Exception {
		logger.info("WebSocketMessageOutboundHandler flush begin");
        ctx.flush();
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.info("WebSocketMessageOutboundHandler exceptionCaught");
		ctx.fireExceptionCaught(cause);
    }
}
