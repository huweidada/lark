package lark.message.outbound.handler.provider;

import lark.message.outbound.handler.MessageOutboundHandler;
import lark.message.outbound.handler.TcpMessageOutboundHandler;

public class TcpMessageOutboundHandlerProvider implements MessageOutboundHandlerProvider {
	
	public TcpMessageOutboundHandlerProvider() {
		messageOutboundHandler = new TcpMessageOutboundHandler();
	}

	private MessageOutboundHandler messageOutboundHandler;

	public MessageOutboundHandler getMessageOutboundHandler() {
		return messageOutboundHandler;
	}

	public String getName() {
		return "tcp";
	}

}
