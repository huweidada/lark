package lark.domain.message.chat;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class ContentTemplate implements Serializable {
	private static final long serialVersionUID = 1L;

	@Getter @Setter private String text;
	
}
