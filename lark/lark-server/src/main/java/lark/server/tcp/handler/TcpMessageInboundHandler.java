package lark.server.tcp.handler;

import java.net.InetSocketAddress;
import java.util.UUID;

import lark.domain.AccessPoint;
import lark.message.inbound.handler.dispatcher.LocalMessageHandlerDispatcher;
import lark.message.inbound.handler.dispatcher.MessageHandlerDispatcher;
import lark.message.outbound.ChannelManager;


import lark.service.user.UserManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.handler.timeout.IdleStateEvent;


@Sharable
public class TcpMessageInboundHandler extends ChannelInboundHandlerAdapter{
	private static final Logger logger = LoggerFactory.getLogger(TcpMessageInboundHandler.class);
	
	private static TcpMessageInboundHandler instance;
	private MessageHandlerDispatcher dispatcher; 

	public static TcpMessageInboundHandler getInstance(){
		if(instance == null){
			instance = new TcpMessageInboundHandler();
		}
		return instance;
	}
	private TcpMessageInboundHandler() {
		super();
		dispatcher = new LocalMessageHandlerDispatcher();
		logger.info("MessageRouteHandler created");		
	}
	
	@Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
		String channelId = UUID.randomUUID().toString();
		Channel channel = ctx.channel();

		AccessPoint accessPoint = new AccessPoint();
		accessPoint.setChannelId(channelId);
		accessPoint.setClientIp(((InetSocketAddress)channel.remoteAddress()).getAddress().getHostAddress());
		accessPoint.setClientPort(((InetSocketAddress)channel.remoteAddress()).getPort());
		accessPoint.setServerIp(((InetSocketAddress)channel.localAddress()).getAddress().getHostAddress());
		accessPoint.setServerPort(((InetSocketAddress)channel.localAddress()).getPort());
		
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
    }
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		String channelId = getAccessPointFromChannel(ctx.channel()).getChannelId();
		logger.info("channelInactive channelId=[{}]",channelId);
		
		ChannelManager.unregisterChannel(channelId);
        
	    UserManager.unregisterAccountByChannelId(channelId);
	}
	
	@Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg == null) return;
        ByteBuf byteBuf = (ByteBuf)msg;
        String message = byteBuf.toString(CharsetUtil.UTF_8);
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
        
    }
	
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx,Object evt) throws Exception {
		if (evt instanceof IdleStateEvent){
			logger.info("IdleStateEvent occured");
			String channelId = getAccessPointFromChannel(ctx.channel()).getChannelId();
			ChannelManager.unregisterChannel(channelId);
			
			logger.info("ChannelManager.unregisterChannel,channelId=[{}]",channelId);
			
			UserManager.unregisterAccountByChannelId(channelId);
			
			ctx.close();
		}
	}
	
	
	@Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("exceptionCaught",cause);
        String channelId = getAccessPointFromChannel(ctx.channel()).getChannelId();
        
        logger.info("channelId=[{}]",channelId);
        ChannelManager.unregisterChannel(channelId);
        
        UserManager.unregisterAccountByChannelId(channelId);
        
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
	/*
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
	}*/
	
}
