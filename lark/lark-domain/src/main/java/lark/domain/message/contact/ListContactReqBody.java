package lark.domain.message.contact;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class ListContactReqBody implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Getter @Setter private String ticket;
	
}
