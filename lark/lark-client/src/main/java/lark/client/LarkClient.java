package lark.client;

import lark.client.frame.HeartbeatFrame;
import lark.client.frame.LoginFrame;
import lark.client.net.NetClient;
import lark.client.net.NetTcpClient;

public class LarkClient {
	public static void main(String[] args) {
		
		NetClient netClient = new NetTcpClient("114.67.227.241",8808);
		HeartbeatFrame heartbeatFrame = new HeartbeatFrame(netClient);
		LoginFrame loginFrame = new LoginFrame(netClient,heartbeatFrame);
		
		loginFrame.setVisible(true);
	}
}
