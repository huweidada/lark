package lark.client.domain;

import lombok.Getter;
import lombok.Setter;

public class ApplicationContext {
	@Setter @Getter private static String ticket;
	
	@Setter @Getter private static int netStatus;
}
