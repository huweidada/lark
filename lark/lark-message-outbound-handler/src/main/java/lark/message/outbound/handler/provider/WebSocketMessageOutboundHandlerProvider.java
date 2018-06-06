package lark.message.outbound.handler.provider;

import lark.domain.TransportProtocol;
import lark.message.outbound.handler.MessageOutboundHandler;
import lark.message.outbound.handler.WebSocketMessageOutboundHandler;

public class WebSocketMessageOutboundHandlerProvider implements MessageOutboundHandlerProvider {
	
	public WebSocketMessageOutboundHandlerProvider() {
		messageOutboundHandler = new WebSocketMessageOutboundHandler();
	}

	private MessageOutboundHandler messageOutboundHandler;

	public MessageOutboundHandler getMessageOutboundHandler() {
		return messageOutboundHandler;
	}

	public String getName() {
		return TransportProtocol.websocket;
	}

}
