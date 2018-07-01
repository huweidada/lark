package lark.client.frame;

import java.awt.BorderLayout;
import java.awt.HeadlessException;

import javax.swing.JFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lark.client.net.NetClient;
import lark.client.panels.PersonPanel;

public class MainFrame extends JFrame{
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(MainFrame.class);
	
	private NetClient netClient;
	
	private PersonPanel personPanel;
	
	public MainFrame(NetClient netClient) throws HeadlessException {
		super();
		this.netClient = netClient;
		
		personPanel = new PersonPanel(null);
		
		add(personPanel,BorderLayout.WEST);
		
	}

}
