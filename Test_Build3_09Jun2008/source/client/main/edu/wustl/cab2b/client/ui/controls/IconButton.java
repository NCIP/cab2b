package edu.wustl.cab2b.client.ui.controls;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class IconButton extends JButton implements MouseListener
{
	private ImageIcon m_normalIcon;
	private ImageIcon m_moIcon;
	
	public IconButton(Image normalIcon, Image moIcon)
	{
		m_normalIcon = 	getImageIcon(normalIcon);
		m_moIcon = getImageIcon(moIcon);
		this.setIcon(m_normalIcon);
		setPreferredSize(new Dimension(m_normalIcon.getIconWidth(), m_normalIcon.getIconHeight()));
		this.addMouseListener(this);
	}
	
	private ImageIcon getImageIcon(Image image)
	{
		return new ImageIcon(image);
	}

	public void mouseClicked(MouseEvent arg0) 
	{
		
	}

	public void mouseEntered(MouseEvent arg0) 
	{
		setIcon(m_moIcon);
	}

	public void mouseExited(MouseEvent arg0) 
	{
		this.setIcon(m_normalIcon);
	}

	public void mousePressed(MouseEvent arg0) {
		
	}

	public void mouseReleased(MouseEvent arg0) {
		
	}

}
