package lark.domain.message.user;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class ListUserMessageBody implements Serializable{
	private static final long serialVersionUID = 1L;

	@Getter @Setter private int maxNumber;
	@Getter @Setter private String userId;
}
