package lark.tools;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class NamedThreadFactory implements ThreadFactory {
	
	private String prefix;
	private boolean daemon;
	private AtomicInteger id = new AtomicInteger(1);
	
	
	public NamedThreadFactory(String prefix) {
		this(prefix, false);
	}
	
	public NamedThreadFactory(String prefix, boolean daemon) {
		this.prefix = prefix;
		this.daemon = daemon;
	}
	
	

	public Thread newThread(Runnable r) {
		Thread t = new Thread(r);
		t.setName(prefix+"-thread-pool-"+id.incrementAndGet());
		t.setDaemon(daemon);
		return t;
	}

}
