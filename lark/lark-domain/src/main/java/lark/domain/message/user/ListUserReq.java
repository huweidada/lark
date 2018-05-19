package lark.domain.message.user;

import lark.domain.message.MessageReqHeader;
import lombok.ToString;

@ToString
public class ListUserReq extends MessageReqHeader<ListUserReqBody>{
	private static final long serialVersionUID = 1L;

}
