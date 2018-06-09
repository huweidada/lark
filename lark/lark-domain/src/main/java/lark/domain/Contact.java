package lark.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
public class Contact implements Serializable{
	private static final long serialVersionUID = 1L;
	@Getter @Setter private String userId;
	@Getter @Setter private String avatarUrl;
	@Getter @Setter private String nickname;
}
