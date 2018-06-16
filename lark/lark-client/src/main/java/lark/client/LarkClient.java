package lark.client;

import lark.client.frame.Heartbeater;
import lark.client.frame.LoginFrame;
import lark.client.net.NetClient;
import lark.client.net.TcpClient;

public class LarkClient {
	public static void main(String[] args) {
		Heartbeater heartbeater = new Heartbeater();
		NetClient netClient = new TcpClient("114.67.227.241",8808,heartbeater);
		LoginFrame loginFrame = new LoginFrame(netClient);
		loginFrame.setVisible(true);
	}
}
