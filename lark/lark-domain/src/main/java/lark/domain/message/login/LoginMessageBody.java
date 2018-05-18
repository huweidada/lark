package lark.domain.message.login;

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
	
	@Getter @Setter private String clientVersion;
	//ios，android，web，pc，mweb
	@Getter @Setter private String platform;
	@Getter @Setter private String netType;
	@Getter @Setter private String clientIp;
	@Getter @Setter private String clientPort;
	@Getter @Setter private String os;
	@Getter @Setter private String deviceNumber;
}
