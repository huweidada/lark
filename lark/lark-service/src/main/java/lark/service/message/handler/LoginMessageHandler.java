package lark.service.message.handler;


import lark.domain.Account;
import lark.domain.Client;
import lark.domain.message.LoginMessage;
import lark.message.inbound.handler.MessageInboundHandler;
import lark.message.outbound.handler.MessageOutboundHandler;
import lark.message.outbound.handler.MessageOutboundHandlerManager;
import lark.service.user.UserManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;



public class LoginMessageHandler implements MessageInboundHandler {
	private static final Logger logger = LoggerFactory.getLogger(LoginMessageHandler.class);
	
	private LoginMessage parse(String message) {
		return JSON.parseObject(message,LoginMessage.class);
	}

	public void handle(String channelId, String message) {
		LoginMessage loginMessage = parse(message);
		
		Account account = new Account();
		
		account.setChannelId(channelId);
		//备注：userId字段，ticket字段应该是后台实现用户名密码验证过后生成的
		account.setUserId(loginMessage.getBody().getUserName());
		account.setUserName(loginMessage.getBody().getUserName());
		account.setPassword(loginMessage.getBody().getPassword());
		account.setTicket(loginMessage.getBody().getPassword());
		
		Client client = new Client();
		client.setVersion(loginMessage.getBody().getClientVersion());
		client.setPlatform(loginMessage.getBody().getPlatform());
		client.setNetType(loginMessage.getBody().getNetType());
		client.setClientLocalIp(loginMessage.getBody().getClientIp());
		client.setClientLocalPort(loginMessage.getBody().getClientPort());
		client.setClientIp("139.23.33.3");
		client.setClientPort("3456");
		client.setOs(loginMessage.getBody().getOs());
		client.setDeviceNumber(loginMessage.getBody().getDeviceNumber());
		account.setClient(client);
		
		UserManager.registerAccount(account);
		MessageOutboundHandler messageOutboundHandler = MessageOutboundHandlerManager.getMessageOutboundHandler("tcp");
		messageOutboundHandler.write(channelId, "登录成功");
		
	}
	
	
}
