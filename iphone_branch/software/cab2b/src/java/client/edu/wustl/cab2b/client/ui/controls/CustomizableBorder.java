/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.controls;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.border.AbstractBorder;

/**
 * A customizable border.
 * @author chetan_bh
 */
public class CustomizableBorder extends AbstractBorder {
    /**
     * Default Insets.
     */
    Insets insets = new Insets(1, 1, 1, 1);

    Color outlineColor = Color.BLACK;

    Color borderFillColor = Color.WHITE;

    boolean isBorderOpaque = true;

    boolean wantBorderline = true;

    /**
     * Creates border with specified insets, borderline and BorderOpaque properties  
     * @param insets
     * @param isBorderOpaque
     * @param wantBorderline
     */
    public CustomizableBorder(Insets insets, boolean isBorderOpaque, boolean wantBorderline) {
        this.insets = insets;
        this.isBorderOpaque = isBorderOpaque;
        this.wantBorderline = wantBorderline;
    }

    /**
     * Creates border with specified insets and border line color
     * @param insets
     * @param borderLineColor
     */
    public CustomizableBorder(Insets insets, Color borderLineColor) {
        this.insets = insets;
        this.isBorderOpaque = true;
        this.wantBorderline = true;
        outlineColor = borderLineColor;
    }

    /** Checks for Opaque Border
     * @return boolean value
     * @see javax.swing.border.AbstractBorder#isBorderOpaque()
     */
    public boolean isBorderOpaque() {
        return isBorderOpaque;
    }

    /** returns border insets
     * @param c
     * @return insets
     * @see javax.swing.border.AbstractBorder#getBorderInsets(java.awt.Component)
     */
    public Insets getBorderInsets(Component c) {
        return insets;
    }

    /** paints the border of given component
     * @param c
     * @param g
     * @param x
     * @param y
     * @param widht
     * @param height
     * @see javax.swing.border.AbstractBorder#paintBorder(java.awt.Component, java.awt.Graphics, int, int, int, int)
     */
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {

        g.setColor(borderFillColor);
        /*Top Border.*/
        g.fillRect(x, y, width, insets.top);
        /*Left Border.*/
        g.fillRect(x, y, x + insets.left, height);
        /*Bottom Border.*/
        g.fillRect(x, y + height - insets.bottom, width, insets.bottom);
        /*Right Border.*/
        g.fillRect(x + width - insets.right, y, insets.right, height);

        if (wantBorderline) {
            g.setColor(outlineColor);
            //g.drawLine(x,y, x+width, y);
            g.drawLine(x + insets.left, y + insets.top, x + width - insets.right, y + insets.top);
            //g.drawLine(x,y,x,y+height);
            g.drawLine(x + insets.left, y + insets.top, x + insets.left, y + height - insets.left);
            //g.drawLine(x,y+height-1,x+width,y+height-1);
            g.drawLine(x + width - insets.right, y + height - insets.bottom, x + insets.left, y + height
                    - insets.bottom);
            //g.drawLine(x+width-1,y+height-1,x+width-1,y);
            g.drawLine(x + width - insets.right, y + height - insets.bottom, x + width - insets.right, y
                    + insets.top);
        }

    }
}
