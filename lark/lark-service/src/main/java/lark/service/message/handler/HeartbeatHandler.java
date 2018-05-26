package lark.service.message.handler;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

import lark.domain.AccessPoint;
import lark.domain.Account;
import lark.domain.Server;
import lark.domain.ServerStatusCode;
import lark.domain.message.heartbeat.HeartbeatReq;
import lark.domain.message.heartbeat.HeartbeatResp;
import lark.domain.message.heartbeat.HeartbeatRespBody;
import lark.message.inbound.handler.MessageInboundHandler;
import lark.message.outbound.handler.MessageOutboundHandler;
import lark.message.outbound.handler.MessageOutboundHandlerManager;
import lark.service.user.UserManager;

public class HeartbeatHandler implements MessageInboundHandler{
	private static final Logger logger = LoggerFactory.getLogger(HeartbeatHandler.class);
	private static final String type = "heartbeat";
	
	private HeartbeatReq parse(String message) {
		return JSON.parseObject(message,HeartbeatReq.class);
	}
	

	public void handle(AccessPoint accessPoint, String message) {
		logger.info("accessPoint=[{}],message=[{}]",JSON.toJSONString(accessPoint),message);
		String channelId = accessPoint.getChannelId();
		
		MessageOutboundHandler messageOutboundHandler = MessageOutboundHandlerManager.getMessageOutboundHandler("tcp");
		
		HeartbeatResp  heartbeatResp = new HeartbeatResp();
		heartbeatResp.setServer(Server.serverName);
		heartbeatResp.setType(type);
		heartbeatResp.setTime(System.currentTimeMillis());
		heartbeatResp.setVersion(Server.version);
		
		
		HeartbeatReq heartbeatReq = parse(message);
		if(heartbeatReq == null){
			heartbeatResp.setTransactionId("999");
			heartbeatResp.setStatusCode(ServerStatusCode.parameterError);
			heartbeatResp.setStatusDescription("parameterError");
			messageOutboundHandler.write(channelId, JSON.toJSONString(heartbeatResp));
			return;
		}
		
		if(StringUtils.isNotBlank(heartbeatReq.getTransactionId())){
			heartbeatResp.setTransactionId(heartbeatReq.getTransactionId());
		}else{
			heartbeatResp.setTransactionId("999");
		}
		
		
		if(checkHeartbeatReq(heartbeatReq) < 0){
			heartbeatResp.setStatusCode(ServerStatusCode.parameterError);
			heartbeatResp.setStatusDescription("parameterError");
			messageOutboundHandler.write(channelId, JSON.toJSONString(heartbeatResp));
			return;
		}
		
		int ticketStatus = checkTicket(channelId,heartbeatReq.getBody().getTicket());
		if(ticketStatus < 0){
			//这个时候需要关闭channel，这里先不处理，直接下发一个系统错误
			heartbeatResp.setStatusCode(ServerStatusCode.systemError);
			heartbeatResp.setStatusDescription("systemError");
			messageOutboundHandler.write(channelId, JSON.toJSONString(heartbeatResp));
			return;
		}
		
		//这个时候表明，ticket检查通过，下发一个成功的通知
		HeartbeatRespBody heartbeatRespBody = new HeartbeatRespBody();
		heartbeatRespBody.setBlank("blank");
		heartbeatResp.setBody(heartbeatRespBody);
		heartbeatResp.setStatusCode(ServerStatusCode.success);
		messageOutboundHandler.write(channelId, JSON.toJSONString(heartbeatResp));
		return;
		
	}
	
	
	private int checkHeartbeatReq(HeartbeatReq heartbeatReq){
		return 0;
	}

	private int checkTicket(String channelId,String ticket){
		Account account = UserManager.getAccountByChannelId(channelId);
		if(account == null){
			logger.info("account == null,channelId=[{}],ticket=[{}]",channelId,ticket);
			return -1;
		}
		
		if(!account.getTicket().equals(ticket)){
			logger.error("!account.getTicket().equals(ticket) == true");
			logger.info("channelId=[{}],ticket=[{}],account=[{}]",channelId,ticket,account.toString());
			return -1;
		}
		return 0;
	}
	
	
	
}
