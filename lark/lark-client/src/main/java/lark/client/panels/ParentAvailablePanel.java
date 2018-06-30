package lark.client.panels;

import javax.swing.JPanel;

public class ParentAvailablePanel extends JPanel
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel parent;

    public ParentAvailablePanel(JPanel parent)
    {
        this.parent = parent;
    }

    public JPanel getParentPanel()
    {
        return parent;
    }
}
