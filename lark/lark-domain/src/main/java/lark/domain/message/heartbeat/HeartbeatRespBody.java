package lark.domain.message.heartbeat;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class HeartbeatRespBody implements Serializable{
	private static final long serialVersionUID = 1L;

	@Getter @Setter private String blank;
	
}
