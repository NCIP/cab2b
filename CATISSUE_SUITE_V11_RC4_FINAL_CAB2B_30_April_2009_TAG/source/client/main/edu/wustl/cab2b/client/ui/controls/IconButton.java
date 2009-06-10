package edu.wustl.cab2b.client.ui.controls;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * CaB2B customized button class wth icon image
 * @author Chetan_BH
 *
 */
public class IconButton extends JButton implements MouseListener {
    /**
     * Normal button icon image
     */
    private ImageIcon normalButtonIcon;

    /**
     * Mouse entered icon image
     */
    private ImageIcon moButtonIcon;

    /**
     * Creates button with normalIcon and moIcon 
     * @param normalIcon
     * @param moIcon
     */
    public IconButton(Image normalIcon, Image moIcon) {
        normalButtonIcon = getImageIcon(normalIcon);
        moButtonIcon = getImageIcon(moIcon);
        this.setIcon(normalButtonIcon);
        setPreferredSize(new Dimension(normalButtonIcon.getIconWidth(), normalButtonIcon.getIconHeight()));
        this.addMouseListener(this);
    }

    /**
     * Returns image icon button with specified image 
     * @param image
     * @return ImageIcon
     */
    private ImageIcon getImageIcon(Image image) {
        return new ImageIcon(image);
    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    public void mouseClicked(MouseEvent arg0) {

    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
     */
    public void mouseEntered(MouseEvent arg0) {
        setIcon(moButtonIcon);
    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     */
    public void mouseExited(MouseEvent arg0) {
        this.setIcon(normalButtonIcon);
    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    public void mousePressed(MouseEvent arg0) {

    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    public void mouseReleased(MouseEvent arg0) {

    }
}
