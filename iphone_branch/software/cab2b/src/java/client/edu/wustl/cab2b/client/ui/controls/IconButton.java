/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

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

    /** Function to handle MouseClicked event
     * @param arg0
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    public void mouseClicked(MouseEvent arg0) {

    }

    /**Function to handle MouseEntered event
     * @param arg0
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    public void mouseEntered(MouseEvent arg0) {
        setIcon(moButtonIcon);
    }

    /**Function to handle MouseExited event
     * @param arg0
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    public void mouseExited(MouseEvent arg0) {
        this.setIcon(normalButtonIcon);
    }

    /** Function to handle MousePressed event
     * @param arg0
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    public void mousePressed(MouseEvent arg0) {

    }

    /**Function to handle MouseReleased event
     * @param arg0
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    public void mouseReleased(MouseEvent arg0) {

    }
}
