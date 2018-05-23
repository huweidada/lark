package lark.domain.message.chat;

import java.util.UUID;

import com.alibaba.fastjson.JSON;

import lark.domain.message.MessageReqHeader;
import lombok.ToString;

@ToString
public class SingleChatReq extends MessageReqHeader<SingleChatReqBody>{
	private static final long serialVersionUID = 1L;
	public static void main(String[] args) {
		SingleChatReq req = new SingleChatReq();
		req.setTransactionId(UUID.randomUUID().toString());
		req.setType("singleChat");
		req.setVersion(1);
		req.setTime(System.currentTimeMillis());
		
		SingleChatReqBody body = new SingleChatReqBody();
		body.setToUserId("baicheng");
		body.setTicket("xxxx");
		body.setContentType(100);
		body.setContent("第一条消息");
		req.setBody(body);
		
		System.out.println(JSON.toJSONString(req));
	}
}
