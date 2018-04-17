package lark.domain.message;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class SingleChatMessageBody implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Getter @Setter private String fromUserId;
	@Getter @Setter private String toUserId;
	@Getter @Setter private String content;

}
