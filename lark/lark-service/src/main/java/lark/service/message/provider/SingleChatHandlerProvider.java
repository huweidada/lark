package lark.service.message.provider;

import lark.message.inbound.handler.MessageInboundHandler;
import lark.message.inbound.handler.provider.MessageInboundHandlerProvider;
import lark.service.message.handler.SingleChatHandler;


public class SingleChatHandlerProvider implements MessageInboundHandlerProvider {
	public SingleChatHandlerProvider() {
		this.handler = new SingleChatHandler();
	}
	private MessageInboundHandler handler;
	
	public MessageInboundHandler getMessageInboundHandler() {
		return handler;
	}
	
	public String getName(){
		return "singleChat";
	}
	
}
