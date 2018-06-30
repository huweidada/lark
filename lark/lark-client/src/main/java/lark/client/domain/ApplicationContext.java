package lark.client.domain;

import java.util.concurrent.atomic.AtomicReference;

import lombok.Getter;
import lombok.Setter;

public class ApplicationContext {
	@Setter @Getter private static AtomicReference<String> ticket = new AtomicReference<String>();
	
	@Setter @Getter private static int netStatus;
}
