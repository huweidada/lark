package lark.server;

public class LarkServer {
	public static void main(String[] args) throws Exception {
		new LarkBootstrap().init().start((short) 8808, 1);
		//new LarkWebsocketBootstrap().start((short) 8808, 1);
	}
}
