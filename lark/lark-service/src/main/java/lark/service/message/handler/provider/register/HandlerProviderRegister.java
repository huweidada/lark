package lark.service.message.handler.provider.register;

import lark.message.inbound.handler.MessageInboundHandlerManager;
import lark.message.inbound.handler.provider.MessageInboundHandlerProvider;
import lark.service.message.handler.provider.HeartbeatHandlerProvider;
import lark.service.message.handler.provider.ListContactHandlerProvider;
import lark.service.message.handler.provider.ListUserHandlerProvider;
import lark.service.message.handler.provider.LoginHandlerProvider;
import lark.service.message.handler.provider.SingleChatHandlerProvider;

public class HandlerProviderRegister {
	public static void registerMessageHandlerProvider(){
		MessageInboundHandlerProvider loginHandlerProvider = new LoginHandlerProvider();
		MessageInboundHandlerManager.registerProvider(loginHandlerProvider.getName(), loginHandlerProvider);
		
		MessageInboundHandlerProvider heartbeatHandlerProvider = new HeartbeatHandlerProvider();
		MessageInboundHandlerManager.registerProvider(heartbeatHandlerProvider.getName(), heartbeatHandlerProvider);
		
		MessageInboundHandlerProvider listUserHandlerProvider = new ListUserHandlerProvider();
		MessageInboundHandlerManager.registerProvider(listUserHandlerProvider.getName(), listUserHandlerProvider);
		
		MessageInboundHandlerProvider singleChatHandlerProvider = new SingleChatHandlerProvider();
		MessageInboundHandlerManager.registerProvider(singleChatHandlerProvider.getName(), singleChatHandlerProvider);
		
		MessageInboundHandlerProvider listContactHandlerProvider = new ListContactHandlerProvider();
		MessageInboundHandlerManager.registerProvider(listContactHandlerProvider.getName(), listContactHandlerProvider);
	}
}
