package lark.service.message.handler;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

	public void handle(String channelId, String message) {
		logger.info("channelId=[{}],message=[{}]",channelId,message);
		
		MessageOutboundHandler messageOutboundHandler = MessageOutboundHandlerManager.getMessageOutboundHandler("tcp");
		
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
		
		
		ListUserRespBody listUserRespBody = new ListUserRespBody();
		Map<String,User> userMap = UserManager.listUser();
		List<User> userList = new LinkedList<User>();
		userList.addAll(userMap.values());
		listUserRespBody.setUserList(userList);
		listUserResp.setStatusCode(ServerStatusCode.success);
		messageOutboundHandler.write(channelId, JSON.toJSONString(listUserResp));
		return;
		
	}
	
	private int checkListUserReq(ListUserReq listUserReq){
		return 0;
	}

}
