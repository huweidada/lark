package lark.service.message.handler.provider;

import lark.message.inbound.handler.MessageInboundHandler;
import lark.message.inbound.handler.provider.MessageInboundHandlerProvider;
import lark.service.message.handler.HeartbeatHandler;

public class HeartbeatHandlerProvider implements MessageInboundHandlerProvider {
	public HeartbeatHandlerProvider() {
		this.handler = new HeartbeatHandler();
	}

	private MessageInboundHandler handler;
	
	
	public MessageInboundHandler getMessageInboundHandler() {
		return handler;
	}
	
	
	public String getName(){
		return "heartbeat";
	}
	
}
