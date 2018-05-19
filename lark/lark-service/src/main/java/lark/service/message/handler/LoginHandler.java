package lark.service.message.handler;


import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Base64.Encoder;

import lark.domain.Account;
import lark.domain.Client;
import lark.domain.Server;
import lark.domain.ServerStatusCode;
import lark.domain.message.login.LoginReq;
import lark.domain.message.login.LoginResp;
import lark.domain.message.login.LoginRespBody;
import lark.message.inbound.handler.MessageInboundHandler;
import lark.message.outbound.handler.MessageOutboundHandler;
import lark.message.outbound.handler.MessageOutboundHandlerManager;
import lark.service.user.UserManager;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;



public class LoginHandler implements MessageInboundHandler {
	private static final Logger logger = LoggerFactory.getLogger(LoginHandler.class);
	private static final String type = "login";
	
	private LoginReq parse(String message) {
		return JSON.parseObject(message,LoginReq.class);
	}

	public void handle(String channelId, String message) {
		logger.info("channelId=[{}],message=[{}]",channelId,message);
		
		MessageOutboundHandler messageOutboundHandler = MessageOutboundHandlerManager.getMessageOutboundHandler("tcp");
		
		LoginResp  loginResp = new LoginResp();
		loginResp.setServer(Server.serverName);
		loginResp.setType(type);
		loginResp.setTime(System.currentTimeMillis());
		loginResp.setVersion(Server.version);
		
		LoginReq loginReq = parse(message);
		if(loginReq == null){
			loginResp.setTransactionId("999");
			loginResp.setStatusCode(ServerStatusCode.parameterError);
			loginResp.setStatusDescription("parameterError");
			messageOutboundHandler.write(channelId, JSON.toJSONString(loginResp));
			return;
		}
		if(StringUtils.isNotBlank(loginReq.getTransactionId())){
			loginResp.setTransactionId(loginReq.getTransactionId());
		}else{
			loginResp.setTransactionId("999");
		}
		
		if(checkLoginReq(loginReq) < 0){
			loginResp.setStatusCode(ServerStatusCode.parameterError);
			loginResp.setStatusDescription("parameterError");
			messageOutboundHandler.write(channelId, JSON.toJSONString(loginResp));
			return;
		}
		
		int authStatus = authUser(loginReq.getBody().getAuthType(),loginReq.getBody().getUserName(),loginReq.getBody().getPassword());
		if(authStatus == -1){
			loginResp.setStatusCode(StatusCode.authTypeError.getCode());
			loginResp.setStatusDescription(StatusCode.authTypeError.getDescription());
			messageOutboundHandler.write(channelId, JSON.toJSONString(loginResp));
			return;
		}else if(authStatus == -2){
			loginResp.setStatusCode(StatusCode.userIdError.getCode());
			loginResp.setStatusDescription(StatusCode.userIdError.getDescription());
			messageOutboundHandler.write(channelId, JSON.toJSONString(loginResp));
			return;
		}else if(authStatus == -3){
			loginResp.setStatusCode(StatusCode.passwordError.getCode());
			loginResp.setStatusDescription(StatusCode.passwordError.getDescription());
			messageOutboundHandler.write(channelId, JSON.toJSONString(loginResp));
			return;
		}
		
		
		String userId = getUserId(loginReq.getBody().getAuthType(),loginReq.getBody().getUserName(),loginReq.getBody().getPassword());
		String ticket = generateTicket(loginReq.getBody().getAuthType(),loginReq.getBody().getUserName(),loginReq.getBody().getPassword());
		
		Account account = new Account();
		
		account.setChannelId(channelId);
		//备注：userId字段，ticket字段应该是后台实现用户名密码验证过后生成的
		account.setUserId(userId);
		account.setUserName(loginReq.getBody().getUserName());
		account.setPassword(loginReq.getBody().getPassword());
		account.setTicket(ticket);
		
		Client client = new Client();
		client.setVersion(loginReq.getBody().getClientVersion());
		client.setPlatform(loginReq.getBody().getPlatform());
		client.setNetType(loginReq.getBody().getNetType());
		client.setClientLocalIp(loginReq.getBody().getClientIp());
		client.setClientLocalPort(loginReq.getBody().getClientPort());
		client.setClientIp("139.23.33.3");
		client.setClientPort("3456");
		client.setOs(loginReq.getBody().getOs());
		client.setDeviceNumber(loginReq.getBody().getDeviceNumber());
		account.setClient(client);
		
		UserManager.registerAccount(account);
		
		
		
		LoginRespBody loginRespBody = new LoginRespBody();
		loginRespBody.setTicket(ticket);
		loginResp.setBody(loginRespBody);
		loginResp.setStatusCode(ServerStatusCode.success);
		messageOutboundHandler.write(channelId, JSON.toJSONString(loginResp));
		return;
	}
	
	
	private int checkLoginReq(LoginReq loginReq){
		return 0;
	}
	
	private int authUser(int authType,String userName,String password){
		return 0;
	}
	
	private String getUserId(int authType,String userName,String password){
		return userName;
	}
	
	private String generateTicket(int authType,String userName,String password){
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			logger.error("MessageDigest.getInstance(\"MD5\") fail",e);
			throw new RuntimeException("MessageDigest.getInstance(\"MD5\" fail");
		}
		
		byte[] input;
		try {
			input = (userName + password).getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("userName=[{}],password=[{}]", userName, password);
			logger.error("getBytes(\"UTF-8\") fail",e);
			throw new RuntimeException("getBytes(\"UTF-8\") fail");
		}
		byte[] output = md5.digest(input);
		Encoder encoder = Base64.getEncoder();
		
		return encoder.encodeToString(output);
	}
	
	//注意100一下为公共的返回码定义，100以上的才是业务产生的编码
	//返回码格式为：xxxyyy，一共六位，公共返回码只有三位
	public static enum StatusCode{
		authTypeError(100100,"认证类型错误"),
		userIdError(100110,"用户名错误"),
		passwordError(100120, "密码错误");
		
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




