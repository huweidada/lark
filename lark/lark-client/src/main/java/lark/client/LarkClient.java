package lark.client;


import lark.client.frame.LoginFrame1;

public class LarkClient {
	public static void main(String[] args) {
		
		/*NetClient netClient = new NetTcpClient("114.67.227.241",8808);
		HeartbeatFrame heartbeatFrame = new HeartbeatFrame(netClient);
		LoginFrame loginFrame = new LoginFrame(netClient,heartbeatFrame);*/
		
		LoginFrame1 loginFrame = new LoginFrame1();
		loginFrame.setVisible(true);
	}
}
