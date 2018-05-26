package lark.client.frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginFrame extends JFrame{
	private static final long serialVersionUID = 1L;
	
	public LoginFrame() throws HeadlessException {
		super();
		setTitle("欢迎登录百灵鸟");
		
		GridBagLayout layout = new GridBagLayout();
		setLayout(layout);
		
		
		URL image = LoginFrame.class.getClassLoader().getResource("image/timg.jpg");
		JLabel imageLogo = new JLabel(new ImageIcon(image)); 
		imageLogo.setSize(360, 175);
		add(imageLogo);
		/**************************************/
		JPanel accountPanel = new JPanel(); 
		accountPanel.setLayout(new GridLayout(1,3,0,0));
		JLabel accountLabel = new JLabel("账号");
        JTextField accountTextField = new JTextField(15);
        accountTextField.setSize(150, 25);
        
        JLabel registerLabel = new JLabel("用户注册");
        
        accountPanel.add(accountLabel);
        accountPanel.add(accountTextField);
        accountPanel.add(registerLabel);
        
        accountPanel.setSize(360, 30);
        
        add(accountPanel);
        /**************************************/
        JPanel passwordPanel = new JPanel(); 
		passwordPanel.setLayout(new GridLayout(1,3,0,0));
		
		JLabel passwordLabel = new JLabel("密码");
        JPasswordField passwordTextField = new JPasswordField(15);
        JLabel forgetLabel = new JLabel("忘记密码");
        
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordTextField);
        passwordPanel.add(forgetLabel);
        
        passwordPanel.setSize(360, 30);
        
        add(passwordPanel);
        /**************************************/
		
        //登录设置  
        JPanel statusPanel = new JPanel();  
        statusPanel.setLayout(new GridLayout(1,4,0,0));
        
        JLabel statusLabel = new JLabel("状态");

        JComboBox<String> statusComboBox = new JComboBox<String>();  
        statusComboBox.addItem("Q我");  
        statusComboBox.addItem("在线");  
        statusComboBox.addItem("隐身");  
        statusComboBox.addItem("离线");  
        
        JCheckBox  rememberPassword = new JCheckBox("记住密码");
        JCheckBox  autoLogon = new JCheckBox("自动登录");
         
        statusPanel.add(statusLabel);
        statusPanel.add(statusComboBox);
        statusPanel.add(rememberPassword);
        statusPanel.add(autoLogon);
        
        statusPanel.setSize(360, 30);
        
        add(statusPanel);
        /**************************************/
          
        //底部登录按钮  
        JPanel bottomPanel = new JPanel();  
        bottomPanel.setLayout(new GridLayout(1,2,0,0));  

        JButton setButton = new JButton("设置");
        JButton loginButton = new JButton("登录");
        
        bottomPanel.add(setButton);
        bottomPanel.add(loginButton);
        
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
		
	}
	
}
