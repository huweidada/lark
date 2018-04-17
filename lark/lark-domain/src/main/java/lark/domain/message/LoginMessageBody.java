package lark.domain.message;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class LoginMessageBody implements Serializable{
	private static final long serialVersionUID = 1L;

	@Getter @Setter private int authType;
	@Getter @Setter private String userName;
	@Getter @Setter private String password;
}
