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

import java.util.Locale;
import java.util.UUID;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.util.AttributeKey;
import lark.message.outbound.ChannelManager;
import lark.service.user.UserManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Echoes uppercase content of text frames.
 */
public class WebSocketMessageInboundHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketMessageInboundHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {
        // ping and pong frames already handled

        if (frame instanceof TextWebSocketFrame) {
            // Send the uppercase string back.
            String request = ((TextWebSocketFrame) frame).text();
            logger.info("{} received {}", ctx.channel(), request);
            throw new UnsupportedOperationException(request);
            //ctx.channel().writeAndFlush(request);
        } else {
            String message = "unsupported frame type: " + frame.getClass().getName();
            throw new UnsupportedOperationException(message);
        }
    }
    
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
    	logger.info("channelActive");
    }
    
    
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
    	logger.info("userEventTriggered，evt=[{}]",evt);
    	
    	if(evt instanceof WebSocketServerProtocolHandler.HandshakeComplete){
    		String channelId = UUID.randomUUID().toString();
    		setChannelId(ctx.channel(),channelId);
    		ChannelManager.registerChannel(channelId,ctx.channel());
    		logger.info("WebSocketServerProtocolHandler.HandshakeComplete,channelId=[{}]",channelId);
    	}
        //ctx.fireUserEventTriggered(evt);
    }
    
    
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    	logger.info("channelInactive");
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("WebSocketMessageInboundHandler exceptionCaught");
        try{
        	 ctx.close();
        }catch(Exception e){
        	logger.error("ctx.close() fail",e);
        }
    }

    private String getChannelId(Channel channel){
		AttributeKey<String> attrKey = getChannelIdKey();
        String channelId = channel.attr(attrKey).get();
        return channelId;
	}
	
	private void setChannelId(Channel channel,String channelId){
		AttributeKey<String> attrKey = getChannelIdKey();
		channel.attr(attrKey).set(channelId);
	}

	private AttributeKey<String> getChannelIdKey() {
		AttributeKey<String> attrKey = AttributeKey.valueOf("channelId");
		return attrKey;
	}


}
