package lark.domain.message.heartbeat;

import java.util.UUID;

import com.alibaba.fastjson.JSON;

import lark.domain.message.MessageReqHeader;

public class HeartbeatReq extends MessageReqHeader<HeartbeatReqBody>{
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		HeartbeatReq req = new HeartbeatReq();
		req.setTransactionId(UUID.randomUUID().toString());
		req.setType("heartbeat");
		req.setVersion(1);
		req.setTime(System.currentTimeMillis());
		
		HeartbeatReqBody body = new HeartbeatReqBody();
		body.setTicket("11");
		req.setBody(body);
		
		System.out.println(JSON.toJSONString(req));
	}
	
}
