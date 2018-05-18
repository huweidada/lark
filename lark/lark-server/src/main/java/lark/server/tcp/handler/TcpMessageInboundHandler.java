package lark.server.tcp.handler;

import java.util.UUID;

import lark.message.inbound.handler.dispatcher.LocalMessageHandlerDispatcher;
import lark.message.inbound.handler.dispatcher.MessageHandlerDispatcher;
import lark.message.outbound.ChannelManager;


import lark.service.user.UserManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang3.StringUtils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;
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
		setChannelId(ctx.channel(),channelId);
		ChannelManager.registerChannel(channelId,ctx.channel());
		
		//ctx.channel().closeFuture().addListener();
		
		logger.info("channelActive,channelId=[{}]",channelId);
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
        	String channelId = getChannelId(ctx.channel());
        	dispatcher.dispatch(channelId,message);
        }catch(Exception e){
        	logger.error("messageHandler.handle fail,message=[{}]",message,e);
        }
        
    }
	
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx,Object evt) throws Exception {
		if (evt instanceof IdleStateEvent){
			logger.info("IdleStateEvent occured");
			String channelId = getChannelId(ctx.channel());
			ChannelManager.unregisterChannel(channelId);
			
			logger.info("ChannelManager.unregisterChannel,channelId=[{}]",channelId);
			
			UserManager.unregisterAccountByChannelId(channelId);
			
			ctx.close();
		}
	}
	
	
	@Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("exceptionCaught",cause);
        String channelId = getChannelId(ctx.channel());
        ChannelManager.unregisterChannel(channelId);
        
        UserManager.unregisterAccountByChannelId(channelId);
        
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
