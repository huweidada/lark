package lark.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class AccessPoint implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Getter @Setter private String channelId;
	@Getter @Setter private String serverIp;
	@Getter @Setter private int serverPort;
	@Getter @Setter private String clientIp;
	@Getter @Setter private int clientPort;
	
}
