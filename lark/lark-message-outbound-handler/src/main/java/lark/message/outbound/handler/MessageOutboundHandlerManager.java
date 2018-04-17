package lark.message.outbound.handler;

import java.util.HashMap;
import java.util.Map;

import lark.message.outbound.handler.provider.MessageOutboundHandlerProvider;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageOutboundHandlerManager {
	private static final Logger logger = LoggerFactory.getLogger(MessageOutboundHandlerManager.class);

	private static Map<String,MessageOutboundHandlerProvider> providerMap = new HashMap<String,MessageOutboundHandlerProvider>();

	public static int registerProvider(String providerName,MessageOutboundHandlerProvider provider){
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
	
	public static MessageOutboundHandler getMessageOutboundHandler(String providerName){
		if(StringUtils.isBlank(providerName)){
			logger.error("StringUtils.isBlank(providerName) == true");
			return null;
		}
		MessageOutboundHandlerProvider provider = providerMap.get(providerName);
		if(provider == null){
			logger.warn("provider == null,providerName=[{}]",providerName);
			return null;
		}
		
		MessageOutboundHandler messageOutboundHandler = provider.getMessageOutboundHandler();
		if(messageOutboundHandler == null){
			logger.error("messageOutboundHandler == null,providerName=[{}]",providerName);
			return null;
		}
		return messageOutboundHandler;
	}

}
