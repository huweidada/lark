package lark.service.message.handler;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

import lark.domain.Server;
import lark.domain.ServerStatusCode;
import lark.domain.message.chat.SingleChatReq;
import lark.domain.message.chat.SingleChatResp;
import lark.message.inbound.handler.MessageInboundHandler;
import lark.message.outbound.handler.MessageOutboundHandler;
import lark.message.outbound.handler.MessageOutboundHandlerManager;

public class SingleChatHandler implements MessageInboundHandler{
	private static final Logger logger = LoggerFactory.getLogger(SingleChatHandler.class);
	private static final String type = "singleChat";
	
	
	public SingleChatReq parse(String message) {
		return JSON.parseObject(message,SingleChatReq.class);
	}

	public void handle(String channelId, String message) {
		logger.info("channelId=[{}],message=[{}]",channelId,message);
		
		MessageOutboundHandler messageOutboundHandler = MessageOutboundHandlerManager.getMessageOutboundHandler("tcp");
		
		SingleChatResp  singleChatResp = new SingleChatResp();
		singleChatResp.setServer(Server.serverName);
		singleChatResp.setType(type);
		singleChatResp.setTime(System.currentTimeMillis());
		singleChatResp.setVersion(Server.version);
		
		SingleChatReq singleChatReq = parse(message);
		if(singleChatReq == null){
			singleChatResp.setTransactionId("999");
			singleChatResp.setStatusCode(ServerStatusCode.parameterError);
			singleChatResp.setStatusDescription("parameterError");
			messageOutboundHandler.write(channelId, JSON.toJSONString(singleChatResp));
			return;
		}
		if(StringUtils.isNotBlank(singleChatReq.getTransactionId())){
			singleChatResp.setTransactionId(singleChatReq.getTransactionId());
		}else{
			singleChatResp.setTransactionId("999");
		}
		
		if(checkSingleChatReq(singleChatReq) < 0){
			singleChatResp.setStatusCode(ServerStatusCode.parameterError);
			singleChatResp.setStatusDescription("parameterError");
			messageOutboundHandler.write(channelId, JSON.toJSONString(singleChatResp));
			return;
		}

		
		
	}

	
	private int checkSingleChatReq(SingleChatReq singleChatReq){
		return 0;
	}
}
