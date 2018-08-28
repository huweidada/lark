package lark.service.message.handler;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

import lark.domain.AccessPoint;
import lark.domain.Server;
import lark.domain.ServerStatusCode;
import lark.domain.message.heartbeat.HeartbeatReq;
import lark.domain.message.heartbeat.HeartbeatResp;
import lark.domain.message.heartbeat.HeartbeatRespBody;
import lark.message.inbound.handler.MessageInboundHandler;
import lark.message.outbound.handler.MessageOutboundHandler;
import lark.message.outbound.handler.MessageOutboundHandlerManager;
import lark.service.TicketService;
import lark.service.impl.TicketServiceImpl;

public class HeartbeatHandler implements MessageInboundHandler{
	private static final Logger logger = LoggerFactory.getLogger(HeartbeatHandler.class);
	private static final String type = "heartbeat";
	
	private TicketService ticketService;
	
	public HeartbeatHandler() {
		super();
		ticketService = TicketServiceImpl.getInstance();
	}
	
	private HeartbeatReq parse(String message) {
		return JSON.parseObject(message,HeartbeatReq.class);
	}
	

	public void handle(AccessPoint accessPoint, String message) {
		logger.info("accessPoint=[{}],message=[{}]",JSON.toJSONString(accessPoint),message);
		String channelId = accessPoint.getChannelId();
		
		MessageOutboundHandler messageOutboundHandler = MessageOutboundHandlerManager.getMessageOutboundHandler(accessPoint.getTransportProtocol());
		
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
		
		String ticket = heartbeatReq.getBody().getTicket();
		//这种情况是允许的，不过需要提示客户端，用户未登录
		if(StringUtils.isBlank(ticket)){
			heartbeatResp.setStatusCode(ServerStatusCode.notLogin);
			heartbeatResp.setStatusDescription("notLogin");
			messageOutboundHandler.write(channelId, JSON.toJSONString(heartbeatResp));
			return;
		}
		
		int ticketStatus = checkTicket(heartbeatReq.getBody().getTicket());
		logger.info("ticketStatus=[{}]",ticketStatus);
		if(ticketStatus < 0){
			//这个时候需要关闭channel，这里先不处理，直接下发一个系统错误
			heartbeatResp.setStatusCode(ServerStatusCode.systemError);
			heartbeatResp.setStatusDescription("systemError");
			messageOutboundHandler.write(channelId, JSON.toJSONString(heartbeatResp));
			return;
		}
		
		if(ticketStatus == 1){
			//这个时候表示，用户的ticket已经过期
			heartbeatResp.setStatusCode(ServerStatusCode.notLogin);
			heartbeatResp.setStatusDescription("notLogin");
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
		if(heartbeatReq == null){
			logger.error("heartbeatReq == null");
			return -1;
		}
		if(heartbeatReq.getBody() == null){
			logger.error("heartbeatReq.getBody() == null");
			return -1;
		}
		
		return 0;
	}

	private int checkTicket(String ticket){
		String userId = null;
		try{
			userId = ticketService.checkTicketAndResetTtl(ticket);
		}catch(Exception e){
			logger.error("ticketService.checkTicketAndResetTtl(ticket[{}]) fail",ticket,e);
			return -1;
		}
		if(StringUtils.isBlank(userId)) return 1;
		return 0;
	}
	
	
	
}
