package lark.service.message.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

import lark.domain.message.chat.SingleChatMessage;
import lark.message.inbound.handler.MessageInboundHandler;

public class SingleChatMessageHandler implements MessageInboundHandler{
	private static final Logger logger = LoggerFactory.getLogger(SingleChatMessageHandler.class);

	/*@Override
	public void handle(Channel channel, SingleChatMessage message) {
		logger.info("message.getBody().getFromPin()=[{}]",message.getBody().getFromPin());
		logger.info("message.getBody().getToPin()=[{}]",message.getBody().getToPin());
		
		
		String toChannelId = UserManager.getChannelId(message.getBody().getToPin());
		Channel toChannel = ChannelManager.getChannel(toChannelId);
		
		toChannel.writeAndFlush(Unpooled.copiedBuffer(message.getBody().getContent(),CharsetUtil.UTF_8));
	}*/

	public SingleChatMessage parse(String message) {
		return JSON.parseObject(message,SingleChatMessage.class);
	}

	public void handle(String channelId, String message) {
		SingleChatMessage singleChatMessage = parse(message);
		/*logger.info("message.getBody().getFromPin()=[{}]",singleChatMessage.getBody().getFromPin());
		logger.info("message.getBody().getToPin()=[{}]",singleChatMessage.getBody().getToPin());*/
		
	}

}
