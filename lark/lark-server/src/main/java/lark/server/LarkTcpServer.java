package lark.server;

public class LarkTcpServer {
	public static void main(String[] args) throws Exception {
		new LarkBootstrap().init().start((short) 8808, 1);
	}
}
