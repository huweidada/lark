package lark.service.message.context;

import lark.message.inbound.handler.MessageInboundHandlerManager;
import lark.message.inbound.handler.provider.MessageInboundHandlerProvider;
import lark.service.message.provider.ListUserMessageHandlerProvider;
import lark.service.message.provider.LoginMessageHandlerProvider;
import lark.service.message.provider.SingleChatMessageHandlerProvider;

public class MessageContext {
	public static void registerMessageHandlerProvider(){
		MessageInboundHandlerProvider loginMessageHandlerProvider = new LoginMessageHandlerProvider();
		MessageInboundHandlerManager.registerProvider(loginMessageHandlerProvider.getName(), loginMessageHandlerProvider);
		
		MessageInboundHandlerProvider listUserMessageHandlerProvider = new ListUserMessageHandlerProvider();
		MessageInboundHandlerManager.registerProvider(listUserMessageHandlerProvider.getName(), listUserMessageHandlerProvider);
		
		MessageInboundHandlerProvider singleChatMessageHandlerProvider = new SingleChatMessageHandlerProvider();
		MessageInboundHandlerManager.registerProvider(singleChatMessageHandlerProvider.getName(), singleChatMessageHandlerProvider);
	}
}
