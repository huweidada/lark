package lark.domain.message.status;

import java.util.UUID;

import com.alibaba.fastjson.JSON;

import lark.domain.message.MessageReqHeader;
import lombok.ToString;

@ToString
public class SubscribeFriendStatusReq extends MessageReqHeader<SubscribeFriendStatusReqBody>{
	private static final long serialVersionUID = 1L;
	
	public static void main(String[] args) {
		SubscribeFriendStatusReq req = new SubscribeFriendStatusReq();
		req.setTransactionId(UUID.randomUUID().toString());
		req.setType("subscribeFriendStatus");
		req.setVersion(1);
		req.setTime(System.currentTimeMillis());
		
		SubscribeFriendStatusReqBody body = new SubscribeFriendStatusReqBody();
		body.setTicket("11");
		req.setBody(body);
		
		System.out.println(JSON.toJSONString(req));
	}
}
