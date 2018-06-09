package lark.service.message.handler.provider;

import lark.message.inbound.handler.MessageInboundHandler;
import lark.message.inbound.handler.provider.MessageInboundHandlerProvider;
import lark.service.message.handler.ListContactHandler;

public class ListContactHandlerProvider implements MessageInboundHandlerProvider {
	public ListContactHandlerProvider() {
		this.handler = new ListContactHandler();
	}

	private MessageInboundHandler handler;
	
	public MessageInboundHandler getMessageInboundHandler() {
		return handler;
	}
	
	public String getName(){
		return "listContact";
	}
	
}
