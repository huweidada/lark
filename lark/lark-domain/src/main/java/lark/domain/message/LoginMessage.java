package lark.domain.message;

import java.util.UUID;

import com.alibaba.fastjson.JSON;

import lombok.ToString;

@ToString
public class LoginMessage extends MessageHeader<LoginMessageBody>{
	private static final long serialVersionUID = 1L;
	
	public static void main(String[] args) {
		System.out.println("xxxxxx");
		
		
		LoginMessage message = new LoginMessage();
		
		message.setTransactionId(UUID.randomUUID().toString());
		message.setType("login");
		message.setVersion(1);
		message.setTime(System.currentTimeMillis());
		
		LoginMessageBody body = new LoginMessageBody();
		body.setAuthType(1);
		body.setUserName("huwei");
		body.setPassword("huwei");
		body.setClientVersion("1.0.0");
		body.setPlatform("web");
		body.setNetType("wifi");
		body.setClientIp("192.168.1.2");
		body.setClientPort("6023");
		body.setDeviceNumber("123456789");
		
		message.setBody(body);
		
		System.out.println(JSON.toJSONString(message));
		
	}

}
