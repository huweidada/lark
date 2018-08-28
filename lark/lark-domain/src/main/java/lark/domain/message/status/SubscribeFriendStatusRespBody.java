package lark.domain.message.status;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class SubscribeFriendStatusRespBody implements Serializable{
	private static final long serialVersionUID = 1L;

	@Getter @Setter private String blank;
}
