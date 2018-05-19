package lark.domain.message.login;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class LoginRespBody implements Serializable{
	private static final long serialVersionUID = 1L;
	@Getter @Setter private String ticket;
}
