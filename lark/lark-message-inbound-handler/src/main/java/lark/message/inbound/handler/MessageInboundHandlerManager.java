package lark.message.inbound.handler;

import java.util.HashMap;
import java.util.Map;

import lark.message.inbound.handler.provider.MessageInboundHandlerProvider;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageInboundHandlerManager {
	private static final Logger logger = LoggerFactory.getLogger(MessageInboundHandlerManager.class);

	private static Map<String,MessageInboundHandlerProvider> providerMap = new HashMap<String,MessageInboundHandlerProvider>();

	public static int registerProvider(String providerName,MessageInboundHandlerProvider provider){
		if(StringUtils.isBlank(providerName)){
			logger.error("StringUtils.isBlank(providerName) == true");
			return -1;
		}
		if(provider == null){
			logger.error("provider == null");
			logger.error("providerName=[{}]",providerName);
			return -1;
		}
		if(providerMap.containsKey(providerName)){
			logger.warn("providerMap.containsKey(providerName[{}]) == true",providerName);
			return -2;
		}
		providerMap.put(providerName, provider);	
		logger.info("providerName[{}] Register Success",providerName);
		return 0;
	}
	
	public static MessageInboundHandler getMessageInboundHandler(String providerName){
		if(StringUtils.isBlank(providerName)){
			logger.error("StringUtils.isBlank(providerName) == true");
			return null;
		}
		MessageInboundHandlerProvider provider = providerMap.get(providerName);
		if(provider == null){
			logger.warn("provider == null,providerName=[{}]",providerName);
			return null;
		}
		
		MessageInboundHandler messageInboundHandler = provider.getMessageInboundHandler();
		if(messageInboundHandler == null){
			logger.error("messageInboundHandler == null,providerName=[{}]",providerName);
			return null;
		}
		return messageInboundHandler;
	}

}
