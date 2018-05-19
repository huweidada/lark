package lark.service.message.context;

import lark.message.inbound.handler.MessageInboundHandlerManager;
import lark.message.inbound.handler.provider.MessageInboundHandlerProvider;
import lark.service.message.provider.HeartbeatHandlerProvider;
import lark.service.message.provider.ListUserHandlerProvider;
import lark.service.message.provider.LoginHandlerProvider;
import lark.service.message.provider.SingleChatMessageHandlerProvider;

public class MessageContext {
	public static void registerMessageHandlerProvider(){
		MessageInboundHandlerProvider loginHandlerProvider = new LoginHandlerProvider();
		MessageInboundHandlerManager.registerProvider(loginHandlerProvider.getName(), loginHandlerProvider);
		
		MessageInboundHandlerProvider heartbeatHandlerProvider = new HeartbeatHandlerProvider();
		MessageInboundHandlerManager.registerProvider(heartbeatHandlerProvider.getName(), heartbeatHandlerProvider);
		
		MessageInboundHandlerProvider listUserHandlerProvider = new ListUserHandlerProvider();
		MessageInboundHandlerManager.registerProvider(listUserHandlerProvider.getName(), listUserHandlerProvider);
		
		MessageInboundHandlerProvider singleChatMessageHandlerProvider = new SingleChatMessageHandlerProvider();
		MessageInboundHandlerManager.registerProvider(singleChatMessageHandlerProvider.getName(), singleChatMessageHandlerProvider);
	}
}
