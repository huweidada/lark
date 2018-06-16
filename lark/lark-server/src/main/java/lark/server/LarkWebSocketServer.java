package lark.server;

public class LarkWebSocketServer {
	public static void main(String[] args) throws Exception {
		new LarkWebsocketBootstrap().init().start((short) 8808, 1);
	}
}
