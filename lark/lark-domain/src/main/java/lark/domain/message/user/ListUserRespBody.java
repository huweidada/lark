package lark.domain.message.user;

import java.io.Serializable;
import java.util.List;

import lark.domain.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class ListUserRespBody implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Getter @Setter private List<User> userList;
	
}
