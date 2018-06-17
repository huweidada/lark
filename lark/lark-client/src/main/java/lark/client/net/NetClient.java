package lark.client.net;

public interface NetClient {
	int init();
	int send(String message);
	void close();
	void startHeartbeat(String ticket);
	
	
	
	String getLocalIp();
	int getLocalPort();
	String getNetType();
}
