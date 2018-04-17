package lark.message.inbound.handler.provider;

import lark.message.inbound.handler.MessageInboundHandler;

public interface MessageInboundHandlerProvider {

	MessageInboundHandler getMessageInboundHandler();
	
	String getName();
	
}
