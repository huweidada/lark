package lark.service.message.provider;

import lark.message.inbound.handler.MessageInboundHandler;
import lark.message.inbound.handler.provider.MessageInboundHandlerProvider;
import lark.service.message.handler.ListUserHandler;

public class ListUserHandlerProvider implements MessageInboundHandlerProvider {
	public ListUserHandlerProvider() {
		this.handler = new ListUserHandler();
	}

	private MessageInboundHandler handler;
	
	public MessageInboundHandler getMessageInboundHandler() {
		return handler;
	}
	
	public String getName(){
		return "listUser";
	}
	
}
