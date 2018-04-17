package lark.service.message.provider;

import lark.message.inbound.handler.MessageInboundHandler;
import lark.message.inbound.handler.provider.MessageInboundHandlerProvider;
import lark.service.message.handler.SingleChatMessageHandler;


public class SingleChatMessageHandlerProvider implements MessageInboundHandlerProvider {
	public SingleChatMessageHandlerProvider() {
		this.handler = new SingleChatMessageHandler();
	}
	private MessageInboundHandler handler;
	
	public MessageInboundHandler getMessageInboundHandler() {
		return handler;
	}
	
	public String getName(){
		return "singleChat";
	}
	
}
