package lark.message.inbound.handler.dispatcher;

import lark.message.inbound.handler.MessageInboundHandler;
import lark.message.inbound.handler.MessageInboundHandlerManager;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;


public class LocalMessageHandlerDispatcher implements MessageHandlerDispatcher {
	private static final Logger logger = LoggerFactory.getLogger(LocalMessageHandlerDispatcher.class);
	
	public LocalMessageHandlerDispatcher() {
		/*MessageInboundHandlerProvider loginMessageHandlerProvider = new LoginMessageHandlerProvider();
		MessageInboundHandlerManager.registerProvider(loginMessageHandlerProvider.getName(), loginMessageHandlerProvider);
		
		MessageInboundHandlerProvider listUserMessageHandlerProvider = new ListUserMessageHandlerProvider();
		MessageInboundHandlerManager.registerProvider(listUserMessageHandlerProvider.getName(), listUserMessageHandlerProvider);
		
		MessageInboundHandlerProvider singleChatMessageHandlerProvider = new SingleChatMessageHandlerProvider();
		MessageInboundHandlerManager.registerProvider(singleChatMessageHandlerProvider.getName(), singleChatMessageHandlerProvider);*/
	}
	
	public void dispatch(String channelId, String message) {
		JSONObject jsonMessage = null;
        try{
        	jsonMessage = JSON.parseObject(message);
        }catch(Exception e){
        	logger.error("message=[{}],JSON.parseObject fail",message,e);
        	return;
        }
        
        String type = jsonMessage.getString("type");
        if(StringUtils.isBlank(type)){
        	logger.error("StringUtils.isBlank(type)==true");
        	logger.error("message=[{}]",message);
        	return;
        }
		
		MessageInboundHandler messageInboundHandler = MessageInboundHandlerManager.getMessageInboundHandler(type);
        if(messageInboundHandler == null){
        	logger.error("messageInboundHandler == null");
        	return;
        }
        try{
        	messageInboundHandler.handle(channelId,message);
        }catch(Exception e){
        	logger.error("messageHandler.handle fail,message=[{}]",message,e);
        }
	}

}
