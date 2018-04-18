package lark.service.message.handler;

import java.util.Map;

import lark.domain.User;
import lark.domain.message.ListUserMessage;
import lark.message.inbound.handler.MessageInboundHandler;
import lark.message.outbound.handler.MessageOutboundHandler;
import lark.message.outbound.handler.MessageOutboundHandlerManager;
import lark.service.user.UserManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

public class ListUserMessageHandler implements MessageInboundHandler{
	private static final Logger logger = LoggerFactory.getLogger(ListUserMessageHandler.class);

	private ListUserMessage parse(String message) {
		return JSON.parseObject(message,ListUserMessage.class);
	}

	public void handle(String channelId, String message) {
		ListUserMessage listUserMessage = parse(message);
		logger.info("userId=[{}]",listUserMessage.getBody().getUserId());
		
		Map<String,User> users = UserManager.listUser();
		
		MessageOutboundHandler messageOutboundHandler = MessageOutboundHandlerManager.getMessageOutboundHandler("tcp");
		messageOutboundHandler.write(channelId, JSON.toJSONString(users));
	}

}
