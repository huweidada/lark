package lark.message.outbound.handler.provider;

import lark.message.outbound.handler.MessageOutboundHandler;

public interface MessageOutboundHandlerProvider {

	MessageOutboundHandler getMessageOutboundHandler();
	
	String getName();
	
}
