package lark.domain.message.user;

import lark.domain.message.MessageHeader;
import lombok.ToString;

@ToString
public class ListUserMessage extends MessageHeader<ListUserMessageBody>{
	private static final long serialVersionUID = 1L;

}
