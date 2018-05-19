package lark.service.message.context;

import lark.message.inbound.handler.MessageInboundHandlerManager;
import lark.message.inbound.handler.provider.MessageInboundHandlerProvider;
import lark.service.message.provider.ListUserMessageHandlerProvider;
import lark.service.message.provider.LoginHandlerProvider;
import lark.service.message.provider.SingleChatMessageHandlerProvider;

public class MessageContext {
	public static void registerMessageHandlerProvider(){
		MessageInboundHandlerProvider loginHandlerProvider = new LoginHandlerProvider();
		MessageInboundHandlerManager.registerProvider(loginHandlerProvider.getName(), loginHandlerProvider);
		
		MessageInboundHandlerProvider listUserMessageHandlerProvider = new ListUserMessageHandlerProvider();
		MessageInboundHandlerManager.registerProvider(listUserMessageHandlerProvider.getName(), listUserMessageHandlerProvider);
		
		MessageInboundHandlerProvider singleChatMessageHandlerProvider = new SingleChatMessageHandlerProvider();
		MessageInboundHandlerManager.registerProvider(singleChatMessageHandlerProvider.getName(), singleChatMessageHandlerProvider);
	}
}
