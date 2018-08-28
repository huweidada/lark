package lark.domain.message.status;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class SubscribeFriendStatusPushBody implements Serializable{
	private static final long serialVersionUID = 1L;

	@Getter @Setter private List<FriendStatus> friendStatusList;
}
