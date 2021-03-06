package lark.service.message.handler;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import lark.domain.AccessPoint;
import lark.domain.Server;
import lark.domain.ServerStatusCode;
import lark.domain.User;
import lark.domain.message.user.ListUserReq;
import lark.domain.message.user.ListUserResp;
import lark.domain.message.user.ListUserRespBody;
import lark.message.inbound.handler.MessageInboundHandler;
import lark.message.outbound.handler.MessageOutboundHandler;
import lark.message.outbound.handler.MessageOutboundHandlerManager;
import lark.service.user.UserManager;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

public class ListUserHandler implements MessageInboundHandler{
	private static final Logger logger = LoggerFactory.getLogger(ListUserHandler.class);
	private static final String type = "listUser";

	private ListUserReq parse(String message) {
		return JSON.parseObject(message,ListUserReq.class);
	}

	public void handle(AccessPoint accessPoint, String message) {
		logger.info("accessPoint=[{}],message=[{}]",JSON.toJSONString(accessPoint),message);
		String channelId = accessPoint.getChannelId();
		
		MessageOutboundHandler messageOutboundHandler = MessageOutboundHandlerManager.getMessageOutboundHandler(accessPoint.getTransportProtocol());
		
		ListUserResp  listUserResp = new ListUserResp();
		listUserResp.setServer(Server.serverName);
		listUserResp.setType(type);
		listUserResp.setTime(System.currentTimeMillis());
		listUserResp.setVersion(Server.version);
		
		ListUserReq listUserReq = parse(message);
		if(listUserReq == null){
			listUserResp.setTransactionId("999");
			listUserResp.setStatusCode(ServerStatusCode.parameterError);
			listUserResp.setStatusDescription("parameterError");
			messageOutboundHandler.write(channelId, JSON.toJSONString(listUserResp));
			return;
		}
		if(StringUtils.isNotBlank(listUserReq.getTransactionId())){
			listUserResp.setTransactionId(listUserReq.getTransactionId());
		}else{
			listUserResp.setTransactionId("999");
		}
		
		if(checkListUserReq(listUserReq) < 0){
			listUserResp.setStatusCode(ServerStatusCode.parameterError);
			listUserResp.setStatusDescription("parameterError");
			messageOutboundHandler.write(channelId, JSON.toJSONString(listUserResp));
			return;
		}
		
		
		
		
		List<User> userList = new LinkedList<User>();
		Map<String,User> userMap = UserManager.listUser();
		userList.addAll(userMap.values());
		
		ListUserRespBody listUserRespBody = new ListUserRespBody();
		listUserRespBody.setUserList(userList);
		
		listUserResp.setBody(listUserRespBody);
		listUserResp.setStatusCode(ServerStatusCode.success);
		messageOutboundHandler.write(channelId, JSON.toJSONString(listUserResp));
		return;
		
	}
	
	private int checkListUserReq(ListUserReq listUserReq){
		return 0;
	}

}
