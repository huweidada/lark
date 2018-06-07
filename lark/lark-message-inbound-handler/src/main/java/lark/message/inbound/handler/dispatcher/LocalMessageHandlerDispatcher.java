package lark.message.inbound.handler.dispatcher;

import lark.domain.AccessPoint;
import lark.domain.message.MessageReqHeader;
import lark.message.inbound.handler.MessageInboundHandler;
import lark.message.inbound.handler.MessageInboundHandlerManager;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;


public class LocalMessageHandlerDispatcher implements MessageHandlerDispatcher {
	private static final Logger logger = LoggerFactory.getLogger(LocalMessageHandlerDispatcher.class);
	
	private static MessageHandlerDispatcher instance;
	public static MessageHandlerDispatcher getInstance(){
		if(instance == null){
			instance = new LocalMessageHandlerDispatcher();
		}
		return instance;
	}
	
	
	private LocalMessageHandlerDispatcher() {
	}
	
	@SuppressWarnings("rawtypes")
	public void dispatch(AccessPoint accessPoint, String message) {
		MessageReqHeader messageReqHeader = null;
		try{
			messageReqHeader = JSON.parseObject(message, MessageReqHeader.class);
		}catch(Exception e){
			logger.error("message=[{}],JSON.parseObject(message, MessageReqHeader.class) fail",message,e);
        	return;
		}
        
        String type = messageReqHeader.getType();
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
        	messageInboundHandler.handle(accessPoint,message);
        }catch(Exception e){
        	logger.error("messageHandler.handle fail,message=[{}]",message,e);
        	return;
        }
	}

}
