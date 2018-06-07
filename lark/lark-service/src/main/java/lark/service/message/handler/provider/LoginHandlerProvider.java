package lark.service.message.handler.provider;

import lark.message.inbound.handler.MessageInboundHandler;
import lark.message.inbound.handler.provider.MessageInboundHandlerProvider;
import lark.service.message.handler.LoginHandler;

public class LoginHandlerProvider implements MessageInboundHandlerProvider {
	public LoginHandlerProvider() {
		this.handler = new LoginHandler();
	}

	private MessageInboundHandler handler;
	
	
	public MessageInboundHandler getMessageInboundHandler() {
		return handler;
	}
	
	
	public String getName(){
		return "login";
	}
	
}
