package lark.client.net;

public interface NetClient {
	int init();
	int send(String message) throws InterruptedException;
	void close();
	
	String getLocalIp();
	int getLocalPort();
	String getNetType();
}
