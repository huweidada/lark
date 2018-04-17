package lark.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class Client implements Serializable{
	private static final long serialVersionUID = 1L;
		
	@Getter @Setter private String version;
	//ios，android，web，pc，mweb
	@Getter @Setter private String platform;
	@Getter @Setter private String netType;
	@Getter @Setter private String clientIp;
	@Getter @Setter private String clientPort;
	@Getter @Setter private String clientLocalIp;
	@Getter @Setter private String clientLocalPort;
	@Getter @Setter private String os;
	@Getter @Setter private String deviceNumber;
}
