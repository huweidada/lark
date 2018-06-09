package lark.service.message.handler;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

import lark.domain.AccessPoint;
import lark.domain.Server;
import lark.domain.ServerStatusCode;
import lark.domain.User;
import lark.domain.message.chat.SingleChatPush;
import lark.domain.message.chat.SingleChatPushBody;
import lark.domain.message.chat.SingleChatReq;
import lark.domain.message.chat.SingleChatResp;
import lark.domain.message.chat.SingleChatRespBody;
import lark.message.inbound.handler.MessageInboundHandler;
import lark.message.outbound.handler.MessageOutboundHandler;
import lark.message.outbound.handler.MessageOutboundHandlerManager;
import lark.service.TicketService;
import lark.service.impl.TicketServiceImpl;
import lark.service.user.UserManager;

public class SingleChatHandler implements MessageInboundHandler{
	private static final Logger logger = LoggerFactory.getLogger(SingleChatHandler.class);
	private static final String type = "singleChat";
	private static final String pushType = "singleChatPush";
	
	private TicketService ticketService;
	
	public SingleChatHandler() {
		super();
		ticketService = TicketServiceImpl.getInstance();
	}
	
	public SingleChatReq parse(String message) {
		return JSON.parseObject(message,SingleChatReq.class);
	}

	public void handle(AccessPoint accessPoint, String message) {
		logger.info("accessPoint=[{}],message=[{}]",JSON.toJSONString(accessPoint),message);
		String channelId = accessPoint.getChannelId();
		
		MessageOutboundHandler messageOutboundHandler = MessageOutboundHandlerManager.getMessageOutboundHandler(accessPoint.getTransportProtocol());
		
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
		
		String userId = null;
		try{
			userId = ticketService.checkTicketAndResetTll(singleChatReq.getBody().getTicket());
		}catch(Exception e){
			logger.error("ticketService.checkTicketAndResetTll(ticket[{}]) fail",singleChatReq.getBody().getTicket(),e);
			singleChatResp.setStatusCode(ServerStatusCode.ticketError);
			singleChatResp.setStatusDescription("ticketError");
			messageOutboundHandler.write(channelId, JSON.toJSONString(singleChatResp));
			return;
		}
		if(StringUtils.isBlank(userId)){
			logger.error("StringUtils.isBlank(userId) == true");
			singleChatResp.setStatusCode(ServerStatusCode.ticketError);
			singleChatResp.setStatusDescription("ticketError");
			messageOutboundHandler.write(channelId, JSON.toJSONString(singleChatResp));
			return;
		}
		
		
		
		//这个一般是遭受到攻击或者客户端有bug的情况，不然应该不会出现这种情况，返回系统错误
		/*
		 Account account = UserManager.getAccountByChannelId(channelId);
		 if(account == null){
			logger.error("account == null,channelId=[{}]]",channelId);
			singleChatResp.setStatusCode(ServerStatusCode.systemError);
			singleChatResp.setStatusDescription("systemError");
			messageOutboundHandler.write(channelId, JSON.toJSONString(singleChatResp));
			return;
		}*/
		
		/*if(!account.getTicket().equals(singleChatReq.getBody().getTicket())){
			logger.error("!account.getTicket().equals(ticket) == true");
			logger.error("channelId=[{}],ticket=[{}],account=[{}]",channelId,singleChatReq.getBody().getTicket(),account.toString());
			
			singleChatResp.setStatusCode(ServerStatusCode.ticketError);
			singleChatResp.setStatusDescription("ticketError");
			messageOutboundHandler.write(channelId, JSON.toJSONString(singleChatResp));
			return;
		}*/
		
		User senderUser = UserManager.getUserByUserId(userId);
		if(senderUser == null){
			logger.error("senderUser == null,userId=[{}]",userId);
			//这里返回ticketError的原因是因为senderUser其实是根据ticket查出来的
			singleChatResp.setStatusCode(ServerStatusCode.ticketError);
			singleChatResp.setStatusDescription("ticketError");
			messageOutboundHandler.write(channelId, JSON.toJSONString(singleChatResp));
			return;
		}
		
		//我们这里第一步先更新activeChannelId
		senderUser.updateActiveChannelId(channelId);
		
		
		//这里应该先检查需要发送的对方的userId是否合法，以及好友检查，是否在线检查等等，暂时先不做
		User receiverUser = UserManager.getUserByUserId(singleChatReq.getBody().getToUserId());
		if(receiverUser == null){
			logger.info("receiverUser == null,toUserId=[{}]",singleChatReq.getBody().getToUserId());
			singleChatResp.setStatusCode(StatusCode.receiverOffline.getCode());
			singleChatResp.setStatusDescription(StatusCode.receiverOffline.getDescription());
			messageOutboundHandler.write(channelId, JSON.toJSONString(singleChatResp));
			return;
		}
		
		long currentTimeMillis = System.currentTimeMillis();
		
		/*ContentText contentText = new ContentText();
		contentText.setText(singleChatReq.getBody().getContent());*/
		
		SingleChatPushBody pushBody = new SingleChatPushBody();
		pushBody.setFromUserId(senderUser.getUserId());
		pushBody.setContentType(100);
		pushBody.setContent(singleChatReq.getBody().getContent());
		
		SingleChatPush singleChatPush = new SingleChatPush();
		singleChatPush.setServer(Server.serverName);
		singleChatPush.setType(pushType);
		singleChatPush.setTime(currentTimeMillis);
		singleChatPush.setVersion(Server.version);
		singleChatPush.setTransactionId(singleChatReq.getTransactionId());
		singleChatPush.setStatusCode(ServerStatusCode.success);
		singleChatPush.setBody(pushBody);
		messageOutboundHandler.write(receiverUser.getActiveChannelId(), JSON.toJSONString(singleChatPush));
		
		
		SingleChatRespBody singleChatRespBody = new SingleChatRespBody();
		singleChatRespBody.setTimestamp(currentTimeMillis);
		singleChatResp.setBody(singleChatRespBody);
		singleChatResp.setStatusCode(ServerStatusCode.success);
		messageOutboundHandler.write(channelId, JSON.toJSONString(singleChatResp));
		return;
		
	}
	
	

	
	private int checkSingleChatReq(SingleChatReq singleChatReq){
		if(singleChatReq.getBody() == null){
			logger.error("singleChatReq.getBody() == null");
			return -1;
		}
		
		if(StringUtils.isBlank(singleChatReq.getBody().getTicket())){
			logger.error("StringUtils.isBlank(singleChatReq.getBody().getTicket()) == true");
			return -1;
		}
		
		if(StringUtils.isBlank(singleChatReq.getBody().getToUserId())){
			logger.error("StringUtils.isBlank(singleChatReq.getBody().getToUserId()) == true");
			return -1;
		}
		
		if(singleChatReq.getBody().getContentType() <= 0){
			logger.error("singleChatReq.getBody().getContentType()[{}] <= 0",singleChatReq.getBody().getContentType());
			return -1;
		}
		
		if(StringUtils.isBlank(singleChatReq.getBody().getContent())){
			logger.error("StringUtils.isBlank(singleChatReq.getBody().getContent()) == true");
			return -1;
		}
		
		return 0;
	}
		
	public static enum StatusCode{
		receiverOffline(200100,"消息接收者离线");
		
		private StatusCode(int code,String description){
			this.code = code;
			this.description = description;
		}
		public int getCode(){
			return code;
		}
		
		public String getDescription(){
			return description;
		}
		
		private int code;
		private String description;
	}
}









