package lark.client.components;

import lark.client.utils.FontUtil;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class LarkTextField extends JTextField
{
	private static final long serialVersionUID = 1L;
	private String placeholder;


    public LarkTextField()
    {
        setBackground(Colors.FONT_WHITE);
        setForeground(Colors.FONT_BLACK);
        setCaretColor(Color.GRAY);
        setBorder(null);


        getDocument().addDocumentListener(new DocumentListener()
        {
            @Override
            public void insertUpdate(DocumentEvent e)
            {
            }

            @Override
            public void removeUpdate(DocumentEvent e)
            {
                if (getText().isEmpty())
                {
                    repaint();
                }

            }

            @Override
            public void changedUpdate(DocumentEvent e)
            {

            }
        });
    }

    @Override
    protected void paintComponent(java.awt.Graphics g)
    {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g ;
        if (getText().isEmpty())
        {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setBackground(Color.gray);
            g2.setFont(FontUtil.getDefaultFont());
            g2.setColor(Color.GRAY);
            g2.drawString(placeholder, 10, 25);
            g2.dispose();
        }

    }

    public String getPlaceholder()
    {
        return placeholder;
    }

    public void setPlaceholder(String placeholder)
    {
        this.placeholder = placeholder;
    }
}
