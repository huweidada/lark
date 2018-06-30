package lark.client.message.inbound.handler;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.UUID;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

import lark.client.domain.Application;
import lark.client.domain.ApplicationContext;
import lark.client.domain.MessageType;
import lark.client.net.NetClient;
import lark.domain.message.login.LoginReq;
import lark.domain.message.login.LoginReqBody;
import lark.domain.message.login.LoginResp;
import lark.tools.Hardware;

public class LoginFrame extends JFrame implements MessageInboundHandler{
	private static final Logger logger = LoggerFactory.getLogger(LoginFrame.class);
	
	private static final long serialVersionUID = 1L;
	private NetClient netClient;
	private HeartbeatFrame heartbeatFrame;
	
	private JTextField accountTextField;
	private JPasswordField passwordTextField;
	private String loginTransactionId = "";
	
	public LoginFrame(NetClient netClient,HeartbeatFrame heartbeatFrame) throws HeadlessException {
		super();
		this.netClient = netClient;
		this.heartbeatFrame = heartbeatFrame;
		setTitle("欢迎来到jimi的世界");
		
		/*Image icon = Toolkit.getDefaultToolkit().getImage(""); 
		setIconImage(icon);*/
		
		GridBagLayout layout = new GridBagLayout();
		setLayout(layout);
		
		
		URL image = LoginFrame.class.getClassLoader().getResource("image/timg.jpg");
		JLabel imageLogo = new JLabel(new ImageIcon(image)); 
		imageLogo.setSize(360, 175);
		add(imageLogo);
		/**************************************/
		JPanel accountPanel = new JPanel();
		
		BoxLayout accountPanelBoxLayout = new BoxLayout(accountPanel,BoxLayout.X_AXIS);
		accountPanel.setLayout(accountPanelBoxLayout);
		JLabel accountLabel = new JLabel("账号:");
		
        accountTextField = new JTextField();
        accountTextField.setSize(100, 25);
        
        JLabel registerLabel = new JLabel("用户注册");
        
        accountPanel.add(Box.createHorizontalStrut(60));
        accountPanel.add(accountLabel);
        accountPanel.add(Box.createHorizontalStrut(5));
        accountPanel.add(accountTextField);
        accountPanel.add(Box.createHorizontalStrut(5));
        accountPanel.add(registerLabel);
        accountPanel.add(Box.createHorizontalStrut(60));
        
        accountPanel.setSize(360, 30);
        
        add(accountPanel);
        /**************************************/
        JPanel passwordPanel = new JPanel(); 
        BoxLayout passwordPanelBoxLayout = new BoxLayout(passwordPanel,BoxLayout.X_AXIS);
        passwordPanel.setLayout(passwordPanelBoxLayout);
        
		JLabel passwordLabel = new JLabel("密码:");
		passwordTextField = new JPasswordField(0);
        JLabel forgetLabel = new JLabel("忘记密码");
        
        passwordPanel.add(Box.createHorizontalStrut(60));
        passwordPanel.add(passwordLabel);
        passwordPanel.add(Box.createHorizontalStrut(5));
        passwordPanel.add(passwordTextField);
        passwordPanel.add(Box.createHorizontalStrut(5));
        passwordPanel.add(forgetLabel);
        passwordPanel.add(Box.createHorizontalStrut(60));
        
        passwordPanel.setSize(360, 30);
        
        add(passwordPanel);
        /**************************************/
		
        //登录设置  
        JPanel statusPanel = new JPanel();  
        BoxLayout statusPanelBoxLayout = new BoxLayout(statusPanel,BoxLayout.X_AXIS);
        statusPanel.setLayout(statusPanelBoxLayout);
        
        JLabel statusLabel = new JLabel("状态:");

        JComboBox<String> statusComboBox = new JComboBox<String>();  
        statusComboBox.addItem("在线");  
        statusComboBox.addItem("隐身");  
        statusComboBox.addItem("离线");  
        
        JCheckBox  rememberPassword = new JCheckBox("记住密码");
        JCheckBox  autoLogon = new JCheckBox("自动登录");
         
        statusPanel.add(Box.createHorizontalStrut(60));
        statusPanel.add(statusLabel);
        statusPanel.add(Box.createHorizontalStrut(5));
        statusPanel.add(statusComboBox);
        statusPanel.add(Box.createHorizontalStrut(5));
        statusPanel.add(rememberPassword);
        statusPanel.add(Box.createHorizontalStrut(5));
        statusPanel.add(autoLogon);
        statusPanel.add(Box.createHorizontalStrut(30));
        
        statusPanel.setSize(360, 30);
        
        add(statusPanel);
        /**************************************/
          
        //底部登录按钮  
        JPanel bottomPanel = new JPanel();  
        BoxLayout bottomPanelBoxLayout = new BoxLayout(bottomPanel,BoxLayout.X_AXIS);
        bottomPanel.setLayout(bottomPanelBoxLayout);

        JButton setButton = new JButton("设置");
        setButton.setPreferredSize(new Dimension(100, 30));

        JButton loginButton = new JButton("登录");
        loginButton.setPreferredSize(new Dimension(100, 30));
        
        loginButton.addActionListener(new LoginButtonActionListener());
        
        bottomPanel.add(Box.createHorizontalGlue());
        bottomPanel.add(setButton);
        bottomPanel.add(Box.createHorizontalGlue());
        bottomPanel.add(loginButton);
        bottomPanel.add(Box.createHorizontalGlue());
        
        bottomPanel.setSize(360, 30);
        
        add(bottomPanel);
        /**************************************/
        
        GridBagConstraints s0= new GridBagConstraints();
        s0.fill = GridBagConstraints.BOTH;
        s0.gridx = 0;
        s0.gridy = 0;
        layout.setConstraints(imageLogo, s0);
        
        GridBagConstraints s1= new GridBagConstraints();
        s1.insets = new Insets(10,0,0,0);
        s1.fill = GridBagConstraints.BOTH;
        s1.gridx = 0;
        s1.gridy = 1;
        layout.setConstraints(accountPanel, s1);
        
        GridBagConstraints s2= new GridBagConstraints();
        s2.insets = new Insets(10,0,0,0);
        s2.fill = GridBagConstraints.BOTH;
        s2.gridx = 0;
        s2.gridy = 2;
        layout.setConstraints(passwordPanel, s2);
        
        GridBagConstraints s3= new GridBagConstraints();
        s3.insets = new Insets(10,0,0,0);
        s3.fill = GridBagConstraints.BOTH;
        s3.gridx = 0;
        s3.gridy = 3;
        layout.setConstraints(statusPanel, s3);
        
        GridBagConstraints s4= new GridBagConstraints();
        s4.insets = new Insets(30,0,0,0);
        s4.fill = GridBagConstraints.BOTH;
        s4.gridx = 0;
        s4.gridy = 4;
        layout.setConstraints(bottomPanel, s4);
        
        setSize(376, 380);
        setDefaultCloseOperation(EXIT_ON_CLOSE);  
        setLocationRelativeTo(null);  
        
        
        
        //注册网络消息事件处理器
        MessageInboundHandlerDispatcher.registerMessageHandler(MessageType.login, this);
		
	}
	
