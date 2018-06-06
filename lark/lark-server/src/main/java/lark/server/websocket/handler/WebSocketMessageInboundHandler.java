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

import java.net.InetSocketAddress;
import java.util.UUID;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GenericFutureListener;
import lark.domain.AccessPoint;
import lark.domain.TransportProtocol;
import lark.message.inbound.handler.dispatcher.LocalMessageHandlerDispatcher;
import lark.message.inbound.handler.dispatcher.MessageHandlerDispatcher;
import lark.message.outbound.ChannelManager;
import lark.service.user.UserManager;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

/**
 * Echoes uppercase content of text frames.
 */
@Sharable
public class WebSocketMessageInboundHandler extends SimpleChannelInboundHandler<WebSocketFrame> {    
	private static final Logger logger = LoggerFactory.getLogger(WebSocketMessageInboundHandler.class);
	
	private static WebSocketMessageInboundHandler instance;
	private MessageHandlerDispatcher dispatcher; 

	public static WebSocketMessageInboundHandler getInstance(){
		if(instance == null){
			instance = new WebSocketMessageInboundHandler();
		}
		return instance;
	}
	private WebSocketMessageInboundHandler() {
		super();
		dispatcher = LocalMessageHandlerDispatcher.getInstance();
		logger.info("WebSocketMessageInboundHandler created");		
	}
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {
        // ping and pong frames already handled
        if (frame instanceof TextWebSocketFrame) {
            String message = ((TextWebSocketFrame) frame).text();
            logger.info("channelRead message=[{}]",message);
            if(StringUtils.isBlank(message)){
            	logger.info("StringUtils.isBlank(message) == true");
            	return;
            }
            try{
            	AccessPoint accessPoint = getAccessPointFromChannel(ctx.channel());
            	dispatcher.dispatch(accessPoint,message);
            }catch(Exception e){
            	logger.error("messageHandler.handle fail,message=[{}]",message,e);
            }
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
    		Channel channel = ctx.channel();

    		AccessPoint accessPoint = new AccessPoint();
    		accessPoint.setChannelId(channelId);
    		accessPoint.setClientIp(((InetSocketAddress)channel.remoteAddress()).getAddress().getHostAddress());
    		accessPoint.setClientPort(((InetSocketAddress)channel.remoteAddress()).getPort());
    		accessPoint.setServerIp(((InetSocketAddress)channel.localAddress()).getAddress().getHostAddress());
    		accessPoint.setServerPort(((InetSocketAddress)channel.localAddress()).getPort());
    		accessPoint.setTransportProtocol(TransportProtocol.websocket);
    		
    		saveAccessPointToChannel(channel, accessPoint);
    		ChannelManager.registerChannel(channelId,ctx.channel());
    		
    		ctx.channel().closeFuture().addListener(new GenericFutureListener<ChannelFuture>() {
    			@Override
    			public void operationComplete(ChannelFuture future)
    					throws Exception {
    				logger.info("closeFuture operationComplete");
    				//future.channel().close();
    			}
    		});
    		logger.info("channelActive,accessPoint=[{}]",JSON.toJSONString(accessPoint));
    		logger.info("WebSocketServerProtocolHandler.HandshakeComplete,channelId=[{}]",channelId);
    	}else if (evt instanceof IdleStateEvent){
			logger.info("IdleStateEvent occured");
			String channelId = getAccessPointFromChannel(ctx.channel()).getChannelId();
			ChannelManager.unregisterChannel(channelId);
			
			logger.info("ChannelManager.unregisterChannel,channelId=[{}]",channelId);
			
			UserManager.unregisterAccountByChannelId(channelId);
			
			try{
	        	 ctx.close();
	        }catch(Exception e){
	        	logger.error("ctx.close() fail",e);
	        }
		}
        //ctx.fireUserEventTriggered(evt);
    }
    
    
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    	logger.info("channelInactive");
    	String channelId = getAccessPointFromChannel(ctx.channel()).getChannelId();
		logger.info("channelInactive channelId=[{}]",channelId);
		
		ChannelManager.unregisterChannel(channelId);
        
	    UserManager.unregisterAccountByChannelId(channelId);
	    
	    try{
       	 	ctx.close();
       }catch(Exception e){
       		logger.error("ctx.close() fail",e);
       }
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

    private AccessPoint getAccessPointFromChannel(Channel channel){
		AttributeKey<AccessPoint> attrKey = getAccessPointKey();
		return channel.attr(attrKey).get();
	}
	
	private void saveAccessPointToChannel(Channel channel,AccessPoint accessPoint){
		AttributeKey<AccessPoint> attrKey = getAccessPointKey();
		channel.attr(attrKey).set(accessPoint);
	}
	
	private AttributeKey<AccessPoint> getAccessPointKey() {
		AttributeKey<AccessPoint> attrKey = AttributeKey.valueOf("accessPoint");
		return attrKey;
	}


}
