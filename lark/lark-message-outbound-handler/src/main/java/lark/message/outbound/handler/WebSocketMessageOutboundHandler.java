package lark.message.outbound.handler;

import lark.message.outbound.ChannelManager;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.util.CharsetUtil;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebSocketMessageOutboundHandler implements MessageOutboundHandler {
	private static final Logger logger = LoggerFactory.getLogger(WebSocketMessageOutboundHandler.class);

	public long write(String channelId,String message){
		logger.info("WebSocketMessageOutboundHandler write begin");
		
		if(StringUtils.isBlank(channelId)){
			throw new RuntimeException("StringUtils.isBlank(channelId) == true");
		}
		
		if(StringUtils.isBlank(message)){
			logger.warn("StringUtils.isBlank(message) == true，系统不会将空消息发出去");
			return -1L;
		}
		
		Channel channel = ChannelManager.getChannel(channelId);
		if(channel == null){
			logger.error("channel == null,channelId=[{}],message=[{}]",channelId,message);
			return -1L;
		}
		
		ByteBufAllocator alloc = channel.alloc();
		ByteBuf byteBuf = alloc.buffer();
		byteBuf.writeCharSequence(message, CharsetUtil.UTF_8);
		byteBuf.writeCharSequence("\r\n", CharsetUtil.UTF_8);
		
		try{
			channel.writeAndFlush(byteBuf);
		}catch(Exception e){
			logger.error("channel.writeAndFlush fail,channelId=[{}],message=[{}]",channelId,message);
			return -1L;
		}
		return System.currentTimeMillis();
	}
}
