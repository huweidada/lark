package lark.client.frame;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import lark.client.components.Colors;
import lark.client.components.GBC;
import lark.client.components.LarkButton;
import lark.client.components.LarkPasswordField;
import lark.client.components.LarkTextField;
import lark.client.components.VerticalFlowLayout;
import lark.client.listener.AbstractMouseListener;
import lark.client.utils.FontUtil;
import lark.client.utils.IconUtil;
import lark.client.utils.OSUtil;

public class LoginFrame1 extends JFrame{
	private static final long serialVersionUID = 1L;
	private static final int windowWidth = 300;
    private static final int windowHeight = 400;
    
    private JPanel controlPanel;
    private JLabel closeLabel;
    private JPanel editPanel;
    private LarkTextField usernameField;
    private LarkPasswordField passwordField;
    private LarkButton loginButton;
    private JLabel titleLabel;
    private JLabel statusLabel;
    
    private static Point origin = new Point();
    
    public LoginFrame1() throws HeadlessException {
		super();
		initComponents();
		layoutComponents();
		bindListeners();
		adjustLocation();
	}
    

    private void initComponents(){
        Dimension windowSize = new Dimension(windowWidth, windowHeight);
        setMinimumSize(windowSize);
        setMaximumSize(windowSize);


        controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        //controlPanel.setBounds(0,5, windowWidth, 30);

        closeLabel = new JLabel();
        closeLabel.setIcon(IconUtil.getIcon(this, "/image/close.png"));
        closeLabel.setHorizontalAlignment(JLabel.CENTER);
        //closeLabel.setPreferredSize(new Dimension(30,30));
        closeLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        titleLabel = new JLabel();
        titleLabel.setText("登  录");
        titleLabel.setFont(FontUtil.getDefaultFont(16));


        editPanel = new JPanel();
        editPanel.setLayout(new VerticalFlowLayout(VerticalFlowLayout.TOP, 0, 5, true, false));

        Dimension textFieldDimension = new Dimension(200, 35);
        usernameField = new LarkTextField();
        usernameField.setPlaceholder("用户名");
        usernameField.setPreferredSize(textFieldDimension);
        usernameField.setFont(FontUtil.getDefaultFont(14));
        usernameField.setForeground(Colors.FONT_BLACK);
        usernameField.setMargin(new Insets(0, 15, 0, 0));

        passwordField = new LarkPasswordField();
        passwordField.setPreferredSize(textFieldDimension);
        passwordField.setPlaceholder("密码");
        //passwordField.setBorder(new RCBorder(RCBorder.BOTTOM, Colors.LIGHT_GRAY));
        passwordField.setFont(FontUtil.getDefaultFont(14));
        passwordField.setForeground(Colors.FONT_BLACK);
        passwordField.setMargin(new Insets(0, 15, 0, 0));


        loginButton = new LarkButton("登 录", Colors.MAIN_COLOR, Colors.MAIN_COLOR_DARKER, Colors.MAIN_COLOR_DARKER);
        loginButton.setFont(FontUtil.getDefaultFont(14));
        loginButton.setPreferredSize(new Dimension(200, 40));

        statusLabel = new JLabel();
        statusLabel.setForeground(Colors.RED);
        statusLabel.setText("密码不正确");
        statusLabel.setVisible(false);
    }

    private void layoutComponents(){
    	 JPanel contentPanel = new JPanel();
         contentPanel.setBorder(new LineBorder(Colors.LIGHT_GRAY));
         contentPanel.setLayout(new GridBagLayout());

         controlPanel.add(closeLabel);

         if (OSUtil.getOsType() != OSUtil.Mac_OS)
         {
             setUndecorated(true);
             contentPanel.add(controlPanel, new GBC(0, 0).setFill(GBC.BOTH).setWeight(1, 1).setInsets(5, 0, 0, 0));
         }

         JPanel titlePanel = new JPanel();
         titlePanel.add(titleLabel);

         JPanel buttonPanel = new JPanel();
         buttonPanel.setLayout(new GridBagLayout());
         buttonPanel.add(loginButton, new GBC(0, 0).setFill(GBC.HORIZONTAL).setWeight(1, 1).setInsets(10, 0, 0, 0));

         editPanel.add(usernameField);
         editPanel.add(passwordField);
         editPanel.add(buttonPanel);

         editPanel.add(statusLabel);



         add(contentPanel);
         contentPanel.add(titlePanel, new GBC(0, 1).setFill(GBC.BOTH).setWeight(1, 1).setInsets(10, 10, 0, 10));
         contentPanel.add(editPanel, new GBC(0, 2).setFill(GBC.BOTH).setWeight(1, 10).setInsets(10, 10, 0, 10));
     }
    
    private void bindListeners(){
        closeLabel.addMouseListener(new AbstractMouseListener(){
            @Override
            public void mouseClicked(MouseEvent e)
            {
                System.exit(1);
                super.mouseClicked(e);
            }

            @Override
            public void mouseEntered(MouseEvent e)
            {
                closeLabel.setBackground(Colors.LIGHT_GRAY);
                super.mouseEntered(e);
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
                closeLabel.setBackground(Colors.WINDOW_BACKGROUND);
                super.mouseExited(e);
            }
        });

        if (OSUtil.getOsType() != OSUtil.Mac_OS)
        {
            addMouseListener(new MouseAdapter()
            {
                public void mousePressed(MouseEvent e)
                {
                    // 当鼠标按下的时候获得窗口当前的位置
                    origin.x = e.getX();
                    origin.y = e.getY();
                }
            });

            addMouseMotionListener(new MouseMotionAdapter()
            {
                public void mouseDragged(MouseEvent e)
                {
                    // 当鼠标拖动时获取窗口当前位置
                    Point p = LoginFrame1.this.getLocation();
                    // 设置窗口的位置
                    LoginFrame1.this.setLocation(p.x + e.getX() - origin.x, p.y + e.getY() - origin.y);
                }
            });
        }

        loginButton.addMouseListener(new AbstractMouseListener(){
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (loginButton.isEnabled())
                {
                    //doLogin();
                }

                super.mouseClicked(e);
            }
        });

        KeyListener keyListener = new KeyListener(){
            @Override
            public void keyTyped(KeyEvent e)
            {
            }

            @Override
            public void keyPressed(KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    //doLogin();
                }
            }

            @Override
            public void keyReleased(KeyEvent e)
            {

            }
        };
        usernameField.addKeyListener(keyListener);
        passwordField.addKeyListener(keyListener);
    }
    
    private void adjustLocation(){
        Toolkit tk = Toolkit.getDefaultToolkit();
        this.setLocation((tk.getScreenSize().width - windowWidth) / 2,
                (tk.getScreenSize().height - windowHeight) / 2);
    }
}








