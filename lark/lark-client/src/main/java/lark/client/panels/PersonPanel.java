package lark.client.panels;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class PersonPanel extends ParentAvailablePanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel avatar;
    private JLabel username;
    private JLabel menuIcon;
    
	public PersonPanel(JPanel parent)
    {
        super(parent);
        initComponents();
        initView();
    }
	
	private void initComponents()
    {
        avatar = new JLabel();
        avatar.setIcon(new ImageIcon());

        avatar.setPreferredSize(new Dimension(50, 50));
        avatar.setCursor(new Cursor(Cursor.HAND_CURSOR));


        username = new JLabel();
        username.setText("胡伟");
        username.setForeground(Colors.FONT_WHITE);


        menuIcon = new JLabel();
        menuIcon.setIcon(new ImageIcon(PersonPanel.class.getClassLoader().getResource("image/options.png")));
        menuIcon.setForeground(Colors.FONT_WHITE);
        menuIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
	
	private void initView()
    {
        this.setBackground(Colors.DARK);
        this.setLayout(new GridBagLayout());

        add(avatar, new GBC(0, 0).setFill(GBC.NONE).setWeight(2, 1));
        add(username, new GBC(1, 0).setFill(GBC.BOTH).setWeight(7, 1));
        add(menuIcon, new GBC(2, 0).setFill(GBC.BOTH).setWeight(1, 1));
    }

}
