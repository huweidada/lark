package lark.service.message.provider;

import lark.message.inbound.handler.MessageInboundHandler;
import lark.message.inbound.handler.provider.MessageInboundHandlerProvider;
import lark.service.message.handler.ListUserMessageHandler;

public class ListUserMessageHandlerProvider implements MessageInboundHandlerProvider {
	public ListUserMessageHandlerProvider() {
		this.handler = new ListUserMessageHandler();
	}

	private MessageInboundHandler handler;
	
	public MessageInboundHandler getMessageInboundHandler() {
		return handler;
	}
	
	public String getName(){
		return "listUser";
	}
	
}
