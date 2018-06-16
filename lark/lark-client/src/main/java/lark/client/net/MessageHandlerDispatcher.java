package lark.client.net;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lark.domain.message.MessageRespHeader;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

public class MessageHandlerDispatcher {
	private static final Logger logger = LoggerFactory.getLogger(MessageHandlerDispatcher.class);
	private static Map<String,MessageInboundHandler> messageHandlerMap = new ConcurrentHashMap<String, MessageInboundHandler>();
	@SuppressWarnings("rawtypes")
	public static void dispatch(String message){
		MessageRespHeader messageRespHeader = null;
		try{
			messageRespHeader = JSON.parseObject(message, MessageRespHeader.class);
		}catch(Exception e){
			logger.error("message=[{}],JSON.parseObject(message, messageRespHeader.class) fail",message,e);
        	return;
		}
        
        String type = messageRespHeader.getType();
        if(StringUtils.isBlank(type)){
        	logger.error("StringUtils.isBlank(type)==true");
        	logger.error("message=[{}]",message);
        	return;
        }
		
		MessageInboundHandler messageInboundHandler = messageHandlerMap.get(type);
        if(messageInboundHandler == null){
        	logger.error("messageInboundHandler == null");
        	return;
        }
        try{
        	messageInboundHandler.handle(message);
        }catch(Exception e){
        	logger.error("messageHandler.handle fail,message=[{}]",message,e);
        	return;
        }
	}

	public static void registerMessageHandler(String name,MessageInboundHandler handler){
		messageHandlerMap.put(name,handler);
	}
}
