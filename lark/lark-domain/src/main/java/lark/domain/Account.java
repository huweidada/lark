package lark.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class Account implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Getter @Setter private String channelId;
	
	//如此设计，是为了解决多个终端，多种登录方式登录的情况
	@Getter @Setter private String userId;
	@Getter @Setter private String userName;
	@Getter @Setter private String password;
	@Getter @Setter private String ticket;
	
	@Getter @Setter private Client client;	

}
