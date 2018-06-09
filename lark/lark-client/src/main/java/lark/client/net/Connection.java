package lark.client.net;

public interface Connection {
	void init();
	
	int write(String message);
	
	
}