	@Override
	public void handle(String message) {
		LoginResp loginResp = JSON.parseObject(message,LoginResp.class);
		if(loginResp.getStatusCode() == 1 && loginTransactionId.equals(loginResp.getTransactionId())){
			logger.info("登录成功");
			//netClient.startHeartbeat(loginResp.getBody().getTicket());
			ApplicationContext.getTicket().set(loginResp.getBody().getTicket());
			heartbeatFrame.start();
			
			MainFrame mainFrame = new MainFrame(netClient);
			mainFrame.setVisible(true);
		}
	}
	
	public class LoginButtonActionListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			
			String userName = accountTextField.getText();
			String password = String.valueOf(passwordTextField.getPassword());
			
			LoginReqBody body = new LoginReqBody();
			
			body.setAuthType(1);
			body.setUserName(userName);
			body.setPassword(password);
			body.setOs(System.getProperty("os.name"));
			body.setPlatform(Application.platform);
			body.setClientVersion(Application.clientName);
			body.setNetType(netClient.getNetType());
			body.setClientIp(netClient.getLocalIp());
			body.setClientPort(netClient.getLocalPort());
			body.setDeviceNumber(Hardware.getCpuNumber());
			
			loginTransactionId = UUID.randomUUID().toString();
			LoginReq loginReq = new LoginReq();
			loginReq.setTransactionId(loginTransactionId);
			loginReq.setType("login");
			loginReq.setVersion(1);
			loginReq.setTime(System.currentTimeMillis());
			loginReq.setBody(body);
			
			String loginMessage = JSON.toJSONString(loginReq);
			int returnCode = -1;
			try {
				returnCode = netClient.send(loginMessage);
			} catch (InterruptedException e1) {
				logger.error("netClient.send fail",e1);
				return;
			}
			if(returnCode != 0){
				logger.error("netClient.send fail");
			}
			logger.info("netClient.send success");
		}
	}	

	
}




















