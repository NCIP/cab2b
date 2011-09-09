package edu.wustl.cab2b.client.ui.mainframe;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

/**
 * This class paints given image as background before painting any of its components. 
 * @author Chandrakant Talele
 */
class BackgroundImagePanel extends JPanel {
    private static final long serialVersionUID = 1L;

    /** Background Image*/
    protected Image backgroundImage;

    /**
     * @param backgroundImage The background image
     */
    public BackgroundImagePanel(Image backgroundImage) {
        this.backgroundImage = backgroundImage;
        setLayout(new BorderLayout());
    }

    /**
     * It paints given image as background before painting any of the components. 
     * @param graphics graphics
     */
    private void drawBackground(Graphics graphics) {
        int width = getWidth();
        int height = getHeight();
        int imageWidth = backgroundImage.getWidth(this);
        int imageHeight = backgroundImage.getHeight(this);
        for (int i = 0; i < width; i += imageWidth) {
            for (int j = 0; j < height; j += imageHeight) {
                graphics.drawImage(backgroundImage, i, j, this);
            }
        }
    }

    /**
     * Paints the component
     * @param graphics 
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        drawBackground(graphics);
    }
}