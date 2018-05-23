package lark.domain.message.user;

import java.util.UUID;

import com.alibaba.fastjson.JSON;

import lark.domain.message.MessageReqHeader;
import lombok.ToString;

@ToString
public class ListUserReq extends MessageReqHeader<ListUserReqBody>{
	private static final long serialVersionUID = 1L;
	public static void main(String[] args) {
		ListUserReq req = new ListUserReq();
		req.setTransactionId(UUID.randomUUID().toString());
		req.setType("listUser");
		req.setVersion(1);
		req.setTime(System.currentTimeMillis());
		
		ListUserReqBody body = new ListUserReqBody();
		body.setTicket("11");
		req.setBody(body);
		
		System.out.println(JSON.toJSONString(req));
	}
}
