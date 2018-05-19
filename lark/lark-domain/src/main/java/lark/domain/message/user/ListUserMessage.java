package lark.domain.message.user;

import lark.domain.message.MessageReqHeader;
import lombok.ToString;

@ToString
public class ListUserMessage extends MessageReqHeader<ListUserMessageBody>{
	private static final long serialVersionUID = 1L;

}
