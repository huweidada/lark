package lark.service.message.handler;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

import lark.domain.AccessPoint;
import lark.domain.Contact;
import lark.domain.Server;
import lark.domain.ServerStatusCode;
import lark.domain.User;
import lark.domain.message.contact.ListContactReq;
import lark.domain.message.contact.ListContactResp;
import lark.domain.message.contact.ListContactRespBody;
import lark.message.inbound.handler.MessageInboundHandler;
import lark.message.outbound.handler.MessageOutboundHandler;
import lark.message.outbound.handler.MessageOutboundHandlerManager;
import lark.service.TicketService;
import lark.service.impl.TicketServiceImpl;
import lark.service.user.UserManager;

public class ListContactHandler implements MessageInboundHandler{
	private static final Logger logger = LoggerFactory.getLogger(ListContactHandler.class);
	private static final String type = "listContact";
	private TicketService ticketService;
	
	public ListContactHandler() {
		super();
		ticketService = TicketServiceImpl.getInstance();
	}
	
	private ListContactReq parse(String message) {
		return JSON.parseObject(message,ListContactReq.class);
	}
	
	@Override
	public void handle(AccessPoint accessPoint, String message) {
		logger.info("accessPoint=[{}],message=[{}]",JSON.toJSONString(accessPoint),message);
		String channelId = accessPoint.getChannelId();
		
		MessageOutboundHandler messageOutboundHandler = MessageOutboundHandlerManager.getMessageOutboundHandler(accessPoint.getTransportProtocol());
		ListContactResp  listContactResp = new ListContactResp();
		listContactResp.setServer(Server.serverName);
		listContactResp.setType(type);
		listContactResp.setTime(System.currentTimeMillis());
		listContactResp.setVersion(Server.version);
		
		
		ListContactReq listContactReq = parse(message);
		if(listContactReq == null){
			listContactResp.setTransactionId("999");
			listContactResp.setStatusCode(ServerStatusCode.parameterError);
			listContactResp.setStatusDescription("parameterError");
			messageOutboundHandler.write(channelId, JSON.toJSONString(listContactResp));
			return;
		}
		
		if(StringUtils.isNotBlank(listContactReq.getTransactionId())){
			listContactResp.setTransactionId(listContactReq.getTransactionId());
		}else{
			listContactResp.setTransactionId("999");
		}
		if(checkListContactReq(listContactReq) < 0){
			listContactResp.setStatusCode(ServerStatusCode.parameterError);
			listContactResp.setStatusDescription("parameterError");
			messageOutboundHandler.write(channelId, JSON.toJSONString(listContactResp));
			return;
		}
		
		
		int ticketStatus = checkTicket(listContactReq.getBody().getTicket());
		logger.info("ticketStatus=[{}]",ticketStatus);
		if(ticketStatus < 0){
			//这个时候需要关闭channel，这里先不处理，直接下发一个系统错误
			listContactResp.setStatusCode(ServerStatusCode.systemError);
			listContactResp.setStatusDescription("systemError");
			messageOutboundHandler.write(channelId, JSON.toJSONString(listContactResp));
			return;
		}
		
		if(ticketStatus == 1){
			//这个时候表示，用户的ticket已经过期
			listContactResp.setStatusCode(ServerStatusCode.notLogin);
			listContactResp.setStatusDescription("notLogin");
			messageOutboundHandler.write(channelId, JSON.toJSONString(listContactResp));
			return;
		}
		
		
		ListContactRespBody listContactRespBody = new ListContactRespBody();
		List<Contact> contactList = new LinkedList<Contact>();
		listContactRespBody.setData(contactList);
		listContactRespBody.setTotalRecordNumber(0);
		//进入业务逻辑处理，这里暂时使用在线用户，如果没有在线用户
		Map<String,User> userMap = UserManager.listUser();
		if(userMap == null || userMap.size() == 0){
			listContactResp.setBody(listContactRespBody);
			listContactResp.setStatusCode(ServerStatusCode.success);
			messageOutboundHandler.write(channelId, JSON.toJSONString(listContactResp));
			return;
		}
		
		Iterator<Map.Entry<String,User>> iterator = userMap.entrySet().iterator();
		while(iterator.hasNext()){
			Map.Entry<String, User> entry = iterator.next();
			Contact contact = new Contact();
			contact.setUserId(entry.getKey());
			contact.setAvatarUrl("");
			contact.setNickname(entry.getKey());
			contactList.add(contact);
		}
		listContactRespBody.setTotalRecordNumber(userMap.size());
		listContactResp.setBody(listContactRespBody);
		listContactResp.setStatusCode(ServerStatusCode.success);
		messageOutboundHandler.write(channelId, JSON.toJSONString(listContactResp));
		return;
	}

	
	
	private int checkListContactReq(ListContactReq listContactReq){
		if(listContactReq == null){
			logger.error("listContactReq == null");
			return -1;
		}
		if(listContactReq.getBody() == null){
			logger.error("listContactReq.getBody() == null");
			return -1;
		}
		
		if(StringUtils.isBlank(listContactReq.getBody().getTicket())){
			logger.error("StringUtils.isBlank(listContactReq.getBody().getTicket()) == true");
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
